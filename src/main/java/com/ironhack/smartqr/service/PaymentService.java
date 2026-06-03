package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.payment.CardPaymentRequest;
import com.ironhack.smartqr.dto.payment.CashPaymentRequest;
import com.ironhack.smartqr.dto.payment.PaymentResponse;
import com.ironhack.smartqr.entity.CardPayment;
import com.ironhack.smartqr.entity.CashPayment;
import com.ironhack.smartqr.entity.Order;
import com.ironhack.smartqr.entity.OrderItem;
import com.ironhack.smartqr.entity.Payment;
import com.ironhack.smartqr.enums.OrderStatus;
import com.ironhack.smartqr.enums.PaymentMethod;
import com.ironhack.smartqr.enums.PaymentStatus;
import com.ironhack.smartqr.repository.CashPaymentRepository;
import com.ironhack.smartqr.repository.OrderRepository;
import com.ironhack.smartqr.repository.PaymentRepository;
import com.ironhack.smartqr.exception.BusinessRuleException;
import com.ironhack.smartqr.exception.ConflictException;
import com.ironhack.smartqr.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
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
    public PaymentResponse processCardPayment(
            String authenticatedEmail,
            CardPaymentRequest request
    ) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order ID not found: " + request.orderId()));

        validateCustomerOwnsOrder(order, authenticatedEmail);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ConflictException(
                    "Cannot process payment: This order is already paid or unavailable.");
        }

        boolean gatewayApproval = true;

        if (!gatewayApproval) {
            throw new BusinessRuleException
                    ("Card payment was rejected by the payment gateway");
        }

        CardPayment cardPayment = new CardPayment();
        cardPayment.setOrder(order);
        cardPayment.setAmount(order.getTotalPrice());
        cardPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        cardPayment.setPaymentMethod(PaymentMethod.CARD);
        cardPayment.setCreatedAt(LocalDateTime.now());
        cardPayment.setCardHolderName(request.cardHolderName());
        cardPayment.setLast4Digits(request.last4Digits());
        cardPayment.setTransactionId(request.transactionId());

        CardPayment savedPayment = paymentRepository.save(cardPayment);

        order.setStatus(OrderStatus.IN_KITCHEN);
        orderRepository.save(order);

        return mapToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse requestCashPayment(
            String authenticatedEmail,
            CashPaymentRequest request
    ) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order not found with ID: " + request.orderId()));

        validateCustomerOwnsOrder(order, authenticatedEmail);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ConflictException(
                    "Cannot request cash checkout: This order is already paid or unavailable.");
        }

        if (request.cashReceived() == null
                || request.cashReceived().compareTo(order.getTotalPrice()) < 0) {
            throw new BusinessRuleException(
                    "Cannot request cash checkout: cash received must be equal to or greater than the order total.");
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

        return mapToResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse confirmCashPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order not found with ID: " + orderId));

        List<CashPayment> allCashPayments = cashPaymentRepository.findAll();
        CashPayment targetPayment = null;

        for (CashPayment payment : allCashPayments) {
            if (payment.getOrder().getId().equals(orderId)
                    && !payment.getConfirmed()) {
                targetPayment = payment;
                break;
            }
        }

        if (targetPayment == null) {
            throw new ResourceNotFoundException("No pending cash payment found for order ID: " + orderId);
        }

        targetPayment.setConfirmed(true);
        targetPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        cashPaymentRepository.save(targetPayment);

        order.setStatus(OrderStatus.IN_KITCHEN);
        orderRepository.save(order);

        return mapToResponse(targetPayment);
    }

    @Transactional
    public String generatePrintableTicket(
            String authenticatedEmail,
            boolean internalStaffAccess,
            Long orderId
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order ID not found: " + orderId));

        if (!internalStaffAccess) {
            validateCustomerOwnsOrder(order, authenticatedEmail);
        }

        Payment payment = order.getPayment();

        if (payment == null) {
            throw new BusinessRuleException(
                    "Cannot generate ticket: this order has no associated payment."
            );
        }

        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new BusinessRuleException(
                    "Cannot generate ticket: payment has not been completed."
            );
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
            ticket.append("Card: **** ")
                    .append(cardPayment.getLast4Digits())
                    .append("\n");
            ticket.append("Transaction ID: ")
                    .append(cardPayment.getTransactionId())
                    .append("\n");
        }

        if (payment instanceof CashPayment cashPayment) {
            ticket.append("Cash received: ")
                    .append(cashPayment.getCashReceived())
                    .append(" EUR\n");
            ticket.append("Change: ")
                    .append(cashPayment.getChangeAmount())
                    .append(" EUR\n");
        }

        ticket.append("========================================\n");
        ticket.append("        Thank you for your order        \n");
        ticket.append("========================================\n");

        return ticket.toString();
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getPaymentMethod(),
                payment.getCreatedAt()
        );
    }

    private void validateCustomerOwnsOrder(Order order, String authenticatedEmail) {
        if (order.getUser() == null
                || !order.getUser().getEmail().equalsIgnoreCase(authenticatedEmail)) {
            throw new AccessDeniedException(
                    "Cannot access order: this order does not belong to the authenticated customer."
            );
        }
    }

}
