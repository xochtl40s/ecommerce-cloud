package com.ecommercecloud.tenant.service;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.security.entity.RolUsuario;
import com.ecommercecloud.security.entity.Usuario;
import com.ecommercecloud.security.repository.UsuarioRepository;
import com.ecommercecloud.tenant.dto.CredencialesTemporalesResponse;
import com.ecommercecloud.tenant.dto.TenantActivationResponse;
import com.ecommercecloud.tenant.dto.TenantCreateRequest;
import com.ecommercecloud.tenant.dto.TenantResponse;
import com.ecommercecloud.tenant.entity.EstadoTenant;
import com.ecommercecloud.tenant.entity.Tenant;
import com.ecommercecloud.tenant.repository.TenantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class TenantServiceImpl implements TenantService {

    private static final String CARACTERES_PASSWORD =
            "ABCDEFGHJKLMNPQRSTUVWXYZ" +
            "abcdefghijkmnopqrstuvwxyz" +
            "23456789" +
            "@#$%";

    private final TenantRepository tenantRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;

    public TenantServiceImpl(
            TenantRepository tenantRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.tenantRepository = tenantRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public TenantResponse crearProspecto(
            TenantCreateRequest request
    ) {

        String correoNormalizado =
                request.correo().trim().toLowerCase(Locale.ROOT);

        if (tenantRepository.existsByCorreo(correoNormalizado)) {
            throw new BusinessException(
                    "Ya existe una empresa registrada " +
                    "con el correo indicado"
            );
        }

        Tenant tenant = new Tenant();

        tenant.setCodigo(generarCodigo(request.verticalId()));
        tenant.setNombreComercial(
                request.nombreComercial().trim()
        );
        tenant.setRazonSocial(
                normalizarOpcional(request.razonSocial())
        );
        tenant.setPropietario(request.propietario().trim());
        tenant.setCorreo(correoNormalizado);
        tenant.setTelefono(
                normalizarOpcional(request.telefono())
        );
        tenant.setCiudad(
                normalizarOpcional(request.ciudad())
        );
        tenant.setVerticalId(request.verticalId());
        tenant.setPlanId(request.planId());
        tenant.setEstado(EstadoTenant.PROSPECTO);
        tenant.setActivo(false);

        return mapear(tenantRepository.save(tenant));
    }

    @Override
    public TenantActivationResponse activar(Long id) {

        Tenant tenant = buscarTenant(id);

        if (Boolean.TRUE.equals(tenant.getActivo())
                || tenant.getEstado() == EstadoTenant.ACTIVO) {

            throw new BusinessException(
                    "La empresa ya se encuentra activa"
            );
        }

        if (tenant.getEstado() == EstadoTenant.CANCELADO) {
            throw new BusinessException(
                    "Una empresa cancelada no puede " +
                    "activarse directamente"
            );
        }

        boolean yaTieneAdministrador =
                usuarioRepository.existsByTenantIdAndRol(
                        tenant.getId(),
                        RolUsuario.TENANT_ADMIN
                );

        if (yaTieneAdministrador) {
            throw new BusinessException(
                    "La empresa ya tiene un administrador"
            );
        }

        LocalDate fechaActivacion = LocalDate.now();

        tenant.setEstado(EstadoTenant.ACTIVO);
        tenant.setActivo(true);
        tenant.setFechaActivacion(fechaActivacion);
        tenant.setFechaVencimiento(
                fechaActivacion.plusMonths(1)
        );

        Tenant tenantActualizado =
                tenantRepository.save(tenant);

        String passwordTemporal =
                generarPasswordTemporal(12);

        Usuario administrador = new Usuario();

        administrador.setTenantId(tenant.getId());
        administrador.setUsername(
                generarUsernameAdministrador(tenant.getCodigo())
        );
        administrador.setPasswordHash(
                passwordEncoder.encode(passwordTemporal)
        );
        administrador.setNombre(tenant.getPropietario());
        administrador.setCorreo(tenant.getCorreo());
        administrador.setRol(RolUsuario.TENANT_ADMIN);
        administrador.setActivo(true);
        administrador.setCambioPasswordRequerido(true);

        Usuario usuarioGuardado =
                usuarioRepository.save(administrador);

        CredencialesTemporalesResponse credenciales =
                new CredencialesTemporalesResponse(
                        usuarioGuardado.getUsername(),
                        passwordTemporal,
                        usuarioGuardado
                                .getCambioPasswordRequerido()
                );

        return new TenantActivationResponse(
                mapear(tenantActualizado),
                credenciales
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TenantResponse obtenerPorId(Long id) {
        return mapear(buscarTenant(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponse> listarTodos() {
        return tenantRepository.findAll()
                .stream()
                .map(this::mapear)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponse> listarPorEstado(
            EstadoTenant estado
    ) {
        return tenantRepository
                .findByEstadoOrderByCreadoEnDesc(estado)
                .stream()
                .map(this::mapear)
                .toList();
    }

    private Tenant buscarTenant(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() ->
                        new BusinessException(
                                "No existe el tenant con id " + id
                        )
                );
    }

    private String generarUsernameAdministrador(
            String codigoTenant
    ) {
        return "admin." +
                codigoTenant
                        .toLowerCase(Locale.ROOT)
                        .replace("-", "");
    }

    private String generarPasswordTemporal(int longitud) {

        StringBuilder password = new StringBuilder(longitud);

        for (int i = 0; i < longitud; i++) {
            int posicion = secureRandom.nextInt(
                    CARACTERES_PASSWORD.length()
            );

            password.append(
                    CARACTERES_PASSWORD.charAt(posicion)
            );
        }

        return password.toString();
    }

    private String generarCodigo(Long verticalId) {

        String prefijo = switch (verticalId.intValue()) {
            case 1 -> "ABA";
            case 2 -> "RES";
            case 3 -> "GYM";
            default -> "TEN";
        };

        long consecutivo = tenantRepository.count() + 1;

        return "%s-%06d".formatted(
                prefijo,
                consecutivo
        );
    }

    private String normalizarOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }

    private TenantResponse mapear(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getCodigo(),
                tenant.getNombreComercial(),
                tenant.getRazonSocial(),
                tenant.getPropietario(),
                tenant.getCorreo(),
                tenant.getTelefono(),
                tenant.getCiudad(),
                tenant.getEstado(),
                tenant.getVerticalId(),
                tenant.getPlanId(),
                tenant.getFechaActivacion(),
                tenant.getFechaVencimiento(),
                tenant.getActivo(),
                tenant.getCreadoEn(),
                tenant.getActualizadoEn()
        );
    }
}
