package com.ecommercecloud.tenant.dto;

public record TenantActivationResponse(

        TenantResponse tenant,
        CredencialesTemporalesResponse credencialesAdministrador

) {
}
