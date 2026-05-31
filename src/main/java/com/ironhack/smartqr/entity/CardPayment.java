package com.ironhack.smartqr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_payments")
@PrimaryKeyJoinColumn(name = "payment_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPayment extends Payment {

    private String cardHolderName;

    private String last4Digits;

    private String transactionId;
}