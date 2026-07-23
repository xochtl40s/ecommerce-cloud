package com.ecommercecloud.security.platform.service;

import com.ecommercecloud.security.platform.entity.PlatformUser;
import com.ecommercecloud.security.platform.repository.PlatformUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Component
public class PlatformAdminInitializer
        implements CommandLineRunner {

    private final PlatformUserRepository
            platformUserRepository;

    private final PasswordEncoder
            passwordEncoder;

    private final String username;

    private final String password;

    private final String email;

    private final String displayName;

    public PlatformAdminInitializer(
            PlatformUserRepository platformUserRepository,
            PasswordEncoder passwordEncoder,

            @Value(
                "${ecommerce.bootstrap.super-admin.username:}"
            )
            String username,

            @Value(
                "${ecommerce.bootstrap.super-admin.password:}"
            )
            String password,

            @Value(
                "${ecommerce.bootstrap.super-admin.email:}"
            )
            String email,

            @Value(
                "${ecommerce.bootstrap.super-admin.name:Super Administrador}"
            )
            String displayName
    ) {
        this.platformUserRepository =
                platformUserRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.username =
                normalize(username);

        this.password =
                password == null
                        ? ""
                        : password;

        this.email =
                normalizeEmail(email);

        this.displayName =
                displayName == null
                        || displayName.isBlank()
                        ? "Super Administrador"
                        : displayName.trim();
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (username.isBlank()
                || password.isBlank()
                || email.isBlank()) {

            System.out.println(
                    "[SECURITY] Super Admin bootstrap " +
                    "no configurado."
            );

            return;
        }

        if (platformUserRepository
                .existsByUsernameIgnoreCase(
                        username
                )) {

            System.out.println(
                    "[SECURITY] Super Admin ya existe: "
                            + username
            );

            return;
        }

        PlatformUser user =
                new PlatformUser();

        user.setUsername(username);

        user.setPasswordHash(
                passwordEncoder.encode(password)
        );

        user.setNombre(displayName);

        user.setCorreo(email);

        user.setRol("SUPER_ADMIN");

        user.setActivo(true);

        user.setCambioPasswordRequerido(true);

        platformUserRepository.save(user);

        System.out.println(
                "[SECURITY] Super Admin creado: "
                        + username
        );
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim()
                    .toLowerCase(Locale.ROOT);
    }

    private String normalizeEmail(
            String value
    ) {
        return normalize(value);
    }
}
