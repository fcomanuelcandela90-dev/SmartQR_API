package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.payment.CardPaymentRequest;
import com.ironhack.smartqr.dto.payment.CashPaymentRequest;
import com.ironhack.smartqr.dto.payment.PaymentResponse;
import com.ironhack.smartqr.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/card")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse processCardPayment(
            Authentication authentication,
            @Valid @RequestBody CardPaymentRequest request
    ) {
        return paymentService.processCardPayment(authentication.getName(), request);
    }

    @PostMapping("/cash/request")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse requestCashPayment(
            Authentication authentication,
            @Valid @RequestBody CashPaymentRequest request
    ) {
        return paymentService.requestCashPayment(authentication.getName(), request);
    }

    @PutMapping("/cash/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse confirmCashPayment(@PathVariable Long orderId) {
        return paymentService.confirmCashPayment(orderId);
    }

    @GetMapping(value = "/ticket/{orderId}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String generatePrintableTicket(
            Authentication authentication,
            @PathVariable Long orderId
    ) {
        return paymentService.generatePrintableTicket(
                authentication.getName(),
                hasInternalStaffRole(authentication),
                orderId
        );
    }

    private boolean hasInternalStaffRole(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_EMPLOYEE".equals(authority.getAuthority())
                    || "ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }

        return false;
    }
}
