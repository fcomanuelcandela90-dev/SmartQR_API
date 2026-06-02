package com.ironhack.smartqr.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(BigDecimal totalIncome, BigDecimal cardIncome, BigDecimal cashIncome,
        Long totalOrders, Long ordersInKitchen, Long readyOrders, Integer totalProductsSold,
        String bestSellingProduct, List<ProductSalesResponse> productSales
) {
}
