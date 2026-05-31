package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
