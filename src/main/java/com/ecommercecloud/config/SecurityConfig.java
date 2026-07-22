package com.ecommercecloud.config;

import com.ecommercecloud.security.auth.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSuccessHandler loginSuccessHandler
    ) throws Exception {

        http
                /*
                 * Temporalmente desactivado porque los formularios
                 * actuales del Super Admin todavía no incluyen tokens.
                 *
                 * Antes de producción lo habilitaremos.
                 */
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/inicio",
                                "/login",
                                "/soluciones/**",
                                "/api/public/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/actuator/health",
                                "/error",
                                "/acceso-denegado"
                        ).permitAll()

                        /*
                         * Temporal durante desarrollo.
                         * En Sprint 4.3 será ROLE_SUPER_ADMIN.
                         */
                        .requestMatchers(
                                "/super-admin/**",
                                "/api/admin/tenants/**"
                        ).permitAll()

                        .requestMatchers(
                                "/cuenta/**",
                                "/workspace/**"
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .exceptionHandling(exception ->
                        exception.accessDeniedPage(
                                "/acceso-denegado"
                        )
                );

        return http.build();
    }
}
