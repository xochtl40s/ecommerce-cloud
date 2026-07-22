package com.ecommercecloud.security.auth;

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

        if (principal.isPasswordChangeRequired()) {
            response.sendRedirect(
                    "/cuenta/cambiar-password"
            );
            return;
        }

        response.sendRedirect(
                "/workspace/" + principal.getTenantCode()
        );
    }
}
