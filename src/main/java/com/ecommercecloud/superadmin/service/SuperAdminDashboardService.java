package com.ecommercecloud.superadmin.service;

import com.ecommercecloud.superadmin.dto.SuperAdminDashboardView;
import com.ecommercecloud.superadmin.dto.TenantResumenView;
import com.ecommercecloud.tenant.entity.EstadoTenant;
import com.ecommercecloud.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SuperAdminDashboardService {

    private final TenantRepository tenantRepository;

    public SuperAdminDashboardService(
            TenantRepository tenantRepository
    ) {
        this.tenantRepository = tenantRepository;
    }

    public SuperAdminDashboardView obtenerDashboard() {

        long totalEmpresas = tenantRepository.count();

        long totalProspectos =
                tenantRepository.countByEstado(
                        EstadoTenant.PROSPECTO
                );

        long totalActivas =
                tenantRepository.countByEstado(
                        EstadoTenant.ACTIVO
                );

        long totalSuspendidas =
                tenantRepository.countByEstado(
                        EstadoTenant.SUSPENDIDO
                );

        List<TenantResumenView> empresasRecientes =
                tenantRepository
                        .findTop5ByOrderByCreadoEnDesc()
                        .stream()
                        .map(tenant -> new TenantResumenView(
                                tenant.getId(),
                                tenant.getCodigo(),
                                tenant.getNombreComercial(),
                                tenant.getPropietario(),
                                tenant.getCorreo(),
                                tenant.getEstado(),
                                tenant.getActivo(),
                                tenant.getCreadoEn()
                        ))
                        .toList();

        return new SuperAdminDashboardView(
                totalEmpresas,
                totalProspectos,
                totalActivas,
                totalSuspendidas,
                empresasRecientes
        );
    }
}
