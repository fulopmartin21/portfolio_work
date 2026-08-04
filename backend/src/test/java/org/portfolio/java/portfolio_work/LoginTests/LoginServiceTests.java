package org.portfolio.java.portfolio_work.LoginTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.portfolio.java.portfolio_work.Entities.Role;
import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedException;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedExceptionSubType;
import org.portfolio.java.portfolio_work.Login.IJwtTokenService;
import org.portfolio.java.portfolio_work.Login.ImpLoginService;
import org.portfolio.java.portfolio_work.Login.LoginRepository;
import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.portfolio.java.portfolio_work.Login.Responses.LoginResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTests
{
        private static final String EMAIL =
                "lajos@lajos.com";

        private static final String RAW_PASSWORD =
                "Password123!";

        @Mock
        private LoginRepository loginRepository;


        @Mock
        private IJwtTokenService jwtTokenService;

        private ImpLoginService loginService;

        @BeforeEach
        void setUp() {
            loginService = new ImpLoginService(
                    loginRepository,
                    jwtTokenService
            );
        }

    @Test
    void login_shouldReturnTokenAndUserData_whenCredentialsAreValid() {
        User user = createUser();
        LoginRequest request = createValidRequest();

        when(loginRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.of(user));


        when(jwtTokenService.generateAccessToken(user))
                .thenReturn("signed.jwt.token");

        when(jwtTokenService.getExpirationSeconds())
                .thenReturn(900L);

        LoginResponse response = loginService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.accessToken())
                .isEqualTo("signed.jwt.token");
        assertThat(response.tokenType())
                .isEqualTo("Bearer");
        assertThat(response.expiresIn())
                .isEqualTo(900L);

        verify(loginRepository)
                .findByEmailIgnoreCase(EMAIL);

        verify(jwtTokenService)
                .generateAccessToken(user);

        verify(jwtTokenService)
                .getExpirationSeconds();

        verifyNoMoreInteractions(
                loginRepository,
                jwtTokenService
        );
    }

    @Test
    void login_shouldThrowUnauthorized_whenEmailDoesNotExist() {
        LoginRequest request = createValidRequest();

        when(loginRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .satisfies(exception -> {
                    UnauthorizedException unauthorizedException =
                            (UnauthorizedException) exception;

                    assertThat(unauthorizedException.getSubType())
                            .isEqualTo(
                                    UnauthorizedExceptionSubType
                                            .INVALID_CREDENTIALS
                            );
                });

        verify(loginRepository)
                .findByEmailIgnoreCase(EMAIL);

        verifyNoInteractions(jwtTokenService);
    }

    @Test
    void login_shouldThrowUnauthorized_whenPasswordDoesNotMatch() {
        User user = createUser();

        LoginRequest request = new LoginRequest(
                EMAIL,
                "WrongPassword123!"
        );

        when(loginRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .satisfies(exception -> {
                    UnauthorizedException unauthorizedException =
                            (UnauthorizedException) exception;

                    assertThat(unauthorizedException.getSubType())
                            .isEqualTo(
                                    UnauthorizedExceptionSubType.INVALID_CREDENTIALS
                            );
                });

        verify(loginRepository)
                .findByEmailIgnoreCase(EMAIL);

        verify(jwtTokenService, never())
                .generateAccessToken(any(User.class));

        verify(jwtTokenService, never())
                .getExpirationSeconds();
    }

    @Test
    void login_shouldNotExposePasswordInResponse() {
        User user = createUser();
        LoginRequest request = createValidRequest();

        when(loginRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.of(user));

        when(jwtTokenService.generateAccessToken(user))
                .thenReturn("signed.jwt.token");

        when(jwtTokenService.getExpirationSeconds())
                .thenReturn(900L);

        LoginResponse response = loginService.login(request);

        assertThat(response.toString())
                .doesNotContain(RAW_PASSWORD)
                .doesNotContain(user.getPassword());
    }

    @Test
    void login_shouldPropagateTokenGenerationFailure() {
        User user = createUser();
        LoginRequest request = createValidRequest();

        when(loginRepository.findByEmailIgnoreCase(EMAIL))
                .thenReturn(Optional.of(user));

        when(jwtTokenService.generateAccessToken(user))
                .thenThrow(
                        new IllegalStateException(
                                "Unable to generate JWT."
                        )
                );

        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Unable to generate JWT.");

        verify(jwtTokenService)
                .generateAccessToken(user);

        verify(jwtTokenService, never())
                .getExpirationSeconds();
    }

    private LoginRequest createValidRequest() {
        return new LoginRequest(
                EMAIL,
                RAW_PASSWORD
        );
    }

    private LoginRequest createInvalidPasswordRequest() {
        return new LoginRequest(
                EMAIL,
                "WrongPassword123!"
        );
    }

    private User createUser() {
        String encodedPassword = BCrypt.hashpw(
                RAW_PASSWORD,
                BCrypt.gensalt()
        );

        return new User(
                UUID.randomUUID(),
                "Lajos",
                "Lajos",
                EMAIL,
                encodedPassword,
                Role.USER
        );
    }
}