package com.ironhack.smartqr.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OrderItemRequest(

        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be greater than zero")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity,

        @Size(max = 300, message = "Notes cannot exceed 300 characters")
        String notes
) {
}
