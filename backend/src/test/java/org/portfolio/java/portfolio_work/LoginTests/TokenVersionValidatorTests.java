package org.portfolio.java.portfolio_work.LoginTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Login.LoginRepository;
import org.portfolio.java.portfolio_work.Login.TokenVersionValidator;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenVersionValidatorTests {

    @Mock
    private LoginRepository loginRepository;

    private TokenVersionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TokenVersionValidator(
                loginRepository
        );
    }

    @Test
    void validate_shouldSucceed_whenVersionsMatch() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(user.getTokenVersion())
                .thenReturn(3L);

        when(loginRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThat(
                validator.validate(
                        createJwt(
                                userId.toString(),
                                3L
                        )
                ).hasErrors()
        ).isFalse();
    }

    @Test
    void validate_shouldFail_whenVersionsDiffer() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);

        when(user.getTokenVersion())
                .thenReturn(4L);

        when(loginRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThat(
                validator.validate(
                        createJwt(
                                userId.toString(),
                                3L
                        )
                ).hasErrors()
        ).isTrue();
    }

    @Test
    void validate_shouldFail_whenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(loginRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThat(
                validator.validate(
                        createJwt(
                                userId.toString(),
                                0L
                        )
                ).hasErrors()
        ).isTrue();
    }

    @Test
    void validate_shouldFail_whenVersionClaimIsMissing() {
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
                        "sub",
                        UUID.randomUUID().toString()
                )
        );

        assertThat(
                validator.validate(jwt).hasErrors()
        ).isTrue();
    }

    @Test
    void validate_shouldFail_whenSubjectIsInvalid() {
        assertThat(
                validator.validate(
                        createJwt(
                                "invalid-subject",
                                0L
                        )
                ).hasErrors()
        ).isTrue();
    }

    private Jwt createJwt(
            String subject,
            long tokenVersion
    ) {
        Instant now = Instant.now();

        return new Jwt(
                "token",
                now,
                now.plusSeconds(900),
                Map.of(
                        "alg", "RS256",
                        "typ", "JWT"
                ),
                Map.of(
                        "sub", subject,
                        TokenVersionValidator
                                .TOKEN_VERSION_CLAIM,
                        tokenVersion
                )
        );
    }
}