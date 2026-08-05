package org.portfolio.java.portfolio_work.UserManagementTests;

import org.junit.jupiter.api.Test;
import org.portfolio.java.portfolio_work.UserManagement.IUserManagementService;
import org.portfolio.java.portfolio_work.UserManagement.Requests.AdminCreateUserRequest;
import org.portfolio.java.portfolio_work.UserManagement.Responses.AdminCreatedUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.time.Instant;
import java.util.UUID;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class UserManagementSecurityIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IUserManagementService service;

    @Test
    void userEndpoint_shouldReturnUnauthorized_withoutJwt()
            throws Exception {

        mockMvc.perform(
                        patch("/users/me/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "Martin",
                                          "lastName": "Fulop"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userEndpoint_shouldAllowUserRole()
            throws Exception {

        mockMvc.perform(
                        patch("/users/me/name")
                                .with(jwt()
                                        .jwt(jwt -> jwt
                                                .subject(UUID.randomUUID().toString())
                                                .claim("tokenVersion", 0L)
                                        )
                                        .authorities(
                                                new SimpleGrantedAuthority("ROLE_USER")
                                        )
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "Martin",
                                          "lastName": "Fulop"
                                        }
                                        """)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void adminEndpoint_shouldReturnForbidden_forUserRole()
            throws Exception {

        mockMvc.perform(
                        post("/admin/users")
                                .with(jwt()
                                        .authorities(
                                                new SimpleGrantedAuthority("ROLE_USER")
                                        )
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "Managed",
                                          "lastName": "Person",
                                          "email": "managed@example.com",
                                          "password": "Password123!",
                                          "role": "USER"
                                        }
                                        """)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoint_shouldAllowAdminRole()
            throws Exception {

        UUID userId = UUID.randomUUID();

        when(service.createUserByAdmin(any(AdminCreateUserRequest.class)))
                .thenReturn(
                        new AdminCreatedUserResponse(
                                userId,
                                "Managed",
                                "Person",
                                "managed@example.com",
                                org.portfolio.java.portfolio_work.Entities.Role.USER,
                                Instant.parse("2026-08-05T10:00:00Z"),
                                "admin-id"
                        )
                );

        mockMvc.perform(
                        post("/admin/users")
                                .with(jwt()
                                        .authorities(
                                                new SimpleGrantedAuthority("ROLE_ADMIN")
                                        )
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "Managed",
                                          "lastName": "Person",
                                          "email": "managed@example.com",
                                          "password": "Password123!",
                                          "role": "USER"
                                        }
                                        """)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void adminEndpoint_shouldReturnUnauthorized_withoutJwt()
            throws Exception {

        mockMvc.perform(
                        post("/admin/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "firstName": "Managed",
                                          "lastName": "Person",
                                          "email": "managed@example.com",
                                          "password": "Password123!",
                                          "role": "USER"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized());
    }
}
