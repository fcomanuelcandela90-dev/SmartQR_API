package com.ironhack.smartqr.dto.ai;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ComboRecommendationRequest(

    @NotBlank(message = "Preferences are required")
    @Size(max = 500, message = "Preferences cannot exceed 500 characters")
    String preferences,

    @NotNull(message = "Maximum budget is required")
    @DecimalMin(value = "1.00", message = "Maximum budget must be at least 1.00 EUR")
    BigDecimal maxBudget,

    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "Number of people must be at least 1")
    @Max(value = 10, message = "Number of people cannot exceed 10")
    Integer numberOfPeople


) {
}
