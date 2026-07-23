package com.ecommercecloud.config;

import com.ecommercecloud.multitenancy.TenantContextFilter;
import com.ecommercecloud.security.auth.LoginFailureHandler;
import com.ecommercecloud.security.auth.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSuccessHandler successHandler,
            LoginFailureHandler failureHandler,
            TenantContextFilter tenantContextFilter
    ) throws Exception {

        http
                .csrf(csrf -> csrf
                        /*
                         * Los formularios Thymeleaf
                         * conservan protección CSRF.
                         *
                         * La API se deja fuera temporalmente
                         * hasta implementar JWT/API tokens.
                         */
                        .ignoringRequestMatchers(
                                "/api/**"
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/inicio",
                                "/login",
                                "/olvide-password",
                                "/restablecer-password",
                                "/soluciones/**",
                                "/api/public/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/actuator/health",
                                "/error",
                                "/acceso-denegado"
                        ).permitAll()

                        .requestMatchers(
                                "/super-admin/**",
                                "/api/admin/**"
                        ).hasRole("SUPER_ADMIN")

                        .requestMatchers(
                                "/cuenta/**"
                        ).authenticated()

                        .requestMatchers(
                                "/workspace/**"
                        ).authenticated()

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(
                                "/login?logout"
                        )
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .exceptionHandling(exception ->
                        exception.accessDeniedPage(
                                "/acceso-denegado"
                        )
                )

                .sessionManagement(session ->
                        session
                                .sessionFixation()
                                .migrateSession()
                                .maximumSessions(1)
                )

                .addFilterAfter(
                        tenantContextFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
