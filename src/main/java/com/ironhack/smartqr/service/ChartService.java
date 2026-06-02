package com.ironhack.smartqr.service;

import com.ironhack.smartqr.dto.dashboard.DashboardResponse;
import com.ironhack.smartqr.dto.dashboard.ProductSalesResponse;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;

@Service
@RequiredArgsConstructor
public class ChartService {

    private static final Path CHART_DIRECTORY = Paths.get("requests", "charts");

    private final DashboardService dashboardService;

    public byte[] generateIncomeByPaymentMethodChart() throws IOException {
        DashboardResponse dashboard = dashboardService.getDashboardMetrics();

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        dataset.setValue("Tarjeta", dashboard.cardIncome());
        dataset.setValue("Efectivo", dashboard.cashIncome());

        JFreeChart chart = ChartFactory.createPieChart(
                "Ingresos por metodo de pago",
                dataset,
                true,
                true,
                false
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChartUtils.writeChartAsPNG(
                outputStream,
                chart,
                800,
                500
        );

        byte[] chartImage = outputStream.toByteArray();

        Files.createDirectories(CHART_DIRECTORY);

        Path outputFile = CHART_DIRECTORY.resolve("income-by-payment-method.png");

        Files.write(
                outputFile,
                chartImage,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );

        return chartImage;
    }

    public byte[] generateProductSalesChart() throws IOException {
        DashboardResponse dashboard = dashboardService.getDashboardMetrics();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (ProductSalesResponse productSale : dashboard.productSales()) {
            dataset.addValue(
                    productSale.quantitySold(),
                    "Unidades vendidas",
                    productSale.productName()
            );
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Productos vendidos",
                "Producto",
                "Unidades",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis unitsAxis = (NumberAxis) plot.getRangeAxis();

        unitsAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        unitsAxis.setNumberFormatOverride(new DecimalFormat("0"));
        unitsAxis.setAutoRangeIncludesZero(true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ChartUtils.writeChartAsPNG(
                outputStream,
                chart,
                900,
                500
        );

        byte[] chartImage = outputStream.toByteArray();

        Files.createDirectories(CHART_DIRECTORY);

        Path outputFile = CHART_DIRECTORY.resolve("product-sales.png");

        Files.write(
                outputFile,
                chartImage,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );

        return chartImage;
    }
}
