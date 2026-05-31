package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.payment.CardPaymentRequest;
import com.ironhack.smartqr.dto.payment.CashPaymentRequest;
import com.ironhack.smartqr.dto.payment.PaymentResponse;
import com.ironhack.smartqr.entity.CardPayment;
import com.ironhack.smartqr.entity.CashPayment;
import com.ironhack.smartqr.entity.Order;
import com.ironhack.smartqr.enums.OrderStatus;
import com.ironhack.smartqr.enums.PaymentMethod;
import com.ironhack.smartqr.enums.PaymentStatus;
import com.ironhack.smartqr.repository.CashPaymentRepository;
import com.ironhack.smartqr.repository.OrderRepository;
import com.ironhack.smartqr.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CashPaymentRepository cashPaymentRepository;

    @Transactional
    public PaymentResponse processCardPayment(CardPaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order ID not found: " + request.orderId()));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot process payment: This order is already paid or unavailable.");
        }

        boolean gatewayApproval = true; // Simulador de pasarela
        if (!gatewayApproval) {
            throw new RuntimeException("Stripe Gateway rejected the credit card transaction.");
        }

        CardPayment cardPayment = new CardPayment();
        cardPayment.setOrder(order);
        cardPayment.setAmount(order.getTotalPrice());
        cardPayment.setPaymentStatus(PaymentStatus.COMPLETED); // Cambiado a COMPLETED
        cardPayment.setPaymentMethod(PaymentMethod.CARD);
        cardPayment.setCreatedAt(LocalDateTime.now());
        cardPayment.setCardHolderName(request.cardHolderName());
        cardPayment.setLast4Digits(request.last4Digits());
        cardPayment.setTransactionId(request.transactionId());

        CardPayment savedPayment = paymentRepository.save(cardPayment);

        // Pasa directamente a cocina al cobrar con tarjeta
        order.setStatus(OrderStatus.IN_KITCHEN);
        orderRepository.save(order);

        return mapToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse requestCashPayment(CashPaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.orderId()));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot request cash checkout: This order is already paid or unavailable.");
        }

        CashPayment cashPayment = new CashPayment();
        cashPayment.setOrder(order);
        cashPayment.setAmount(order.getTotalPrice());
        cashPayment.setPaymentStatus(PaymentStatus.PENDING);
        cashPayment.setPaymentMethod(PaymentMethod.CASH);
        cashPayment.setCreatedAt(LocalDateTime.now());
        cashPayment.setCashReceived(request.cashReceived());

        cashPayment.setChangeAmount(request.cashReceived().subtract(order.getTotalPrice()));
        cashPayment.setConfirmed(false);

        CashPayment savedPayment = paymentRepository.save(cashPayment);

        // Se mantiene en PENDING hasta que el cajero confirme el pago en metálico
        return mapToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse confirmCashPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order ID not found: " + orderId));

        List<CashPayment> allCashPayments = cashPaymentRepository.findAll();
        CashPayment targetPayment = null;

        for (CashPayment p : allCashPayments) {
            if (p.getOrder().getId().equals(orderId) && !p.getConfirmed()) {
                targetPayment = p;
                break;
            }
        }

        if (targetPayment == null) {
            throw new RuntimeException("No pending cash payment found for Order ID: " + orderId);
        }

        targetPayment.setConfirmed(true);
        targetPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        cashPaymentRepository.save(targetPayment);

        // Una vez confirmado el pago en caja, pasa a cocina
        order.setStatus(OrderStatus.IN_KITCHEN);
        orderRepository.save(order);

        return mapToResponse(targetPayment);
    }

    private PaymentResponse mapToResponse(com.ironhack.smartqr.entity.Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getPaymentMethod(),
                payment.getCreatedAt()
        );
    }
}