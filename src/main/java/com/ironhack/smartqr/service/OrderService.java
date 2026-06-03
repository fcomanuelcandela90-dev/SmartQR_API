package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.order.CreateOrderRequest;
import com.ironhack.smartqr.dto.order.OrderItemRequest;
import com.ironhack.smartqr.dto.order.OrderItemResponse;
import com.ironhack.smartqr.dto.order.OrderResponse;
import com.ironhack.smartqr.entity.Order;
import com.ironhack.smartqr.entity.OrderItem;
import com.ironhack.smartqr.entity.Product;
import com.ironhack.smartqr.entity.User;
import com.ironhack.smartqr.enums.OrderStatus;
import com.ironhack.smartqr.repository.OrderRepository;
import com.ironhack.smartqr.repository.ProductRepository;
import com.ironhack.smartqr.repository.UserRepository;
import com.ironhack.smartqr.exception.BusinessRuleException;
import com.ironhack.smartqr.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(String userEmail, CreateOrderRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + userEmail));

        Order order = new Order();
        order.setTableNumber(request.tableNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUser(user);

        long pendingOrdersCount = orderRepository.countByStatus(OrderStatus.PENDING);
        order.setEstimatedTime(5 + (int) (pendingOrdersCount * 3));

        BigDecimal totalAcumulator = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException
                            ("Product not found with ID: " + itemRequest.productId()));

            BigDecimal subTotalLine = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.quantity()));

            totalAcumulator = totalAcumulator.add(subTotalLine);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setSubTotal(subTotalLine);
            orderItem.setNotes(itemRequest.notes());

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalAcumulator);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersByTable(Integer tableNumber) {
        List<Order> orders = orderRepository.findByTableNumber(tableNumber);
        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : orders) {
            responseList.add(mapToOrderResponse(order));
        }

        return responseList;
    }

    public OrderResponse getOrderById(
            String authenticatedEmail,
            boolean internalStaffAccess,
            Long orderId
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order not found with ID: " + orderId));

        if (!internalStaffAccess) {
            validateCustomerOwnsOrder(order, authenticatedEmail);
        }

        return mapToOrderResponse(order);
    }

    public List<OrderResponse> getKitchenQueue() {
        List<OrderStatus> activeStatuses = new ArrayList<>();
        activeStatuses.add(OrderStatus.PENDING);
        activeStatuses.add(OrderStatus.IN_KITCHEN);

        List<Order> activeOrders = orderRepository.findByStatusIn(activeStatuses);
        List<OrderResponse> responseList = new ArrayList<>();

        for (Order order : activeOrders) {
            responseList.add(mapToOrderResponse(order));
        }

        return responseList;
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order not found with ID: " + orderId));

        order.setStatus(newStatus);

        return mapToOrderResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order not found with ID: " + orderId));

        if (order.getStatus() == OrderStatus.IN_KITCHEN
                || order.getStatus() == OrderStatus.READY
                || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessRuleException(
                    "Cannot cancel an order that is already in preparation, ready or delivered."
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        return mapToOrderResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateOrderItems(
            Long orderId,
            List<OrderItemRequest> newItemsRequest
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException(
                    "Order items can only be modified while the order is in PENDING status."
            );
        }

        order.getOrderItems().clear();

        BigDecimal totalAcumulator = BigDecimal.ZERO;
        List<OrderItem> updatedItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : newItemsRequest) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException
                            ("Product not found with ID: " + itemRequest.productId()));

            BigDecimal subTotalLine = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.quantity()));

            totalAcumulator = totalAcumulator.add(subTotalLine);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setSubTotal(subTotalLine);
            orderItem.setNotes(itemRequest.notes());

            updatedItems.add(orderItem);
        }

        order.getOrderItems().addAll(updatedItems);
        order.setTotalPrice(totalAcumulator);

        Order savedOrder = orderRepository.save(order);

        return mapToOrderResponse(savedOrder);
    }

    private void validateCustomerOwnsOrder(Order order, String authenticatedEmail) {
        if (order.getUser() == null
                || !order.getUser().getEmail().equalsIgnoreCase(authenticatedEmail)) {
            throw new AccessDeniedException(
                    "Cannot access order: this order does not belong to the authenticated customer."
            );
        }
    }

    public OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {
            itemResponses.add(mapToItemResponse(item));
        }

        return new OrderResponse(
                order.getId(),
                order.getTableNumber(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getEstimatedTime(),
                order.getCreatedAt(),
                itemResponses
        );
    }

    private OrderItemResponse mapToItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getSubTotal(),
                item.getNotes()
        );
    }
}
