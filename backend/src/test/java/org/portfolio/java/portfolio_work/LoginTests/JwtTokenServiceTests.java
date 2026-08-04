package org.portfolio.java.portfolio_work.LoginTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.portfolio.java.portfolio_work.Entities.Role;
import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Login.ImpJwtTokenService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTests {

    @Mock
    private JwtEncoder jwtEncoder;

    private ImpJwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new ImpJwtTokenService(
                jwtEncoder,
                "https://portfolio-work.local",
                900L
        );
    }

    @Test
    void generateAccessToken_shouldReturnEncodedToken() {
        User user = createUser();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(createJwt());

        String token =
                jwtTokenService.generateAccessToken(user);

        assertThat(token).isEqualTo("signed.jwt.token");

        verify(jwtEncoder)
                .encode(any(JwtEncoderParameters.class));

        verifyNoMoreInteractions(jwtEncoder);
    }

    @Test
    void generateAccessToken_shouldCreateCorrectClaims() {
        User user = createUser();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(createJwt());

        ArgumentCaptor<JwtEncoderParameters> captor =
                ArgumentCaptor.forClass(
                        JwtEncoderParameters.class
                );

        jwtTokenService.generateAccessToken(user);

        verify(jwtEncoder).encode(captor.capture());

        JwtEncoderParameters parameters = captor.getValue();

        assertThat(
                parameters.getClaims().getIssuer().toString()
        ).isEqualTo("https://portfolio-work.local");

        assertThat(
                parameters.getClaims()
                        .getClaims()
                        .get("email")
        ).isEqualTo(user.getEmail());

        assertThat(
                parameters.getClaims()
                        .getClaims()
                        .get("roles")
        ).isEqualTo(List.of("USER"));

        assertThat(parameters.getClaims().getIssuedAt())
                .isNotNull();

        assertThat(parameters.getClaims().getExpiresAt())
                .isNotNull();
    }

    @Test
    void generateAccessToken_shouldUseConfiguredExpiration() {
        User user = createUser();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(createJwt());

        ArgumentCaptor<JwtEncoderParameters> captor =
                ArgumentCaptor.forClass(
                        JwtEncoderParameters.class
                );

        jwtTokenService.generateAccessToken(user);

        verify(jwtEncoder).encode(captor.capture());

        Instant issuedAt =
                captor.getValue()
                        .getClaims()
                        .getIssuedAt();

        Instant expiresAt =
                captor.getValue()
                        .getClaims()
                        .getExpiresAt();

        assertThat(issuedAt).isNotNull();
        assertThat(expiresAt).isNotNull();

        assertThat(
                expiresAt.getEpochSecond()
                        - issuedAt.getEpochSecond()
        ).isEqualTo(900L);
    }

    @Test
    void generateAccessToken_shouldUseRs256AndJwtHeader() {
        User user = createUser();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(createJwt());

        ArgumentCaptor<JwtEncoderParameters> captor =
                ArgumentCaptor.forClass(
                        JwtEncoderParameters.class
                );

        jwtTokenService.generateAccessToken(user);

        verify(jwtEncoder).encode(captor.capture());

        assertThat(
                captor.getValue()
                        .getJwsHeader()
                        .getAlgorithm()
                        .getName()
        ).isEqualTo("RS256");

        assertThat(
                captor.getValue()
                        .getJwsHeader()
                        .getType()
        ).isEqualTo("JWT");
    }

    @Test
    void getExpirationSeconds_shouldReturnConfiguredExpiration() {
        assertThat(jwtTokenService.getExpirationSeconds())
                .isEqualTo(900L);
    }

    private User createUser() {
        return new User(
                UUID.randomUUID(),
                "Lajos",
                "Lajos",
                "lajos@lajos.com",
                "$2a$10$encodedPassword",
                Role.USER
        );
    }

    private Jwt createJwt() {
        Instant now = Instant.now();

        return new Jwt(
                "signed.jwt.token",
                now,
                now.plusSeconds(900),
                Map.of(
                        "alg", "RS256",
                        "typ", "JWT"
                ),
                Map.of(
                        "sub", UUID.randomUUID().toString(),
                        "email", "lajos@lajos.com"
                )
        );
    }
}
