package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.CashPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashPaymentRepository extends JpaRepository<CashPayment, Long> {
}
