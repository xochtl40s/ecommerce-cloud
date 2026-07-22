package com.ecommercecloud.superadmin.service;

import com.ecommercecloud.superadmin.dto.TenantCreateForm;
import com.ecommercecloud.tenant.dto.TenantActivationResponse;
import com.ecommercecloud.tenant.dto.TenantCreateRequest;
import com.ecommercecloud.tenant.dto.TenantResponse;
import com.ecommercecloud.tenant.entity.EstadoTenant;
import com.ecommercecloud.tenant.entity.Tenant;
import com.ecommercecloud.tenant.repository.TenantRepository;
import com.ecommercecloud.tenant.service.TenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SuperAdminEmpresaService {

    private final TenantRepository tenantRepository;
    private final TenantService tenantService;

    public SuperAdminEmpresaService(
            TenantRepository tenantRepository,
            TenantService tenantService
    ) {
        this.tenantRepository = tenantRepository;
        this.tenantService = tenantService;
    }

    @Transactional(readOnly = true)
    public List<TenantResponse> buscar(
            String texto,
            EstadoTenant estado
    ) {

        String textoNormalizado =
                texto == null || texto.isBlank()
                        ? null
                        : texto.trim();

        List<Tenant> tenants;

        if (textoNormalizado == null && estado == null) {

            tenants =
                    tenantRepository
                            .findAllByOrderByCreadoEnDesc();

        } else if (textoNormalizado == null) {

            tenants =
                    tenantRepository
                            .findByEstadoOrderByCreadoEnDesc(
                                    estado
                            );

        } else if (estado == null) {

            tenants =
                    tenantRepository
                            .findByNombreComercialContainingIgnoreCaseOrCodigoContainingIgnoreCaseOrCorreoContainingIgnoreCaseOrPropietarioContainingIgnoreCaseOrderByCreadoEnDesc(
                                    textoNormalizado,
                                    textoNormalizado,
                                    textoNormalizado,
                                    textoNormalizado
                            );

        } else {

            tenants =
                    tenantRepository
                            .findByEstadoAndNombreComercialContainingIgnoreCaseOrEstadoAndCodigoContainingIgnoreCaseOrEstadoAndCorreoContainingIgnoreCaseOrEstadoAndPropietarioContainingIgnoreCaseOrderByCreadoEnDesc(
                                    estado,
                                    textoNormalizado,
                                    estado,
                                    textoNormalizado,
                                    estado,
                                    textoNormalizado,
                                    estado,
                                    textoNormalizado
                            );
        }

        return tenants
                .stream()
                .map(this::mapear)
                .toList();
    }

    @Transactional(readOnly = true)
    public TenantResponse obtener(Long id) {
        return tenantService.obtenerPorId(id);
    }

    public TenantResponse registrar(
            TenantCreateForm form
    ) {
        TenantCreateRequest request =
                new TenantCreateRequest(
                        form.getNombreComercial(),
                        form.getRazonSocial(),
                        form.getPropietario(),
                        form.getCorreo(),
                        form.getTelefono(),
                        form.getCiudad(),
                        form.getVerticalId(),
                        form.getPlanId()
                );

        return tenantService.crearProspecto(request);
    }

    public TenantActivationResponse activar(Long id) {
        return tenantService.activar(id);
    }

    private TenantResponse mapear(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getCodigo(),
                tenant.getNombreComercial(),
                tenant.getRazonSocial(),
                tenant.getPropietario(),
                tenant.getCorreo(),
                tenant.getTelefono(),
                tenant.getCiudad(),
                tenant.getEstado(),
                tenant.getVerticalId(),
                tenant.getPlanId(),
                tenant.getFechaActivacion(),
                tenant.getFechaVencimiento(),
                tenant.getActivo(),
                tenant.getCreadoEn(),
                tenant.getActualizadoEn()
        );
    }
}
