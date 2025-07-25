package com.enterprise.cleanqueen.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("🧹 Clean Queen Service Management System API")
                .description("""
                    **Enterprise-grade backend system for managing cleaning services**
                    
                    ## 🌟 Key Features
                    - **Role-Based Access Control (RBAC):** Secure endpoints for CUSTOMER, SUPERVISOR, and ADMIN roles
                    - **Hierarchical Task Management:** Support for projects with nested task structures
                    - **JWT Authentication:** Secure API access with JSON Web Tokens
                    - **Email Verification:** OTP-based account verification system
                    - **Intelligent Rating System:** Automatic rating propagation up task hierarchies
                    - **Code-Based Assignment:** Project assignment using unique shareable codes
                    
                    ## 🔐 Authentication Flow
                    1. **Register** → 2. **Verify OTP** → 3. **Login** → 4. **Access Protected Endpoints**
                    
                    ## 🚀 Getting Started
                    1. Register a new account with `/auth/register`
                    2. Check email for OTP and verify with `/auth/verify-otp`
                    3. Login with `/auth/login` to get JWT tokens
                    4. Use the "Authorize" button above to set your Bearer token
                    5. Explore and test all available endpoints!
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("Clean Queen Development Team")
                    .email("lasinthaattanayake@gmail.com")
                    .url("https://cleanqueen.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:" + serverPort + "/api")
                    .description("🔧 Development Server"),
                new Server()
                    .url("https://api.cleanqueen.com/api")
                    .description("🚀 Production Server")))
            .addSecurityItem(new SecurityRequirement().addList("JWT Authentication"))
            .components(new Components()
                .addSecuritySchemes("JWT Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("🔑 Enter JWT token obtained from login endpoint. Format: Bearer <your_jwt_token>")));
    }
    
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
            .group("authentication")
            .pathsToMatch("/auth/**")
            .build();
    }
}