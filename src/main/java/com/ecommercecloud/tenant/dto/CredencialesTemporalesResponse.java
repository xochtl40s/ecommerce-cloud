package com.ecommercecloud.tenant.dto;

public record CredencialesTemporalesResponse(

        String username,
        String passwordTemporal,
        Boolean cambioPasswordRequerido

) {
}
