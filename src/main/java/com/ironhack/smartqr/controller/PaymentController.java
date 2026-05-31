package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.payment.CardPaymentRequest;
import com.ironhack.smartqr.dto.payment.CashPaymentRequest;
import com.ironhack.smartqr.dto.payment.PaymentResponse;
import com.ironhack.smartqr.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // POST: Process a card payment
    @PostMapping("/card")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse processCardPayment(@Valid @RequestBody CardPaymentRequest request) {
        return paymentService.processCardPayment(request);
    }

    //POST: Process a cash payment
    @PostMapping("/cash/request")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse requestCashPayment(@Valid @RequestBody CashPaymentRequest request) {
        return paymentService.requestCashPayment(request);
    }

    //PUT: Confirm a cash payment
    @PutMapping("/cash/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse confirmCashPayment(@PathVariable Long orderId){
        return paymentService.confirmCashPayment(orderId);
    }
}