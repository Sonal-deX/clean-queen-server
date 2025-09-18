package com.enterprise.cleanqueen.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.enterprise.cleanqueen.dto.common.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            
            ApiErrorResponse error = new ApiErrorResponse(
                false,
                "Access denied. Insufficient privileges to access this resource.",
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                request.getRequestURI()
            );
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules(); // For LocalDateTime serialization
            response.getWriter().write(mapper.writeValueAsString(error));
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            
            ApiErrorResponse error = new ApiErrorResponse(
                false,
                "Authentication required. Please provide a valid token.",
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                request.getRequestURI()
            );
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules(); // For LocalDateTime serialization
            response.getWriter().write(mapper.writeValueAsString(error));
        };
    }

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
                .requestMatchers("/docs.html", "/docs/**").permitAll() // Allow Scalar API docs
                .requestMatchers("/actuator/**").permitAll() // Allow actuator endpoints
                .requestMatchers(HttpMethod.GET, "/projects/user/**").permitAll() // Allow public access to get projects by user
                .requestMatchers(HttpMethod.GET, "/projects/*/tasks").permitAll() // Allow public access to get project tasks

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
                )
                .exceptionHandling(exceptions -> exceptions
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(authenticationEntryPoint())
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
