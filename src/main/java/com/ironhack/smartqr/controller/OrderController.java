package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.order.CreateOrderRequest;
import com.ironhack.smartqr.dto.order.OrderResponse;
import com.ironhack.smartqr.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}