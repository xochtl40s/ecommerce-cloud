package com.ecommercecloud.platform.workspace;

import com.ecommercecloud.platform.vertical.VerticalDescriptor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    /**
     * Calcula los días restantes de la suscripción.
     *
     * @return días restantes, 0 cuando ya venció o null cuando no hay
     * fecha de vencimiento.
     */
    public Long daysRemaining() {

        if (expirationDate == null) {
            return null;
        }

        long days = ChronoUnit.DAYS.between(
                LocalDate.now(),
                expirationDate
        );

        return Math.max(days, 0);
    }

    /**
     * Código interno utilizado por la interfaz para representar el estado.
     */
    public String subscriptionStatus() {

        if (expirationDate == null) {
            return "NO_EXPIRATION";
        }

        long days = ChronoUnit.DAYS.between(
                LocalDate.now(),
                expirationDate
        );

        if (days < 0) {
            return "EXPIRED";
        }

        if (days <= 7) {
            return "CRITICAL";
        }

        if (days <= 30) {
            return "WARNING";
        }

        return "ACTIVE";
    }

    /**
     * Texto comercial mostrado al cliente.
     */
    public String subscriptionStatusLabel() {

        return switch (subscriptionStatus()) {
            case "EXPIRED" -> "Suscripción vencida";
            case "CRITICAL" -> "Renovación urgente";
            case "WARNING" -> "Próxima a vencer";
            case "NO_EXPIRATION" -> "Sin vencimiento";
            default -> "Suscripción activa";
        };
    }

    public String safeOwnerName() {

        if (ownerName == null || ownerName.isBlank()) {
            return "Administrador";
        }

        return ownerName.trim();
    }

    public String safeBusinessName() {

        if (businessName == null || businessName.isBlank()) {
            return "Mi empresa";
        }

        return businessName.trim();
    }

    public String safePlanName() {

        if (planName == null || planName.isBlank()) {
            return "Plan personalizado";
        }

        return planName.trim();
    }
}
