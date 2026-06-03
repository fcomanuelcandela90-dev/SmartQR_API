package com.ironhack.smartqr.dto.payment;

import jakarta.validation.constraints.*;

public record CardPaymentRequest(
        @NotNull(message = "Order ID is required")
        @Positive(message = "Order ID must be greater than zero")
        Long orderId,

        @NotBlank(message = "Card holder name is obligatory")
        @Size(max = 100, message = "Card holder name cannot exceed 100 characters")
        String cardHolderName,

        @NotBlank(message = "Last 4 digits of the card are obligatory")
        @Pattern(regexp = "\\d{4}", message = "Last 4 digits must contain exactly 4 numbers")
        String last4Digits,

        @NotBlank(message = "Transaction Stripe ID is required")
        @Size(max = 150, message = "Transaction ID cannot exceed 150 characters")
        String transactionId
) {
}

