package org.portfolio.java.portfolio_work.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI portfolioOpenApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Portfolio Work API")
                                .description("""
                                        Spring Boot REST API demonstrating:
                                        - User registration
                                        - JWT-based authentication
                                        - Role-based authorization
                                        - PostgreSQL persistence
                                        - Flyway migrations
                                        - Docker containerization
                                        """)
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Fülöp Martin")
                                )
                                .license(
                                        new License()
                                                .name("MIT License")
                                )
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BEARER_AUTH,
                                        new SecurityScheme()
                                                .name(BEARER_AUTH)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description(
                                                        "Enter the JWT access token without the 'Bearer' prefix."
                                                )
                                )
                );
    }
}