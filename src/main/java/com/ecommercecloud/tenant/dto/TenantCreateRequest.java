package com.ecommercecloud.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TenantCreateRequest(

        @NotBlank
        @Size(max = 150)
        String nombreComercial,

        @Size(max = 200)
        String razonSocial,

        @NotBlank
        @Size(max = 150)
        String propietario,

        @NotBlank
        @Email
        @Size(max = 150)
        String correo,

        @Size(max = 30)
        String telefono,

        @Size(max = 100)
        String ciudad,

        @NotNull
        Long verticalId,

        @NotNull
        Long planId
) {
}
