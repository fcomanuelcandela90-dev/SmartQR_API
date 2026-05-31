package com.ironhack.smartqr.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardPaymentRequest(
        @NotNull(message = "Order ID is required")
        Long orderId,

        @NotBlank(message = "Card holder name is obligatory")
        String cardHolderName,

        @NotBlank(message = "Last 4 digits of the card are obligatory")
        String last4Digits,

        @NotBlank(message = "Transaction Stripe ID is required")
        String transactionId
) {
}

