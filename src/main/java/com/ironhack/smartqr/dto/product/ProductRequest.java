package com.ironhack.smartqr.dto.product;

import com.ironhack.smartqr.enums.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(max = 100, message = "Product name cannot exceed 100 characters")
        String name,

        @Size(max = 500, message = "Product description cannot exceed 500 characters")
        String description,

        @NotNull(message = "Product price is required")
        @Positive(message = "Product price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Product category is required")
        ProductCategory category,

        @Size(max = 500, message = "Image URL cannot exceed 500 characters")
        String imageUrl

) {
}
