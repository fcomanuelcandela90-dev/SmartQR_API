package com.ironhack.smartqr.dto.feedback;

import java.math.BigDecimal;

public record FeedbackStatisticsResponse(Long totalFeedback, BigDecimal averageRating, Long positiveCount,
        Long neutralCount, Long negativeCount
) {
}