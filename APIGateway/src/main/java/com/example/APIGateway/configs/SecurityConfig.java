package com.example.APIGateway.configs;

import com.example.APIGateway.filters.JwtAuthenticationWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationWebFilter jwtAuthenticationWebFilter;

    public SecurityConfig(JwtAuthenticationWebFilter jwtAuthenticationWebFilter) {
        this.jwtAuthenticationWebFilter = jwtAuthenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http

                // Wyłączenie CSRF - API korzysta z JWT
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Wyłączenie formularza logowania
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // Wyłączenie HTTP Basic
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // Wyłączenie logouta Springa
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                // Konfiguracja dostępu
                .authorizeExchange(exchanges -> exchanges

                        // Endpointy publiczne
                        .pathMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/user/get"
                        ).permitAll()

                        // Wszystko inne wymaga JWT
                        .anyExchange().authenticated()
                )

                // Dodanie własnego filtra JWT
                .addFilterAt(
                        jwtAuthenticationWebFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )

                .build();
    }
}