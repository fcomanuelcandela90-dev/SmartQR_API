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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + userEmail));

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
                    .orElseThrow(() -> new RuntimeException("Product ID not found: " + itemRequest.productId()));

            BigDecimal subTotalLine = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
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