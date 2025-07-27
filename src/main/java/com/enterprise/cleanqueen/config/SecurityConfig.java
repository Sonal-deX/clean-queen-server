package com.enterprise.cleanqueen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/health/**").permitAll() // Allow health endpoints
                .requestMatchers("/auth/**").permitAll() // Allow authentication endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // Allow Swagger
                .requestMatchers("/docs.html").permitAll() // Add this line

                // Admin only endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/projects/**").hasRole("ADMIN")
                // Customer endpoints
                .requestMatchers("/requests/**").hasRole("CUSTOMER")
                .requestMatchers("/assignments/customer").hasRole("CUSTOMER")
                // Supervisor endpoints
                .requestMatchers("/assignments/supervisor/**").hasRole("SUPERVISOR")
                .requestMatchers("/reviews/**").hasRole("SUPERVISOR")
                // All other endpoints require authentication
                .anyRequest().authenticated() // All other endpoints require authentication
                );

        return http.build();
    }
}
