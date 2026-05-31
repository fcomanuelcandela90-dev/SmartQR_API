package com.ironhack.smartqr.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.smartqr.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private Boolean available;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String imageUrl;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();
}