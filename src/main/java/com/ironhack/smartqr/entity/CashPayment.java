package com.ironhack.smartqr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cash_payments")
@PrimaryKeyJoinColumn(name = "payment_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashPayment extends Payment {

    @Column(precision = 10, scale = 2)
    private BigDecimal cashReceived;

    @Column(precision = 10, scale = 2)
    private BigDecimal changeAmount;

    private Boolean confirmed;
}