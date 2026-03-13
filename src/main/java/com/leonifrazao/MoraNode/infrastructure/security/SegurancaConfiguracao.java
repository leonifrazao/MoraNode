package com.leonifrazao.MoraNode.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SegurancaConfiguracao {

    private final JwtFiltroAutenticacao jwtFiltroAutenticacao;
    private final CorsConfigurationSource corsConfigurationSource;
    private final HandlerExceptionResolver exceptionResolver;

    public SegurancaConfiguracao(
            JwtFiltroAutenticacao jwtFiltroAutenticacao,
            CorsConfigurationSource corsConfigurationSource,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtFiltroAutenticacao = jwtFiltroAutenticacao;
        this.corsConfigurationSource = corsConfigurationSource;
        this.exceptionResolver = exceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> exceptionResolver
                                .resolveException(request, response, null, authException))
                        .accessDeniedHandler((request, response, accessDeniedException) -> exceptionResolver
                                .resolveException(request, response, null, accessDeniedException)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/registro", "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.GET, "/imoveis/**", "/contratos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/imoveis/**", "/contratos/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/imoveis/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/contratos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/imoveis/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFiltroAutenticacao, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
