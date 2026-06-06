package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.product.ProductRequest;
import com.ironhack.smartqr.dto.product.ProductResponse;
import com.ironhack.smartqr.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/menu")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getMenu() {
        return productService.getAvailableMenu();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/out-of-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getOutStockProducts() {
        return productService.getOutOfStockProducts();
    }
}