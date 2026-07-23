package com.ecommercecloud.security.audit.service;

import com.ecommercecloud.security.audit.entity.LoginAudit;
import com.ecommercecloud.security.audit.repository.LoginAuditRepository;
import com.ecommercecloud.security.auth.ECommercePrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginAuditService {

    private final LoginAuditRepository
            loginAuditRepository;

    public LoginAuditService(
            LoginAuditRepository loginAuditRepository
    ) {
        this.loginAuditRepository =
                loginAuditRepository;
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public void registerSuccess(
            ECommercePrincipal principal,
            HttpServletRequest request
    ) {
        LoginAudit audit =
                createBaseAudit(
                        principal.getUsername(),
                        request
                );

        audit.setAccountType(
                principal
                        .getAccountType()
                        .name()
        );

        audit.setTenantId(
                principal.getTenantId()
        );

        audit.setSuccess(true);

        loginAuditRepository.save(audit);
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public void registerFailure(
            String username,
            String reason,
            HttpServletRequest request
    ) {
        LoginAudit audit =
                createBaseAudit(
                        username,
                        request
                );

        audit.setSuccess(false);

        audit.setFailureReason(
                limit(reason, 255)
        );

        loginAuditRepository.save(audit);
    }

    private LoginAudit createBaseAudit(
            String username,
            HttpServletRequest request
    ) {
        LoginAudit audit =
                new LoginAudit();

        audit.setUsername(
                limit(username, 150)
        );

        audit.setIpAddress(
                extractIp(request)
        );

        audit.setUserAgent(
                limit(
                        request.getHeader(
                                "User-Agent"
                        ),
                        500
                )
        );

        return audit;
    }

    private String extractIp(
            HttpServletRequest request
    ) {
        String forwarded =
                request.getHeader(
                        "X-Forwarded-For"
                );

        if (forwarded != null
                && !forwarded.isBlank()) {

            return limit(
                    forwarded.split(",")[0].trim(),
                    80
            );
        }

        return limit(
                request.getRemoteAddr(),
                80
        );
    }

    private String limit(
            String value,
            int maxLength
    ) {
        if (value == null) {
            return null;
        }

        if (value.length() <= maxLength) {
            return value;
        }

        return value.substring(
                0,
                maxLength
        );
    }
}
