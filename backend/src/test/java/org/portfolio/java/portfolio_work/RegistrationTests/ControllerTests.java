package org.portfolio.java.portfolio_work.RegistrationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictException;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictExceptionSubType;
import org.portfolio.java.portfolio_work.Exceptions.GlobalExceptionHandler;
import org.portfolio.java.portfolio_work.Registration.IRegistrationService;
import org.portfolio.java.portfolio_work.Registration.ImpRegistrationController;
import org.portfolio.java.portfolio_work.Registration.Requests.CreateUserRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ControllerTests {

    @Mock
    private IRegistrationService registrationService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ImpRegistrationController controller =
                new ImpRegistrationController(registrationService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void createUser_shouldReturnCreated_whenRequestIsValid()
            throws Exception {

        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Martin",
                "Fulop",
                "martin@example.com",
                "Password123!"
        );

        UUID userId = UUID.randomUUID();

        User savedUser = new User(
                userId,
                "Martin",
                "Fulop",
                "martin@example.com",
                "$2a$10$hashedPassword"
        );

        when(registrationService.createUser(any(User.class)))
                .thenReturn(savedUser);

        // Act + Assert
        mockMvc.perform(
                        post("/create-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        verify(registrationService)
                .createUser(any(User.class));

        verifyNoMoreInteractions(registrationService);
    }

    @Test
    void createUser_shouldReturnBadRequest_whenRequestIsInvalid()
            throws Exception {

        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "",
                "",
                "invalid-email",
                "weak"
        );

        // Act + Assert
        mockMvc.perform(
                        post("/create-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.subType").value(
                        /* VALIDATION_FAILED subtype száma */ 5
                ))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/create-user"))
                .andExpect(jsonPath("$.validationErrors").exists());

        verifyNoInteractions(registrationService);
    }

    @Test
    void createUser_shouldReturnConflict_whenEmailAlreadyExists()
            throws Exception {

        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Martin",
                "Fulop",
                "martin@example.com",
                "Password123!"
        );

        when(registrationService.createUser(any(User.class)))
                .thenThrow(
                        new ConflictException(
                                ConflictExceptionSubType.EMAIL_ALREADY_EXISTS
                        )
                );

        // Act + Assert
        mockMvc.perform(
                        post("/create-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.subType").value(
                        ConflictExceptionSubType.EMAIL_ALREADY_EXISTS
                                .getNumber()
                ))
                .andExpect(jsonPath("$.message").value(
                        ConflictExceptionSubType.EMAIL_ALREADY_EXISTS
                                .getMessage()
                ))
                .andExpect(jsonPath("$.path").value("/create-user"));

        verify(registrationService)
                .createUser(any(User.class));

        verifyNoMoreInteractions(registrationService);
    }
}