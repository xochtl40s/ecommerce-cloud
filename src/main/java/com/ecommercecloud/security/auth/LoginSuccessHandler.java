package com.ecommercecloud.security.auth;

import com.ecommercecloud.security.audit.service.LoginAuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final LoginAuditService
            loginAuditService;

    public LoginSuccessHandler(
            LoginAuditService loginAuditService
    ) {
        this.loginAuditService =
                loginAuditService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Object principalObject =
                authentication.getPrincipal();

        if (!(principalObject
                instanceof ECommercePrincipal principal)) {

            response.sendRedirect("/");
            return;
        }

        loginAuditService.registerSuccess(
                principal,
                request
        );

        if (principal.isPasswordChangeRequired()) {
            response.sendRedirect(
                    "/cuenta/cambiar-password"
            );

            return;
        }

        if (principal.isSuperAdmin()) {
            response.sendRedirect(
                    "/super-admin"
            );

            return;
        }

        if (principal.isTenantUser()) {
            response.sendRedirect(
                    "/workspace/"
                            + principal.getTenantCode()
            );

            return;
        }

        response.sendRedirect(
                "/acceso-denegado"
        );
    }
}
