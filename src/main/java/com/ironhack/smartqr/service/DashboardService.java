package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.dashboard.DashboardResponse;
import com.ironhack.smartqr.dto.dashboard.ProductSalesResponse;
import com.ironhack.smartqr.entity.OrderItem;
import com.ironhack.smartqr.enums.OrderStatus;
import com.ironhack.smartqr.enums.PaymentMethod;
import com.ironhack.smartqr.enums.PaymentStatus;
import com.ironhack.smartqr.repository.OrderItemRepository;
import com.ironhack.smartqr.repository.OrderRepository;
import com.ironhack.smartqr.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public DashboardResponse getDashboardMetrics() {
        BigDecimal totalIncome = paymentRepository.calculateTotalCompletedIncome();

        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }

        BigDecimal cardIncome = paymentRepository.calculateCompletedIncomeByMethod(
                PaymentStatus.COMPLETED,
                PaymentMethod.CARD
        );

        if (cardIncome == null) {
            cardIncome = BigDecimal.ZERO;
        }

        BigDecimal cashIncome = paymentRepository.calculateCompletedIncomeByMethod(
                PaymentStatus.COMPLETED,
                PaymentMethod.CASH
        );

        if (cashIncome == null) {
            cashIncome = BigDecimal.ZERO;
        }

        Long totalOrders = orderRepository.count();
        Long ordersInKitchen = orderRepository.countByStatus(OrderStatus.IN_KITCHEN);
        Long readyOrders = orderRepository.countByStatus(OrderStatus.READY);

        List<ProductSalesResponse> productSales = calculateProductSales();

        Integer totalProductsSold = calculateTotalProductsSold(productSales);
        String bestSellingProduct = findBestSellingProduct(productSales);

        return new DashboardResponse(
                totalIncome,
                cardIncome,
                cashIncome,
                totalOrders,
                ordersInKitchen,
                readyOrders,
                totalProductsSold,
                bestSellingProduct,
                productSales
        );
    }

    private List<ProductSalesResponse> calculateProductSales() {
        List<OrderItem> allOrderItems = orderItemRepository.findAll();
        List<ProductSalesResponse> productSales = new ArrayList<>();

        for (OrderItem item : allOrderItems) {
            if (item.getOrder().getPayment() != null
                    && item.getOrder().getPayment().getPaymentStatus() == PaymentStatus.COMPLETED) {

                String productName = item.getProduct().getName();
                Integer quantity = item.getQuantity();

                int existingProductIndex = findProductIndex(productSales, productName);

                if (existingProductIndex == -1) {
                    ProductSalesResponse newProductSale = new ProductSalesResponse(
                            productName,
                            quantity
                    );

                    productSales.add(newProductSale);
                } else {
                    ProductSalesResponse existingProductSale = productSales.get(existingProductIndex);

                    ProductSalesResponse updatedProductSale = new ProductSalesResponse(
                            existingProductSale.productName(),
                            existingProductSale.quantitySold() + quantity
                    );

                    productSales.set(existingProductIndex, updatedProductSale);
                }
            }
        }

        return productSales;
    }

    private int findProductIndex(List<ProductSalesResponse> productSales, String productName) {
        for (int i = 0; i < productSales.size(); i++) {
            ProductSalesResponse productSale = productSales.get(i);

            if (productSale.productName().equals(productName)) {
                return i;
            }
        }

        return -1;
    }

    private Integer calculateTotalProductsSold(List<ProductSalesResponse> productSales) {
        Integer totalProductsSold = 0;

        for (ProductSalesResponse productSale : productSales) {
            totalProductsSold = totalProductsSold + productSale.quantitySold();
        }

        return totalProductsSold;
    }

    private String findBestSellingProduct(List<ProductSalesResponse> productSales) {
        if (productSales.isEmpty()) {
            return "No sales registered";
        }

        ProductSalesResponse bestSellingProduct = productSales.get(0);

        for (ProductSalesResponse productSale : productSales) {
            if (productSale.quantitySold() > bestSellingProduct.quantitySold()) {
                bestSellingProduct = productSale;
            }
        }

        return bestSellingProduct.productName();
    }

}

