package com.ecommercecloud.security.platform.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "platform_users")
public class PlatformUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String username;

    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(
            nullable = false,
            unique = true,
            length = 150
    )
    private String correo;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(nullable = false)
    private Boolean activo;

    @Column(
            name = "cambio_password_requerido",
            nullable = false
    )
    private Boolean cambioPasswordRequerido;

    @Column(name = "creado_en", nullable = false)
    private OffsetDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private OffsetDateTime actualizadoEn;

    @PrePersist
    public void prePersist() {

        OffsetDateTime ahora =
                OffsetDateTime.now();

        if (rol == null || rol.isBlank()) {
            rol = "SUPER_ADMIN";
        }

        if (activo == null) {
            activo = true;
        }

        if (cambioPasswordRequerido == null) {
            cambioPasswordRequerido = true;
        }

        creadoEn = ahora;
        actualizadoEn = ahora;
    }

    @PreUpdate
    public void preUpdate() {
        actualizadoEn = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(
            String passwordHash
    ) {
        this.passwordHash = passwordHash;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getCambioPasswordRequerido() {
        return cambioPasswordRequerido;
    }

    public void setCambioPasswordRequerido(
            Boolean cambioPasswordRequerido
    ) {
        this.cambioPasswordRequerido =
                cambioPasswordRequerido;
    }

    public OffsetDateTime getCreadoEn() {
        return creadoEn;
    }

    public OffsetDateTime getActualizadoEn() {
        return actualizadoEn;
    }
}
