package org.portfolio.java.portfolio_work;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictException;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictExceptionSubType;
import org.portfolio.java.portfolio_work.Registration.ImpRegistrationService;
import org.portfolio.java.portfolio_work.Registration.RegistrationRepository;
import org.portfolio.java.portfolio_work.Registration.Requests.CreateUserRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationTests
{

    @Mock
    private RegistrationRepository registrationRepository;

    private ImpRegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService =
                new ImpRegistrationService(registrationRepository);
    }

    @Test
    void createUser_shouldSaveAndReturnUser_whenEmailDoesNotExist() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Martin",
                "Fulop",
                "martin@example.com",
                "Password123!"
        );

        User user = new User(request);

        User savedUser = new User(
                UUID.randomUUID(),
                "Martin",
                "Fulop",
                "martin@example.com",
                user.getPassword()
        );

        when(registrationRepository.existsByEmailIgnoreCase(
                "martin@example.com"
        )).thenReturn(false);

        when(registrationRepository.save(user))
                .thenReturn(savedUser);

        // Act
        User result = registrationService.createUser(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedUser.getId());
        assertThat(result.getFirstName()).isEqualTo("Martin");
        assertThat(result.getLastName()).isEqualTo("Fulop");
        assertThat(result.getEmail()).isEqualTo("martin@example.com");

        verify(registrationRepository)
                .existsByEmailIgnoreCase("martin@example.com");

        verify(registrationRepository).save(user);

        verifyNoMoreInteractions(registrationRepository);
    }

    @Test
    void createUser_shouldThrowConflictException_whenEmailAlreadyExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Martin",
                "Fulop",
                "martin@example.com",
                "Password123!"
        );

        User user = new User(request);

        when(registrationRepository.existsByEmailIgnoreCase(
                "martin@example.com"
        )).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> registrationService.createUser(user))
                .isInstanceOf(ConflictException.class)
                .satisfies(exception -> {
                    ConflictException conflictException =
                            (ConflictException) exception;

                    assertThat(conflictException.getSubType())
                            .isEqualTo(
                                    ConflictExceptionSubType.EMAIL_ALREADY_EXISTS
                            );

                    assertThat(conflictException.getMessage())
                            .isEqualTo(
                                    ConflictExceptionSubType.EMAIL_ALREADY_EXISTS
                                            .getMessage()
                            );
                });

        verify(registrationRepository)
                .existsByEmailIgnoreCase("martin@example.com");

        verify(registrationRepository, never())
                .save(any(User.class));

        verifyNoMoreInteractions(registrationRepository);
    }
}