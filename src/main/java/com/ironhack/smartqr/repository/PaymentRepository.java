package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.Payment;
import com.ironhack.smartqr.enums.PaymentMethod;
import com.ironhack.smartqr.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @Query(value = "SELECT SUM(amount) FROM payments WHERE payment_status = 'COMPLETED'", nativeQuery = true)
    BigDecimal calculateTotalCompletedIncome();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = :status AND p.paymentMethod = :method")
    BigDecimal calculateCompletedIncomeByMethod(
            @Param("status") PaymentStatus status,
            @Param("method") PaymentMethod method
    );


}

