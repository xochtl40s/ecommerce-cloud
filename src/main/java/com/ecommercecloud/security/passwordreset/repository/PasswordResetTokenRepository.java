package com.ecommercecloud.security.passwordreset.repository;

import com.ecommercecloud.security.passwordreset.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken>
    findByTokenHashAndUsedAtIsNull(
            String tokenHash
    );

    List<PasswordResetToken>
    findByPlatformUserIdAndUsedAtIsNull(
            Long platformUserId
    );

    List<PasswordResetToken>
    findByTenantUserIdAndUsedAtIsNull(
            Long tenantUserId
    );
}
