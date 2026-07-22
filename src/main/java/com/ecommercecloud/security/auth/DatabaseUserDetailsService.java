package com.ecommercecloud.security.auth;

import com.ecommercecloud.security.entity.Usuario;
import com.ecommercecloud.security.repository.UsuarioRepository;
import com.ecommercecloud.tenant.entity.Tenant;
import com.ecommercecloud.tenant.repository.TenantRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DatabaseUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final TenantRepository tenantRepository;

    public DatabaseUserDetailsService(
            UsuarioRepository usuarioRepository,
            TenantRepository tenantRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        String normalizedUsername =
                username == null
                        ? ""
                        : username.trim();

        Usuario usuario = usuarioRepository
                .findByUsernameIgnoreCase(normalizedUsername)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario o contraseña incorrectos"
                        )
                );

        Tenant tenant = tenantRepository
                .findById(usuario.getTenantId())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "La empresa asociada no existe"
                        )
                );

        boolean accountActive =
                Boolean.TRUE.equals(usuario.getActivo())
                && Boolean.TRUE.equals(tenant.getActivo());

        return new ECommercePrincipal(
                usuario.getId(),
                tenant.getId(),
                tenant.getCodigo(),
                tenant.getNombreComercial(),
                usuario.getUsername(),
                usuario.getPasswordHash(),
                usuario.getNombre(),
                usuario.getRol(),
                accountActive,
                Boolean.TRUE.equals(
                        usuario.getCambioPasswordRequerido()
                )
        );
    }
}
