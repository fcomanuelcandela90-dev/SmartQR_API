package com.ironhack.smartqr.dto.order;

import java.util.List;

public record CreateOrderRequest(Integer tableNumber, List<OrderItemRequest> items) {
}
