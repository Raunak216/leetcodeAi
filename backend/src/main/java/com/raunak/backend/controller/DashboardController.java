package com.raunak.backend.controller;

import com.raunak.backend.dto.DashboardResponse;
import com.raunak.backend.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService =
                dashboardService;
    }

    @GetMapping("/{userId}")
    public DashboardResponse getDashboard(
            @PathVariable
            int userId
    ) {
        return dashboardService
                .getDashboard(
                        userId
                );
    }
}