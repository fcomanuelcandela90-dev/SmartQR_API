package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
