package com.ecommercecloud.platform.workspace;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.platform.vertical.VerticalCode;
import com.ecommercecloud.platform.vertical.VerticalModuleRegistry;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class TenantWorkspaceService {

    private final JdbcClient jdbcClient;
    private final VerticalModuleRegistry moduleRegistry;

    public TenantWorkspaceService(
            JdbcClient jdbcClient,
            VerticalModuleRegistry moduleRegistry
    ) {
        this.jdbcClient = jdbcClient;
        this.moduleRegistry = moduleRegistry;
    }

    public TenantWorkspaceView load(String tenantCode) {

        String normalizedCode =
                tenantCode.trim().toUpperCase();

        WorkspaceRow row = jdbcClient.sql("""
                        SELECT
                            t.id AS tenant_id,
                            t.codigo AS tenant_code,
                            t.nombre_comercial AS business_name,
                            t.propietario AS owner_name,
                            t.fecha_vencimiento AS expiration_date,
                            t.activo,
                            t.estado,
                            v.codigo AS vertical_code,
                            p.nombre AS plan_name,
                            COALESCE(
                                c.zona_horaria,
                                'America/Mexico_City'
                            ) AS timezone,
                            COALESCE(c.locale, 'es-MX') AS locale,
                            COALESCE(c.moneda, 'MXN') AS currency,
                            COALESCE(
                                c.color_primario,
                                '#2563EB'
                            ) AS primary_color
                        FROM tenants t
                        INNER JOIN verticales v
                            ON v.id = t.vertical_id
                        INNER JOIN planes p
                            ON p.id = t.plan_id
                        LEFT JOIN tenant_configuraciones c
                            ON c.tenant_id = t.id
                        WHERE UPPER(t.codigo) = :tenantCode
                        """)
                .param("tenantCode", normalizedCode)
                .query((rs, rowNum) ->
                        new WorkspaceRow(
                                rs.getLong("tenant_id"),
                                rs.getString("tenant_code"),
                                rs.getString("business_name"),
                                rs.getString("owner_name"),
                                rs.getString("plan_name"),
                                rs.getObject(
                                        "expiration_date",
                                        LocalDate.class
                                ),
                                rs.getBoolean("activo"),
                                rs.getString("estado"),
                                rs.getString("vertical_code"),
                                rs.getString("timezone"),
                                rs.getString("locale"),
                                rs.getString("currency"),
                                rs.getString("primary_color")
                        )
                )
                .optional()
                .orElseThrow(() ->
                        new BusinessException(
                                "No existe una empresa con el código "
                                        + normalizedCode
                        )
                );

        if (!row.active()
                || !"ACTIVO".equals(row.status())) {

            throw new BusinessException(
                    "La empresa no tiene una suscripción activa"
            );
        }

        VerticalCode verticalCode =
                VerticalCode.fromDatabase(
                        row.verticalCode()
                );

        return new TenantWorkspaceView(
                row.tenantId(),
                row.tenantCode(),
                row.businessName(),
                row.ownerName(),
                row.planName(),
                row.expirationDate(),
                row.timezone(),
                row.locale(),
                row.currency(),
                row.primaryColor(),
                moduleRegistry.get(verticalCode)
        );
    }

    private record WorkspaceRow(
            Long tenantId,
            String tenantCode,
            String businessName,
            String ownerName,
            String planName,
            LocalDate expirationDate,
            boolean active,
            String status,
            String verticalCode,
            String timezone,
            String locale,
            String currency,
            String primaryColor
    ) {
    }
}
