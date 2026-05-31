package com.ironhack.smartqr.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CashPaymentRequest(
        @NotNull(message = "Order ID is obligatory")
        Long orderId,

        @NotNull(message = "Cash Received is obligatory")
        @Positive(message = "Cash Received must be a positive number")
        BigDecimal cashReceived
) {
}
