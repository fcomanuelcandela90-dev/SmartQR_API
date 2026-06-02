package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/dashboard/charts")
@RequiredArgsConstructor
public class ChartController {


    private final ChartService chartService;

    @GetMapping(value = "/income-by-payment-method", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public byte[] generateIncomeByPaymentMethodChart() throws IOException {
        return chartService.generateIncomeByPaymentMethodChart();
    }

    @GetMapping(value = "/product-sales", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public byte[] generateProductSalesChart() throws IOException {
        return chartService.generateProductSalesChart();
    }

}
