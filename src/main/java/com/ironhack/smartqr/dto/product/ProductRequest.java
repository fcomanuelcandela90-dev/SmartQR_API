package com.ironhack.smartqr.dto.product;

import com.ironhack.smartqr.enums.ProductCategory;

import java.math.BigDecimal;

public record ProductRequest(String name, String description, BigDecimal price, ProductCategory category, String imageUrl) {
}
