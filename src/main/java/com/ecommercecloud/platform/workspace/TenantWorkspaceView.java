package com.ecommercecloud.platform.workspace;

import com.ecommercecloud.platform.vertical.VerticalDescriptor;

import java.time.LocalDate;

public record TenantWorkspaceView(
        Long tenantId,
        String tenantCode,
        String businessName,
        String ownerName,
        String planName,
        LocalDate expirationDate,
        String timezone,
        String locale,
        String currency,
        String primaryColor,
        VerticalDescriptor vertical
) {
}
