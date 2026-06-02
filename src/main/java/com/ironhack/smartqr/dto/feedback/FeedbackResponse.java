package com.ironhack.smartqr.dto.feedback;

import com.ironhack.smartqr.enums.SentimentType;

import java.time.LocalDateTime;

public record FeedbackResponse(Long id, Long orderId, Integer rating, String comment, SentimentType sentiment,
        LocalDateTime createdAt
) {
}