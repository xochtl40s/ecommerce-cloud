package com.ecommercecloud.security.service;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.security.auth.ECommercePrincipal;
import com.ecommercecloud.security.entity.Usuario;
import com.ecommercecloud.security.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(
            ECommercePrincipal principal,
            String currentPassword,
            String newPassword,
            String confirmPassword
    ) {

        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException(
                    "La confirmación no coincide " +
                    "con la nueva contraseña"
            );
        }

        Usuario usuario = usuarioRepository
                .findById(principal.getUserId())
                .orElseThrow(() ->
                        new BusinessException(
                                "No encontramos la cuenta del usuario"
                        )
                );

        if (!passwordEncoder.matches(
                currentPassword,
                usuario.getPasswordHash()
        )) {
            throw new BusinessException(
                    "La contraseña actual es incorrecta"
            );
        }

        if (passwordEncoder.matches(
                newPassword,
                usuario.getPasswordHash()
        )) {
            throw new BusinessException(
                    "La nueva contraseña debe ser diferente"
            );
        }

        usuario.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        usuario.setCambioPasswordRequerido(false);

        usuarioRepository.save(usuario);
    }
}
