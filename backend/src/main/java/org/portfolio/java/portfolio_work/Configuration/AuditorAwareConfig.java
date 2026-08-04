package org.portfolio.java.portfolio_work.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig {

    public static final String SYSTEM_ACTOR = "SYSTEM";

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication == null
                    || !authentication.isAuthenticated()
                    || "anonymousUser".equals(
                    authentication.getPrincipal()
            )) {
                return Optional.of(SYSTEM_ACTOR);
            }

            if (authentication
                    instanceof JwtAuthenticationToken jwtAuthentication) {

                return Optional.ofNullable(
                                jwtAuthentication
                                        .getToken()
                                        .getSubject()
                        )
                        .filter(subject -> !subject.isBlank())
                        .or(() -> Optional.of(SYSTEM_ACTOR));
            }

            return Optional.ofNullable(
                            authentication.getName()
                    )
                    .filter(name -> !name.isBlank())
                    .or(() -> Optional.of(SYSTEM_ACTOR));
        };
    }
}