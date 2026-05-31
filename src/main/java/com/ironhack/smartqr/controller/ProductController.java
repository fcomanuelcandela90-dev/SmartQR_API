package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.product.ProductRequest;
import com.ironhack.smartqr.dto.product.ProductResponse;
import com.ironhack.smartqr.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // [GET] CUSTOMER - Scan QR & View Menu
    @GetMapping("/menu")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getMenu() {
        return productService.getAvailableMenu();
    }

    // [GET] ADMIN - View all products
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    // [POST] ADMIN - Save Product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    // [PUT] ADMIN - Update Product
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    // [DELETE] ADMIN - Delete Product
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // [GET] ADMIN/EMPLOYEE - View Out of Stock
    @GetMapping("/out-of-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getOutStockProducts() {
        return productService.getOutOfStockProducts();
    }
}