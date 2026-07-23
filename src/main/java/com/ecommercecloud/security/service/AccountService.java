package com.ecommercecloud.security.service;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.security.auth.ECommercePrincipal;
import com.ecommercecloud.security.entity.Usuario;
import com.ecommercecloud.security.platform.entity.PlatformUser;
import com.ecommercecloud.security.platform.repository.PlatformUserRepository;
import com.ecommercecloud.security.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final UsuarioRepository
            usuarioRepository;

    private final PlatformUserRepository
            platformUserRepository;

    private final PasswordEncoder
            passwordEncoder;

    public AccountService(
            UsuarioRepository usuarioRepository,
            PlatformUserRepository platformUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository =
                usuarioRepository;

        this.platformUserRepository =
                platformUserRepository;

        this.passwordEncoder =
                passwordEncoder;
    }

    public void changePassword(
            ECommercePrincipal principal,
            String currentPassword,
            String newPassword,
            String confirmPassword
    ) {
        validateConfirmation(
                newPassword,
                confirmPassword
        );

        if (principal.isPlatformUser()) {
            changePlatformPassword(
                    principal,
                    currentPassword,
                    newPassword
            );

            return;
        }

        changeTenantPassword(
                principal,
                currentPassword,
                newPassword
        );
    }

    private void changePlatformPassword(
            ECommercePrincipal principal,
            String currentPassword,
            String newPassword
    ) {
        PlatformUser user =
                platformUserRepository
                        .findById(
                                principal.getUserId()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "No encontramos " +
                                        "la cuenta administrativa"
                                )
                        );

        validateCurrentAndNewPassword(
                currentPassword,
                newPassword,
                user.getPasswordHash()
        );

        user.setPasswordHash(
                passwordEncoder.encode(
                        newPassword
                )
        );

        user.setCambioPasswordRequerido(
                false
        );

        platformUserRepository.save(user);
    }

    private void changeTenantPassword(
            ECommercePrincipal principal,
            String currentPassword,
            String newPassword
    ) {
        Usuario user =
                usuarioRepository
                        .findById(
                                principal.getUserId()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "No encontramos " +
                                        "la cuenta del usuario"
                                )
                        );

        validateCurrentAndNewPassword(
                currentPassword,
                newPassword,
                user.getPasswordHash()
        );

        user.setPasswordHash(
                passwordEncoder.encode(
                        newPassword
                )
        );

        user.setCambioPasswordRequerido(
                false
        );

        usuarioRepository.save(user);
    }

    private void validateConfirmation(
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
    }

    private void validateCurrentAndNewPassword(
            String currentPassword,
            String newPassword,
            String currentHash
    ) {
        if (!passwordEncoder.matches(
                currentPassword,
                currentHash
        )) {
            throw new BusinessException(
                    "La contraseña actual " +
                    "es incorrecta"
            );
        }

        if (passwordEncoder.matches(
                newPassword,
                currentHash
        )) {
            throw new BusinessException(
                    "La nueva contraseña " +
                    "debe ser diferente"
            );
        }
    }
}
