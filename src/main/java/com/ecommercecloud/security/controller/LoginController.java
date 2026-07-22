package com.ecommercecloud.security.controller;

import com.ecommercecloud.common.exception.BusinessException;
import com.ecommercecloud.security.auth.ECommercePrincipal;
import com.ecommercecloud.security.dto.PasswordChangeForm;
import com.ecommercecloud.security.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final AccountService accountService;

    public LoginController(
            AccountService accountService
    ) {
        this.accountService = accountService;
    }

    @GetMapping("/login")
    public String login(
            Authentication authentication
    ) {

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal()
                instanceof ECommercePrincipal principal) {

            if (principal.isPasswordChangeRequired()) {
                return "redirect:/cuenta/cambiar-password";
            }

            return "redirect:/workspace/"
                    + principal.getTenantCode();
        }

        return "security/login";
    }

    @GetMapping("/cuenta/cambiar-password")
    public String passwordForm(
            Authentication authentication,
            Model model
    ) {

        ECommercePrincipal principal =
                requirePrincipal(authentication);

        if (!model.containsAttribute(
                "passwordChangeForm"
        )) {
            model.addAttribute(
                    "passwordChangeForm",
                    new PasswordChangeForm()
            );
        }

        model.addAttribute("principal", principal);

        return "security/change-password";
    }

    @PostMapping("/cuenta/cambiar-password")
    public String changePassword(
            @Valid
            @ModelAttribute("passwordChangeForm")
            PasswordChangeForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {

        ECommercePrincipal principal =
                requirePrincipal(authentication);

        model.addAttribute("principal", principal);

        if (bindingResult.hasErrors()) {
            return "security/change-password";
        }

        try {
            accountService.changePassword(
                    principal,
                    form.getCurrentPassword(),
                    form.getNewPassword(),
                    form.getConfirmPassword()
            );

            return "redirect:/logout?passwordChanged";

        } catch (BusinessException exception) {
            model.addAttribute(
                    "messageError",
                    exception.getMessage()
            );

            return "security/change-password";
        }
    }

    @GetMapping("/acceso-denegado")
    public String accessDenied() {
        return "security/access-denied";
    }

    private ECommercePrincipal requirePrincipal(
            Authentication authentication
    ) {

        if (authentication == null
                || !(authentication.getPrincipal()
                instanceof ECommercePrincipal principal)) {

            throw new BusinessException(
                    "La sesión no es válida"
            );
        }

        return principal;
    }
}
