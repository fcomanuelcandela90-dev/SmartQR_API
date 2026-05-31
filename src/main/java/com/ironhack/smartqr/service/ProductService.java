package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.product.ProductRequest;
import com.ironhack.smartqr.dto.product.ProductResponse;
import com.ironhack.smartqr.entity.Product;
import com.ironhack.smartqr.enums.ProductCategory;
import com.ironhack.smartqr.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAvailableMenu() {
        List<Product> availableProducts = productRepository.findByAvailableTrue();
        List<ProductResponse> responseList = new ArrayList<>();

        for (Product product : availableProducts) {
            responseList.add(mapToResponse(product));
        }

        return responseList;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        List<ProductResponse> responseList = new ArrayList<>();

        for (Product product : allProducts) {
            responseList.add(mapToResponse(product));
        }

        return responseList;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());
        product.setAvailable(true);

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
        product.setImageUrl(request.imageUrl());

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        product.setAvailable(false);
        productRepository.save(product);
    }

    public List<ProductResponse> getOutOfStockProducts() {
        List<Product> allProducts = productRepository.findAll();
        List<ProductResponse> outOfStockList = new ArrayList<>();

        for (Product product : allProducts) {
            if (!product.getAvailable()) { // Si NO está disponible
                outOfStockList.add(mapToResponse(product));
            }
        }

        return outOfStockList;
    }

    // Este es nuestro método "helper" o auxiliar para no repetir código
    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailable(),
                product.getCategory(),
                product.getImageUrl()
        );
    }
}
