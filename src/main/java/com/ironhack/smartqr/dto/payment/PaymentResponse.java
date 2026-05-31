package com.ironhack.smartqr.dto.payment;

import com.ironhack.smartqr.enums.PaymentMethod;
import com.ironhack.smartqr.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long id, BigDecimal amount, PaymentStatus paymentStatus, PaymentMethod paymentMethod,
                              LocalDateTime createdAt) {
}
