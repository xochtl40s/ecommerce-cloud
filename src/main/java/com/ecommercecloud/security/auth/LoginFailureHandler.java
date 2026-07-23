package com.ecommercecloud.security.auth;

import com.ecommercecloud.security.audit.service.LoginAuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginFailureHandler
        implements AuthenticationFailureHandler {

    private final LoginAuditService
            loginAuditService;

    public LoginFailureHandler(
            LoginAuditService loginAuditService
    ) {
        this.loginAuditService =
                loginAuditService;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        String username =
                request.getParameter(
                        "username"
                );

        loginAuditService.registerFailure(
                username,
                exception.getClass()
                        .getSimpleName(),
                request
        );

        response.sendRedirect(
                "/login?error"
        );
    }
}
