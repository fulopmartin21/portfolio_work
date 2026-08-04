package org.portfolio.java.portfolio_work.LoginTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.portfolio.java.portfolio_work.Configuration.AuditorAwareConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuditorAwareConfigTests {

    private final AuditorAwareConfig config =
            new AuditorAwareConfig();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void auditorAware_shouldReturnSystem_withoutAuthentication() {
        assertThat(
                config.auditorAware()
                        .getCurrentAuditor()
        ).contains(
                AuditorAwareConfig.SYSTEM_ACTOR
        );
    }

    @Test
    void auditorAware_shouldReturnJwtSubject() {
        Instant now = Instant.now();

        Jwt jwt = new Jwt(
                "token",
                now,
                now.plusSeconds(900),
                Map.of(
                        "alg", "RS256",
                        "typ", "JWT"
                ),
                Map.of(
                        "sub", "user-id-123"
                )
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new JwtAuthenticationToken(jwt)
                );

        assertThat(
                config.auditorAware()
                        .getCurrentAuditor()
        ).contains("SYSTEM");
    }

    @Test
    void auditorAware_shouldReturnAuthenticationName_forNonJwtAuthentication() {
        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        UsernamePasswordAuthenticationToken
                                .authenticated(
                                        "test-user",
                                        null,
                                        java.util.List.of()
                                )
                );

        assertThat(
                config.auditorAware()
                        .getCurrentAuditor()
        ).contains("test-user");
    }
}