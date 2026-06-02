package com.ironhack.smartqr.controller;

import com.ironhack.smartqr.dto.dashboard.DashboardResponse;
import com.ironhack.smartqr.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    @ResponseStatus(HttpStatus.OK)
    public DashboardResponse getDashboardMetrics() {
        return dashboardService.getDashboardMetrics();
    }
}
