package com.ecommercecloud.superadmin.dto;

import java.util.List;

public record SuperAdminDashboardView(

        long totalEmpresas,
        long totalProspectos,
        long totalActivas,
        long totalSuspendidas,
        List<TenantResumenView> empresasRecientes

) {
}
