package com.ecommercecloud.superadmin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TenantCreateForm {

    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 150)
    private String nombreComercial;

    @Size(max = 200)
    private String razonSocial;

    @NotBlank(message = "El propietario es obligatorio")
    @Size(max = 150)
    private String propietario;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Escribe un correo válido")
    @Size(max = 150)
    private String correo;

    @Size(max = 30)
    private String telefono;

    @Size(max = 100)
    private String ciudad;

    @NotNull(message = "Selecciona el tipo de negocio")
    private Long verticalId;

    @NotNull(message = "Selecciona un plan")
    private Long planId;

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Long getVerticalId() {
        return verticalId;
    }

    public void setVerticalId(Long verticalId) {
        this.verticalId = verticalId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
