package com.ironhack.smartqr.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;


public record CreateOrderRequest(

        @NotNull(message = "Table number is required")
        @Positive(message = "Table number must be greater than zero")
        Integer tableNumber,

        @NotEmpty(message = "Order must contain at least one item")
        @Valid
        List<OrderItemRequest> items
) {
}
