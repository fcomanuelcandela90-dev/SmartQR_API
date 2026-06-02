package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.feedback.FeedbackRequest;
import com.ironhack.smartqr.dto.feedback.FeedbackResponse;
import com.ironhack.smartqr.entity.FeedBack;
import com.ironhack.smartqr.entity.Order;
import com.ironhack.smartqr.entity.User;
import com.ironhack.smartqr.enums.SentimentType;
import com.ironhack.smartqr.repository.FeedBackRepository;
import com.ironhack.smartqr.repository.OrderRepository;
import com.ironhack.smartqr.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + customerEmail));

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.orderId()));

        if (order.getUser() == null || !order.getUser().getId().equals(customer.getId())) {
            throw new IllegalStateException(
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

        String aiResponse = chatClient.prompt()
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

        if (aiResponse == null || aiResponse.isBlank()) {
            throw new IllegalStateException("OpenAI did not return a sentiment classification.");
        }

        String normalizedSentiment = aiResponse
                .trim()
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z]", "");

        try {
            return SentimentType.valueOf(normalizedSentiment);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "OpenAI returned an invalid sentiment classification: " + aiResponse
            );
        }
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




