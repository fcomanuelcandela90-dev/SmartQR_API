package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.feedback.FeedbackRequest;
import com.ironhack.smartqr.dto.feedback.FeedbackResponse;
import com.ironhack.smartqr.dto.feedback.FeedbackStatisticsResponse;
import com.ironhack.smartqr.entity.FeedBack;
import com.ironhack.smartqr.entity.Order;
import com.ironhack.smartqr.entity.User;
import com.ironhack.smartqr.enums.SentimentType;
import com.ironhack.smartqr.exception.ExternalServiceException;
import com.ironhack.smartqr.exception.ResourceNotFoundException;
import com.ironhack.smartqr.repository.FeedBackRepository;
import com.ironhack.smartqr.repository.OrderRepository;
import com.ironhack.smartqr.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ChatClient chatClient;

    @Transactional
    public FeedbackResponse createFeedback(String customerEmail, FeedbackRequest request) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with email: " + customerEmail
                ));

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + request.orderId()
                ));

        if (order.getUser() == null || !order.getUser().getId().equals(customer.getId())) {
            throw new AccessDeniedException(
                    "Cannot submit feedback: this order does not belong to the authenticated customer."
            );
        }

        SentimentType sentiment = analyzeSentiment(request.comment(), request.rating());

        FeedBack feedBack = new FeedBack();
        feedBack.setComment(request.comment());
        feedBack.setRating(request.rating());
        feedBack.setSentiment(sentiment);
        feedBack.setCreatedAt(LocalDateTime.now());
        feedBack.setUser(customer);
        feedBack.setOrder(order);

        FeedBack savedFeedBack = feedBackRepository.save(feedBack);

        return mapToResponse(savedFeedBack);
    }


    private SentimentType analyzeSentiment(String comment, Integer rating) {
        String customerFeedback = """
                                    Rating: %d out of 5
                                    Customer comment: %s
                                  """.formatted(rating, comment);

        String aiResponse;

        try {
            aiResponse = chatClient.prompt()
                    .system("""
                    You are a sentiment classifier for restaurant customer feedback.
                    Classify the customer's feedback using exactly one of these values:
                    POSITIVE
                    NEUTRAL
                    NEGATIVE

                    Reply only with one uppercase value.
                    Do not include punctuation, explanations or extra text.
                    """)
                    .user(customerFeedback)
                    .call()
                    .content();
        } catch (Exception exception) {
            throw new ExternalServiceException(
                    "OpenAI sentiment analysis service is currently unavailable.",
                    exception
            );
        }

        if (aiResponse == null || aiResponse.isBlank()) {
            throw new ExternalServiceException(
                    "OpenAI did not return a sentiment classification."
            );
        }

        String normalizedSentiment = aiResponse
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z]", "");

        try {
            return SentimentType.valueOf(normalizedSentiment);
        } catch (IllegalArgumentException exception) {
            throw new ExternalServiceException(
                    "OpenAI returned an invalid sentiment classification."
            );
        }
    }

    public FeedbackStatisticsResponse getFeedbackStatistics() {
        List<FeedBack> feedbackList = feedBackRepository.findAll();

        Long totalFeedback = (long) feedbackList.size();
        Long positiveCount = 0L;
        Long neutralCount = 0L;
        Long negativeCount = 0L;

        Integer totalRating = 0;
        Integer feedbackWithRating = 0;

        for (FeedBack feedback : feedbackList) {
            if (feedback.getRating() != null) {
                totalRating = totalRating + feedback.getRating();
                feedbackWithRating++;
            }

            if (feedback.getSentiment() == SentimentType.POSITIVE) {
                positiveCount++;
            } else if (feedback.getSentiment() == SentimentType.NEUTRAL) {
                neutralCount++;
            } else if (feedback.getSentiment() == SentimentType.NEGATIVE) {
                negativeCount++;
            }
        }

        BigDecimal averageRating = BigDecimal.ZERO;

        if (feedbackWithRating > 0) {
            averageRating = BigDecimal.valueOf(totalRating)
                    .divide(
                            BigDecimal.valueOf(feedbackWithRating),
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return new FeedbackStatisticsResponse(
                totalFeedback,
                averageRating,
                positiveCount,
                neutralCount,
                negativeCount
        );
    }

    private FeedbackResponse mapToResponse(FeedBack feedBack) {
        return new FeedbackResponse(
                feedBack.getId(),
                feedBack.getOrder().getId(),
                feedBack.getRating(),
                feedBack.getComment(),
                feedBack.getSentiment(),
                feedBack.getCreatedAt()
        );
    }
}




