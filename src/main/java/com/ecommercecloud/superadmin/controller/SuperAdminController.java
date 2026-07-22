package com.ecommercecloud.superadmin.controller;

import com.ecommercecloud.superadmin.service.SuperAdminDashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuperAdminController {

    private final SuperAdminDashboardService dashboardService;

    public SuperAdminController(
            SuperAdminDashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({
            "/super-admin",
            "/super-admin/dashboard"
    })
    public String dashboard(Model model) {

        model.addAttribute(
                "dashboard",
                dashboardService.obtenerDashboard()
        );

        model.addAttribute(
                "paginaActual",
                "dashboard"
        );

        return "superadmin/dashboard";
    }
}
