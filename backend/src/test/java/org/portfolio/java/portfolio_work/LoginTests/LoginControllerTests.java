package org.portfolio.java.portfolio_work.LoginTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.portfolio.java.portfolio_work.Exceptions.GlobalExceptionHandler;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedException;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedExceptionSubType;
import org.portfolio.java.portfolio_work.Login.ILoginService;
import org.portfolio.java.portfolio_work.Login.ImpLoginController;
import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.portfolio.java.portfolio_work.Login.Responses.LoginResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTests {

    @Mock
    private ILoginService loginService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ImpLoginController controller =
                new ImpLoginController(loginService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(
                        new GlobalExceptionHandler()
                )
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void login_shouldReturnOkAndJwtResponse_whenCredentialsAreValid()
            throws Exception {

        UUID userId = UUID.randomUUID();

        LoginRequest request = new LoginRequest(
                "lajos@lajos.com",
                "Password123!"
        );

        LoginResponse response = new LoginResponse(
                "signed.jwt.token",
                "Bearer",
                900L
        );

        when(loginService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(
                                MediaType.APPLICATION_JSON
                        )
                )
                .andExpect(
                        jsonPath("$.accessToken")
                                .value("signed.jwt.token")
                )
                .andExpect(
                        jsonPath("$.tokenType")
                                .value("Bearer")
                )
                .andExpect(
                        jsonPath("$.expiresIn")
                                .value(900)
                )
                .andExpect(
                        jsonPath("$.password").doesNotExist()
                )
                .andExpect(
                        jsonPath("$.user.password").doesNotExist()
                );

        verify(loginService)
                .login(any(LoginRequest.class));

        verifyNoMoreInteractions(loginService);
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsAreInvalid()
            throws Exception {

        LoginRequest request = new LoginRequest(
                "lajos@lajos.com",
                "WrongPassword123!"
        );

        when(loginService.login(any(LoginRequest.class)))
                .thenThrow(
                        new UnauthorizedException(
                                UnauthorizedExceptionSubType
                                        .INVALID_CREDENTIALS
                        )
                );

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isUnauthorized())
                .andExpect(
                        jsonPath("$.status").value(401)
                )
                .andExpect(
                        jsonPath("$.error").value("Unauthorized")
                )
                .andExpect(
                        jsonPath("$.subType").value(
                                UnauthorizedExceptionSubType
                                        .INVALID_CREDENTIALS
                                        .getNumber()
                        )
                )
                .andExpect(
                        jsonPath("$.message").value(
                                UnauthorizedExceptionSubType
                                        .INVALID_CREDENTIALS
                                        .getMessage()
                        )
                )
                .andExpect(
                        jsonPath("$.path").value("/login")
                );

        verify(loginService)
                .login(any(LoginRequest.class));
    }

    @Test
    void login_shouldReturnBadRequest_whenEmailIsInvalid()
            throws Exception {

        LoginRequest request = new LoginRequest(
                "invalid-email",
                "Password123!"
        );

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status").value(400)
                )
                .andExpect(
                        jsonPath("$.validationErrors.email").exists()
                );

        verifyNoInteractions(loginService);
    }

    @Test
    void login_shouldReturnBadRequest_whenEmailIsBlank()
            throws Exception {

        LoginRequest request = new LoginRequest(
                "",
                "Password123!"
        );

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.validationErrors.email").exists()
                );

        verifyNoInteractions(loginService);
    }

    @Test
    void login_shouldReturnBadRequest_whenPasswordIsBlank()
            throws Exception {

        LoginRequest request = new LoginRequest(
                "lajos@lajos.com",
                ""
        );

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.validationErrors.password").exists()
                );

        verifyNoInteractions(loginService);
    }

    @Test
    void login_shouldReturnBadRequest_whenPasswordIsTooShort()
            throws Exception {

        LoginRequest request = new LoginRequest(
                "lajos@lajos.com",
                "Short1!"
        );

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.validationErrors.password").exists()
                );

        verifyNoInteractions(loginService);
    }

    @Test
    void login_shouldReturnBadRequest_whenPasswordIsTooLong()
            throws Exception {

        LoginRequest request = new LoginRequest(
                "lajos@lajos.com",
                "Password123456789012345!"
        );

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.validationErrors.password").exists()
                );

        verifyNoInteractions(loginService);
    }

    @Test
    void login_shouldReturnBadRequest_whenBodyIsMalformed()
            throws Exception {

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "lajos@lajos.com",
                                          "password":
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loginService);
    }

    @Test
    void login_shouldReturnBadRequest_whenBodyIsEmpty()
            throws Exception {

        mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loginService);
    }
}