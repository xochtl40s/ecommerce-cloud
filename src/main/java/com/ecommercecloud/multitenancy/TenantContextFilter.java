package com.ecommercecloud.multitenancy;

import com.ecommercecloud.security.auth.ECommercePrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
public class TenantContextFilter
        extends OncePerRequestFilter {

    private static final String WORKSPACE_PREFIX =
            "/workspace/";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || !(authentication.getPrincipal()
                    instanceof ECommercePrincipal principal)) {

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }

            if (principal.isTenantUser()) {

                TenantContext.set(
                        principal.getTenantId(),
                        principal.getTenantCode()
                );

                if (!isWorkspaceAllowed(
                        request,
                        principal
                )) {
                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "No puedes acceder " +
                            "al espacio de otra empresa"
                    );

                    return;
                }
            }

            filterChain.doFilter(
                    request,
                    response
            );

        } finally {
            TenantContext.clear();
        }
    }

    private boolean isWorkspaceAllowed(
            HttpServletRequest request,
            ECommercePrincipal principal
    ) {

        String path =
                request.getRequestURI();

        if (!path.startsWith(
                WORKSPACE_PREFIX
        )) {
            return true;
        }

        String remaining =
                path.substring(
                        WORKSPACE_PREFIX.length()
                );

        String requestedTenantCode =
                remaining.split("/")[0];

        return requestedTenantCode
                .toUpperCase(Locale.ROOT)
                .equals(
                        principal
                                .getTenantCode()
                                .toUpperCase(Locale.ROOT)
                );
    }
}
