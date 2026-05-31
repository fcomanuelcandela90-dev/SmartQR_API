package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
