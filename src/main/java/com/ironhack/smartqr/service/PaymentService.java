package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.payment.CardPaymentRequest;
import com.ironhack.smartqr.dto.payment.CashPaymentRequest;
import com.ironhack.smartqr.dto.payment.PaymentResponse;
import com.ironhack.smartqr.entity.CardPayment;
import com.ironhack.smartqr.entity.CashPayment;
import com.ironhack.smartqr.entity.Order;
import com.ironhack.smartqr.entity.OrderItem;
import com.ironhack.smartqr.enums.OrderStatus;
import com.ironhack.smartqr.entity.Payment;
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

        if (request.cashReceived() == null || request.cashReceived().compareTo(order.getTotalPrice()) < 0) {
            throw new IllegalArgumentException(
                    "Cannot request cash checkout: cash received must be equal to or greater than the order total."
            );
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

    @Transactional
    public String generatePrintableTicket(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order ID not found: " + orderId));

        Payment payment = order.getPayment();

        if (payment == null) {
            throw new IllegalStateException("Cannot generate ticket: this order has no associated payment.");
        }

        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot generate ticket: payment has not been completed.");
        }

        StringBuilder ticket = new StringBuilder();

        ticket.append("========================================\n");
        ticket.append("              SMARTQR TICKET            \n");
        ticket.append("========================================\n");
        ticket.append("Order ID: ").append(order.getId()).append("\n");
        ticket.append("Table: ").append(order.getTableNumber()).append("\n");
        ticket.append("Created at: ").append(order.getCreatedAt()).append("\n");
        ticket.append("Order status: ").append(order.getStatus()).append("\n");
        ticket.append("----------------------------------------\n");
        ticket.append("ITEMS\n");
        ticket.append("----------------------------------------\n");

        for (OrderItem item : order.getOrderItems()) {
            ticket.append("- ")
                    .append(item.getProduct().getName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" ........ ")
                    .append(item.getSubTotal())
                    .append(" EUR\n");

            if (item.getNotes() != null && !item.getNotes().isBlank()) {
                ticket.append("  Notes: ")
                        .append(item.getNotes())
                        .append("\n");
            }
        }

        ticket.append("----------------------------------------\n");
        ticket.append("TOTAL: ").append(order.getTotalPrice()).append(" EUR\n");
        ticket.append("----------------------------------------\n");
        ticket.append("Payment method: ").append(payment.getPaymentMethod()).append("\n");
        ticket.append("Payment status: ").append(payment.getPaymentStatus()).append("\n");

        if (payment instanceof CardPayment cardPayment) {
            ticket.append("Card: **** ").append(cardPayment.getLast4Digits()).append("\n");
            ticket.append("Transaction ID: ").append(cardPayment.getTransactionId()).append("\n");
        }

        if (payment instanceof CashPayment cashPayment) {
            ticket.append("Cash received: ").append(cashPayment.getCashReceived()).append(" EUR\n");
            ticket.append("Change: ").append(cashPayment.getChangeAmount()).append(" EUR\n");
        }

        ticket.append("========================================\n");
        ticket.append("        Thank you for your order        \n");
        ticket.append("========================================\n");

        return ticket.toString();
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