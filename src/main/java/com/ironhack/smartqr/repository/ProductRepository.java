package com.ironhack.smartqr.repository;

import com.ironhack.smartqr.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
