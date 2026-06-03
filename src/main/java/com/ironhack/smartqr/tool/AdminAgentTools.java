package com.ironhack.smartqr.tool;

import com.ironhack.smartqr.dto.dashboard.DashboardResponse;
import com.ironhack.smartqr.dto.order.OrderResponse;
import com.ironhack.smartqr.dto.product.ProductResponse;
import com.ironhack.smartqr.service.DashboardService;
import com.ironhack.smartqr.service.OrderService;
import com.ironhack.smartqr.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminAgentTools {
    
    private final DashboardService dashboardService;
    private final OrderService orderService;
    private final ProductService productService;

    @Tool(
            name = "get_sales_metrics",
            description = "Returns the current SmartQR sales dashboard metrics, including completed income, income by payment method, order counts and best selling product. This is a read-only operation."
    )
    @McpTool(
            name = "get_sales_metrics",
            description = "Returns the current SmartQR sales dashboard metrics, including completed income, income by payment method, order counts and best selling product. This is a read-only operation.",
            annotations = @McpTool.McpAnnotations(
                    title = "SmartQR Sales Metrics",
                    readOnlyHint = true,
                    destructiveHint = false,
                    idempotentHint = true,
                    openWorldHint = false
            )
    )
    public DashboardResponse getSalesMetrics() {
        return dashboardService.getDashboardMetrics();
    }

    @Tool(
            name = "get_kitchen_queue",
            description = "Returns the current active kitchen queue with pending and in-kitchen orders. Use this tool when the administrator asks about current kitchen workload. This is a read-only operation."
    )
    @McpTool(
            name = "get_kitchen_queue",
            description = "Returns the current active kitchen queue with pending and in-kitchen orders. This is a read-only operation.",
            annotations = @McpTool.McpAnnotations(
                    title = "SmartQR Kitchen Queue",
                    readOnlyHint = true,
                    destructiveHint = false,
                    idempotentHint = true,
                    openWorldHint = false
            )
    )
    public List<OrderResponse> getKitchenQueue() {
        return orderService.getKitchenQueue();
    }

    @Tool(
            name = "get_available_menu",
            description = "Returns the products currently available in the SmartQR menu, including names, categories and prices. Use this tool when the administrator asks about available products. This is a read-only operation."
    )
    @McpTool(
            name = "get_available_menu",
            description = "Returns the products currently available in the SmartQR menu, including names, categories and prices. This is a read-only operation.",
            annotations = @McpTool.McpAnnotations(
                    title = "SmartQR Available Menu",
                    readOnlyHint = true,
                    destructiveHint = false,
                    idempotentHint = true,
                    openWorldHint = false
            )
    )
    public List<ProductResponse> getAvailableMenu() {
        return productService.getAvailableMenu();
    }

}
