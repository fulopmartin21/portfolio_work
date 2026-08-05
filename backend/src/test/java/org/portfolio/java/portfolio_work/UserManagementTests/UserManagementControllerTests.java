package org.portfolio.java.portfolio_work.UserManagementTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.portfolio.java.portfolio_work.UserManagement.ImpUserManagementController;
import org.portfolio.java.portfolio_work.UserManagement.IUserManagementService;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangeEmailRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangePasswordRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.UpdateNameRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserManagementControllerTests {

    @Mock
    private IUserManagementService service;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(
                        new ImpUserManagementController(service)
                )
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void updateOwnName_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        UpdateNameRequest request =
                new UpdateNameRequest("Martin", "Fulop");

        mockMvc.perform(
                        patch("/users/me/name")
                                .principal(authentication(userId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNoContent());

        verify(service).updateOwnName(userId, request);
    }

    @Test
    void changeOwnEmail_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangeEmailRequest request =
                new ChangeEmailRequest(
                        "new@example.com",
                        RAW_PASSWORD
                );

        mockMvc.perform(
                        patch("/users/me/email")
                                .principal(authentication(userId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNoContent());

        verify(service).changeOwnEmail(userId, request);
    }

    @Test
    void changeOwnPassword_shouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        ChangePasswordRequest request =
                new ChangePasswordRequest(
                        RAW_PASSWORD,
                        "NewPassword123!",
                        "NewPassword123!"
                );

        mockMvc.perform(
                        patch("/users/me/password")
                                .principal(authentication(userId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNoContent());

        verify(service).changeOwnPassword(userId, request);
    }

    @Test
    void updateOwnName_shouldReturnBadRequest_whenInputIsInvalid()
            throws Exception {

        mockMvc.perform(
                        patch("/users/me/name")
                                .principal(authentication(UUID.randomUUID()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "",
                                          "lastName": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    private static final String RAW_PASSWORD = "Password123!";

    private JwtAuthenticationToken authentication(UUID userId) {
        Instant now = Instant.now();

        Jwt jwt = new Jwt(
                "token",
                now,
                now.plusSeconds(900),
                Map.of("alg", "RS256", "typ", "JWT"),
                Map.of(
                        "sub", userId.toString(),
                        "roles", List.of("USER"),
                        "tokenVersion", 0L
                )
        );

        return new JwtAuthenticationToken(jwt);
    }
}
