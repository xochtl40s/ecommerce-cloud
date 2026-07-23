package com.ecommercecloud.security.passwordreset.service;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.security.auth.AccountType;
import com.ecommercecloud.security.entity.Usuario;
import com.ecommercecloud.security.passwordreset.entity.PasswordResetToken;
import com.ecommercecloud.security.passwordreset.repository.PasswordResetTokenRepository;
import com.ecommercecloud.security.platform.entity.PlatformUser;
import com.ecommercecloud.security.platform.repository.PlatformUserRepository;
import com.ecommercecloud.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class PasswordResetService {

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    private final PlatformUserRepository
            platformUserRepository;

    private final UsuarioRepository
            usuarioRepository;

    private final PasswordResetTokenRepository
            tokenRepository;

    private final PasswordEncoder
            passwordEncoder;

    private final PasswordResetMailService
            mailService;

    private final String publicBaseUrl;

    private final int expirationMinutes;

    public PasswordResetService(
            PlatformUserRepository
                    platformUserRepository,

            UsuarioRepository
                    usuarioRepository,

            PasswordResetTokenRepository
                    tokenRepository,

            PasswordEncoder passwordEncoder,

            PasswordResetMailService mailService,

            @Value(
                "${ecommerce.public-base-url:http://localhost:8080}"
            )
            String publicBaseUrl,

            @Value(
                "${ecommerce.password-reset.expiration-minutes:30}"
            )
            int expirationMinutes
    ) {
        this.platformUserRepository =
                platformUserRepository;

        this.usuarioRepository =
                usuarioRepository;

        this.tokenRepository =
                tokenRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.mailService = mailService;

        this.publicBaseUrl =
                removeTrailingSlash(
                        publicBaseUrl
                );

        this.expirationMinutes =
                expirationMinutes;
    }

    public void requestReset(
            String username,
            String requestedIp
    ) {
        String normalizedUsername =
                normalize(username);

        PlatformUser platformUser =
                platformUserRepository
                        .findByUsernameIgnoreCase(
                                normalizedUsername
                        )
                        .orElse(null);

        if (platformUser != null) {
            if (Boolean.TRUE.equals(
                    platformUser.getActivo()
            )) {
                issuePlatformToken(
                        platformUser,
                        requestedIp
                );
            }

            return;
        }

        Usuario tenantUser =
                usuarioRepository
                        .findByUsernameIgnoreCase(
                                normalizedUsername
                        )
                        .orElse(null);

        if (tenantUser != null
                && Boolean.TRUE.equals(
                        tenantUser.getActivo()
                )) {

            issueTenantToken(
                    tenantUser,
                    requestedIp
            );
        }

        /*
         * No lanzamos error si el usuario no existe.
         * La respuesta pública debe ser siempre igual.
         */
    }

    @Transactional(readOnly = true)
    public boolean isTokenValid(
            String rawToken
    ) {
        if (rawToken == null
                || rawToken.isBlank()) {
            return false;
        }

        String tokenHash =
                hashToken(rawToken);

        return tokenRepository
                .findByTokenHashAndUsedAtIsNull(
                        tokenHash
                )
                .filter(
                        PasswordResetToken::isValid
                )
                .isPresent();
    }

    public void resetPassword(
            String rawToken,
            String newPassword,
            String confirmPassword
    ) {
        if (!newPassword.equals(
                confirmPassword
        )) {
            throw new BusinessException(
                    "La confirmación no coincide " +
                    "con la nueva contraseña"
            );
        }

        String tokenHash =
                hashToken(rawToken);

        PasswordResetToken token =
                tokenRepository
                        .findByTokenHashAndUsedAtIsNull(
                                tokenHash
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "El enlace es inválido " +
                                        "o ya fue utilizado"
                                )
                        );

        if (!token.isValid()) {
            throw new BusinessException(
                    "El enlace de recuperación expiró"
            );
        }

        if (token.getAccountType()
                == AccountType.PLATFORM) {

            resetPlatformPassword(
                    token,
                    newPassword
            );

        } else {
            resetTenantPassword(
                    token,
                    newPassword
            );
        }

        token.markUsed();

        tokenRepository.save(token);
    }

    private void issuePlatformToken(
            PlatformUser user,
            String requestedIp
    ) {
        invalidateTokens(
                tokenRepository
                        .findByPlatformUserIdAndUsedAtIsNull(
                                user.getId()
                        )
        );

        GeneratedToken generated =
                createToken(
                        AccountType.PLATFORM,
                        user.getId(),
                        null,
                        requestedIp
                );

        mailService.sendPasswordReset(
                user.getCorreo(),
                user.getNombre(),
                buildResetUrl(generated.rawToken()),
                expirationMinutes
        );
    }

    private void issueTenantToken(
            Usuario user,
            String requestedIp
    ) {
        invalidateTokens(
                tokenRepository
                        .findByTenantUserIdAndUsedAtIsNull(
                                user.getId()
                        )
        );

        GeneratedToken generated =
                createToken(
                        AccountType.TENANT,
                        null,
                        user.getId(),
                        requestedIp
                );

        mailService.sendPasswordReset(
                user.getCorreo(),
                user.getNombre(),
                buildResetUrl(generated.rawToken()),
                expirationMinutes
        );
    }

    private GeneratedToken createToken(
            AccountType accountType,
            Long platformUserId,
            Long tenantUserId,
            String requestedIp
    ) {
        byte[] randomBytes =
                new byte[32];

        SECURE_RANDOM.nextBytes(
                randomBytes
        );

        String rawToken =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(
                                randomBytes
                        );

        PasswordResetToken token =
                new PasswordResetToken();

        token.setAccountType(accountType);

        token.setPlatformUserId(
                platformUserId
        );

        token.setTenantUserId(
                tenantUserId
        );

        token.setTokenHash(
                hashToken(rawToken)
        );

        token.setExpiresAt(
                OffsetDateTime.now()
                        .plusMinutes(
                                expirationMinutes
                        )
        );

        token.setRequestedIp(
                limit(requestedIp, 80)
        );

        tokenRepository.save(token);

        return new GeneratedToken(
                rawToken
        );
    }

    private void resetPlatformPassword(
            PasswordResetToken token,
            String newPassword
    ) {
        PlatformUser user =
                platformUserRepository
                        .findById(
                                token.getPlatformUserId()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "La cuenta ya no existe"
                                )
                        );

        if (passwordEncoder.matches(
                newPassword,
                user.getPasswordHash()
        )) {
            throw new BusinessException(
                    "La nueva contraseña debe ser diferente"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        newPassword
                )
        );

        user.setCambioPasswordRequerido(
                false
        );

        platformUserRepository.save(user);

        invalidateTokens(
                tokenRepository
                        .findByPlatformUserIdAndUsedAtIsNull(
                                user.getId()
                        ),
                token.getId()
        );
    }

    private void resetTenantPassword(
            PasswordResetToken token,
            String newPassword
    ) {
        Usuario user =
                usuarioRepository
                        .findById(
                                token.getTenantUserId()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "La cuenta ya no existe"
                                )
                        );

        if (passwordEncoder.matches(
                newPassword,
                user.getPasswordHash()
        )) {
            throw new BusinessException(
                    "La nueva contraseña debe ser diferente"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(
                        newPassword
                )
        );

        user.setCambioPasswordRequerido(
                false
        );

        usuarioRepository.save(user);

        invalidateTokens(
                tokenRepository
                        .findByTenantUserIdAndUsedAtIsNull(
                                user.getId()
                        ),
                token.getId()
        );
    }

    private void invalidateTokens(
            List<PasswordResetToken> tokens
    ) {
        invalidateTokens(tokens, null);
    }

    private void invalidateTokens(
            List<PasswordResetToken> tokens,
            Long tokenToKeep
    ) {
        for (PasswordResetToken token : tokens) {

            if (tokenToKeep != null
                    && tokenToKeep.equals(
                            token.getId()
                    )) {
                continue;
            }

            token.markUsed();
        }

        tokenRepository.saveAll(tokens);
    }

    private String buildResetUrl(
            String rawToken
    ) {
        return publicBaseUrl
                + "/restablecer-password?token="
                + rawToken;
    }

    private String hashToken(
            String rawToken
    ) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hash =
                    digest.digest(
                            rawToken.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return HexFormat.of()
                    .formatHex(hash);

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "No fue posible proteger el token",
                    exception
            );
        }
    }

    private String normalize(
            String value
    ) {
        return value == null
                ? ""
                : value.trim()
                    .toLowerCase(Locale.ROOT);
    }

    private String removeTrailingSlash(
            String value
    ) {
        if (value == null
                || value.isBlank()) {
            return "http://localhost:8080";
        }

        String result = value.trim();

        while (result.endsWith("/")) {
            result = result.substring(
                    0,
                    result.length() - 1
            );
        }

        return result;
    }

    private String limit(
            String value,
            int maxLength
    ) {
        if (value == null) {
            return null;
        }

        return value.length() <= maxLength
                ? value
                : value.substring(
                        0,
                        maxLength
                );
    }

    private record GeneratedToken(
            String rawToken
    ) {
    }
}
