package com.ironhack.smartqr.dto.product;

import com.ironhack.smartqr.enums.ProductCategory;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price, Boolean available,
                              ProductCategory category, String imageUrl){
}
