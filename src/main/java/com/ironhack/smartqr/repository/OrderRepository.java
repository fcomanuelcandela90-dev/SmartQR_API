package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.Order;
import com.ironhack.smartqr.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByStatus(OrderStatus status);
    List<Order> findByTableNumber(Integer tableNumber);
    List<Order> findByStatusIn(List<OrderStatus> statuses);
    List<Order> findByStatus(OrderStatus status);
}
