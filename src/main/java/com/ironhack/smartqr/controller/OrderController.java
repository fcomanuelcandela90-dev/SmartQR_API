package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.order.CreateOrderRequest;
import com.ironhack.smartqr.dto.order.OrderItemRequest;
import com.ironhack.smartqr.dto.order.OrderResponse;
import com.ironhack.smartqr.enums.OrderStatus;
import com.ironhack.smartqr.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(Authentication authentication, @Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(authentication.getName(), request);
    }

    @GetMapping("/table/{tableNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getOrdersByTable(@PathVariable Integer tableNumber) {
        return orderService.getOrdersByTable(tableNumber);
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse updateOrderItems(@PathVariable Long orderId, @Valid @RequestBody List<OrderItemRequest> newItemsRequest) {
        return orderService.updateOrderItems(orderId, newItemsRequest);
    }

    @PatchMapping("/{orderId}/status")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @PutMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/kitchen/queue")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getKitchenQueue() {
        return orderService.getKitchenQueue();
    }
}