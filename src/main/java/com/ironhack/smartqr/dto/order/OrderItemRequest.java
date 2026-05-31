package com.ironhack.smartqr.dto.order;

import java.math.BigDecimal;

public record OrderItemRequest(Long productId, String productName, Integer quantity, BigDecimal subtotal, String notes)  {
}
