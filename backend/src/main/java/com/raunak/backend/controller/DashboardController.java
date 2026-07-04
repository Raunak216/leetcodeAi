package com.raunak.backend.controller;

import com.raunak.backend.dto.DashboardResponse;
import com.raunak.backend.security.AuthUser;
import com.raunak.backend.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse getDashboard(
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser) authentication.getPrincipal();

        return dashboardService.getDashboard(
                authUser.getUserId()
        );
    }
}