package com.ecommercecloud.security.passwordreset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResetPasswordForm {

    @NotBlank
    private String token;

    @NotBlank(
            message = "La nueva contraseña es obligatoria"
    )
    @Size(
            min = 10,
            max = 72,
            message = "Debe contener entre 10 y 72 caracteres"
    )
    @Pattern(
            regexp =
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)" +
                    "(?=.*[^A-Za-z0-9]).+$",
            message =
                    "Debe incluir mayúscula, minúscula, número y símbolo"
    )
    private String newPassword;

    @NotBlank(
            message = "Confirma la nueva contraseña"
    )
    private String confirmPassword;

    public String getToken() {
        return token;
    }

    public void setToken(
            String token
    ) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(
            String newPassword
    ) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(
            String confirmPassword
    ) {
        this.confirmPassword = confirmPassword;
    }
}
