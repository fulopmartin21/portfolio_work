package org.portfolio.java.portfolio_work.Login;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenVersionValidator
        implements OAuth2TokenValidator<Jwt> {

    public static final String TOKEN_VERSION_CLAIM =
            "tokenVersion";

    private final LoginRepository loginRepository;

    public TokenVersionValidator(
            LoginRepository loginRepository
    ) {
        this.loginRepository = loginRepository;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        try {
            UUID userId =
                    UUID.fromString(jwt.getSubject());

            Number versionClaim =
                    jwt.getClaim(TOKEN_VERSION_CLAIM);

            if (versionClaim == null) {
                return invalidToken(
                        "Token version is missing."
                );
            }

            long tokenVersion =
                    versionClaim.longValue();

            boolean valid = loginRepository
                    .findById(userId)
                    .map(user ->
                            user.getTokenVersion()
                                    == tokenVersion
                    )
                    .orElse(false);

            return valid
                    ? OAuth2TokenValidatorResult.success()
                    : invalidToken(
                    "The access token has been revoked."
            );

        } catch (RuntimeException exception) {
            return invalidToken(
                    "The access token is invalid."
            );
        }
    }

    private OAuth2TokenValidatorResult invalidToken(
            String description
    ) {
        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error(
                        "invalid_token",
                        description,
                        null
                )
        );
    }
}