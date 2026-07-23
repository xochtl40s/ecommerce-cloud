package com.ecommercecloud.multitenancy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TenantScopedEntity {

    @Column(
            name = "tenant_id",
            nullable = false,
            updatable = false
    )
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void assignTenant(Long tenantId) {

        if (tenantId == null) {
            throw new IllegalArgumentException(
                    "tenantId es obligatorio"
            );
        }

        if (this.tenantId != null
                && !this.tenantId.equals(tenantId)) {

            throw new IllegalStateException(
                    "No se puede mover un registro entre tenants"
            );
        }

        this.tenantId = tenantId;
    }
}
