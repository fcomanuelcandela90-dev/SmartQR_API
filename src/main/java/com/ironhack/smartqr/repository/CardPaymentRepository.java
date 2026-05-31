package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.CardPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPaymentRepository extends JpaRepository<CardPayment, Long> {
}
