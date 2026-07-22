package com.ecommercecloud.tenant.dto;

import com.ecommercecloud.tenant.entity.EstadoTenant;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record TenantResponse(

        Long id,
        String codigo,
        String nombreComercial,
        String razonSocial,
        String propietario,
        String correo,
        String telefono,
        String ciudad,
        EstadoTenant estado,
        Long verticalId,
        Long planId,
        LocalDate fechaActivacion,
        LocalDate fechaVencimiento,
        Boolean activo,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn
) {
}
