package com.ecommercecloud.multitenancy;

public final class TenantContext {

    private static final ThreadLocal<Long>
            CURRENT_TENANT_ID =
            new ThreadLocal<>();

    private static final ThreadLocal<String>
            CURRENT_TENANT_CODE =
            new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(
            Long tenantId,
            String tenantCode
    ) {
        if (tenantId == null) {
            throw new IllegalArgumentException(
                    "tenantId es obligatorio"
            );
        }

        if (tenantCode == null
                || tenantCode.isBlank()) {

            throw new IllegalArgumentException(
                    "tenantCode es obligatorio"
            );
        }

        CURRENT_TENANT_ID.set(tenantId);
        CURRENT_TENANT_CODE.set(tenantCode);
    }

    public static Long getTenantId() {

        Long tenantId =
                CURRENT_TENANT_ID.get();

        if (tenantId == null) {
            throw new IllegalStateException(
                    "No existe un tenant activo " +
                    "en la petición"
            );
        }

        return tenantId;
    }

    public static Long getTenantIdOrNull() {
        return CURRENT_TENANT_ID.get();
    }

    public static String getTenantCode() {

        String tenantCode =
                CURRENT_TENANT_CODE.get();

        if (tenantCode == null) {
            throw new IllegalStateException(
                    "No existe un código tenant " +
                    "activo en la petición"
            );
        }

        return tenantCode;
    }

    public static boolean isPresent() {
        return CURRENT_TENANT_ID.get() != null;
    }

    public static void clear() {
        CURRENT_TENANT_ID.remove();
        CURRENT_TENANT_CODE.remove();
    }
}
