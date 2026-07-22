package com.ecommercecloud.superadmin.dto;

import com.ecommercecloud.tenant.entity.EstadoTenant;

import java.time.OffsetDateTime;

public record TenantResumenView(

        Long id,
        String codigo,
        String nombreComercial,
        String propietario,
        String correo,
        EstadoTenant estado,
        Boolean activo,
        OffsetDateTime creadoEn

) {
}
