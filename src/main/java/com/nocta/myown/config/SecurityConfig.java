package com.nocta.myown.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nocta.myown.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig( JwtAuthenticationFilter jwtAuthenticationFilter ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /*
     * PANEL WEB ADMINISTRATIVO
     */
    @Bean
    @Order(1)
    SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {

        return http.securityMatcher(
                        "/admin/**",
                        "/admin-login",
                        "/admin-logout"
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/admin-login",
                                "/admin/css/**",
                                "/admin/js/**"
                        ).permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/admin-login")
                        .loginProcessingUrl("/admin-login")
                        .defaultSuccessUrl(
                                "/admin/especialidades",
                                true
                        )
                        .failureUrl(
                                "/admin-login?error"
                        )
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/admin-logout")
                        .logoutSuccessUrl(
                                "/admin-login?logout"
                        )
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                .build();
    }

    /*
     * API REST UTILIZADA POR ANDROID
     */
    @Bean
    @Order(2)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .xssProtection(Customizer.withDefaults())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/registro",
                                "/auth/registro/solicitar",
                                "/auth/registro/verificar",
                                "/auth/login",
                                "/auth/refresh",
                                "/auth/olvide-password",
                                "/auth/restablecer-password",
                                "/health",
                                "/auth/google"
                        ).permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}