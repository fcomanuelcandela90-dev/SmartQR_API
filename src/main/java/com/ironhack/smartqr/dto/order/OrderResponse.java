package com.ironhack.smartqr.dto.order;

import com.ironhack.smartqr.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long orderId, Integer tableNumber, OrderStatus status, BigDecimal totalPrice, Integer stimatedTime,
                            LocalDateTime createdAt, List<OrderItemResponse> items) {
}
