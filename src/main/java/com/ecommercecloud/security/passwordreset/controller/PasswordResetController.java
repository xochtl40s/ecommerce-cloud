package com.ecommercecloud.security.passwordreset.controller;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.security.passwordreset.dto.ForgotPasswordForm;
import com.ecommercecloud.security.passwordreset.dto.ResetPasswordForm;
import com.ecommercecloud.security.passwordreset.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {

    private final PasswordResetService
            passwordResetService;

    public PasswordResetController(
            PasswordResetService passwordResetService
    ) {
        this.passwordResetService =
                passwordResetService;
    }

    @GetMapping("/olvide-password")
    public String forgotPassword(
            Model model
    ) {
        if (!model.containsAttribute(
                "forgotPasswordForm"
        )) {
            model.addAttribute(
                    "forgotPasswordForm",
                    new ForgotPasswordForm()
            );
        }

        return "security/forgot-password";
    }

    @PostMapping("/olvide-password")
    public String requestPasswordReset(
            @Valid
            @ModelAttribute("forgotPasswordForm")
            ForgotPasswordForm form,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            return "security/forgot-password";
        }

        passwordResetService.requestReset(
                form.getUsername(),
                extractIp(request)
        );

        /*
         * Siempre mostramos la misma respuesta.
         * No confirmamos si el usuario existe.
         */
        return "redirect:/olvide-password?sent";
    }

    @GetMapping("/restablecer-password")
    public String resetPasswordForm(
            @RequestParam String token,
            Model model
    ) {
        boolean valid =
                passwordResetService
                        .isTokenValid(token);

        model.addAttribute(
                "tokenValid",
                valid
        );

        if (valid) {
            ResetPasswordForm form =
                    new ResetPasswordForm();

            form.setToken(token);

            model.addAttribute(
                    "resetPasswordForm",
                    form
            );
        }

        return "security/reset-password";
    }

    @PostMapping("/restablecer-password")
    public String resetPassword(
            @Valid
            @ModelAttribute("resetPasswordForm")
            ResetPasswordForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "tokenValid",
                    true
            );

            return "security/reset-password";
        }

        try {
            passwordResetService.resetPassword(
                    form.getToken(),
                    form.getNewPassword(),
                    form.getConfirmPassword()
            );

            return "redirect:/login?resetSuccess";

        } catch (BusinessException exception) {
            model.addAttribute(
                    "tokenValid",
                    passwordResetService
                            .isTokenValid(
                                    form.getToken()
                            )
            );

            model.addAttribute(
                    "messageError",
                    exception.getMessage()
            );

            return "security/reset-password";
        }
    }

    private String extractIp(
            HttpServletRequest request
    ) {
        String forwarded =
                request.getHeader(
                        "X-Forwarded-For"
                );

        if (forwarded != null
                && !forwarded.isBlank()) {
            return forwarded.split(",")[0]
                    .trim();
        }

        return request.getRemoteAddr();
    }
}
