package com.ecommercecloud.security.passwordreset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ForgotPasswordForm {

    @NotBlank(
            message = "Escribe tu nombre de usuario"
    )
    @Size(
            max = 100,
            message = "El usuario no puede exceder 100 caracteres"
    )
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username
    ) {
        this.username = username;
    }
}
