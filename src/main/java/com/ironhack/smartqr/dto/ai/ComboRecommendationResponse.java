package com.ironhack.smartqr.dto.ai;

import java.math.BigDecimal;

public record ComboRecommendationResponse(String preferences, BigDecimal maxBudget, Integer numberOfPeople,
        String recommendation
) {
}
