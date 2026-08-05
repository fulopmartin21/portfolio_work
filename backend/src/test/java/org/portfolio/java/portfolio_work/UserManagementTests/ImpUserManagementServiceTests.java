package org.portfolio.java.portfolio_work.UserManagementTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.portfolio.java.portfolio_work.Entities.Role;
import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Exceptions.BadRequestException.BadRequestException;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictException;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedException;
import org.portfolio.java.portfolio_work.UserManagement.ImpUserManagementService;
import org.portfolio.java.portfolio_work.UserManagement.UserManagementRepository;
import org.portfolio.java.portfolio_work.UserManagement.Requests.AdminCreateUserRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangeEmailRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangePasswordRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.UpdateNameRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImpUserManagementServiceTests {

    private static final String RAW_PASSWORD = "Password123!";

    @Mock
    private UserManagementRepository repository;

    private ImpUserManagementService service;

    @BeforeEach
    void setUp() {
        service = new ImpUserManagementService(repository);
    }

    @Test
    void updateOwnName_shouldUpdateName_withoutIncrementingTokenVersion() {
        User user = createUser(Role.USER);
        long originalVersion = user.getTokenVersion();

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        service.updateOwnName(
                user.getId(),
                new UpdateNameRequest(" Martin ", " Fulop ")
        );

        assertThat(user.getFirstName()).isEqualTo("Martin");
        assertThat(user.getLastName()).isEqualTo("Fulop");
        assertThat(user.getTokenVersion()).isEqualTo(originalVersion);
    }

    @Test
    void changeOwnEmail_shouldNormalizeEmailAndIncrementTokenVersion() {
        User user = createUser(Role.USER);
        long originalVersion = user.getTokenVersion();

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(repository.existsByEmailIgnoreCase("new.email@example.com"))
                .thenReturn(false);

        service.changeOwnEmail(
                user.getId(),
                new ChangeEmailRequest(
                        " NEW.EMAIL@example.com ",
                        RAW_PASSWORD
                )
        );

        assertThat(user.getEmail())
                .isEqualTo("new.email@example.com");
        assertThat(user.getTokenVersion())
                .isEqualTo(originalVersion + 1);
    }

    @Test
    void changeOwnEmail_shouldRejectCurrentEmail() {
        User user = createUser(Role.USER);

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.changeOwnEmail(
                user.getId(),
                new ChangeEmailRequest(
                        user.getEmail().toUpperCase(),
                        RAW_PASSWORD
                )
        )).isInstanceOf(BadRequestException.class);

        verify(repository, never())
                .existsByEmailIgnoreCase(anyString());
    }

    @Test
    void changeOwnEmail_shouldRejectDuplicateEmail() {
        User user = createUser(Role.USER);

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(repository.existsByEmailIgnoreCase("taken@example.com"))
                .thenReturn(true);

        assertThatThrownBy(() -> service.changeOwnEmail(
                user.getId(),
                new ChangeEmailRequest(
                        "taken@example.com",
                        RAW_PASSWORD
                )
        )).isInstanceOf(ConflictException.class);
    }

    @Test
    void changeOwnEmail_shouldRejectIncorrectCurrentPassword() {
        User user = createUser(Role.USER);

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.changeOwnEmail(
                user.getId(),
                new ChangeEmailRequest(
                        "new@example.com",
                        "WrongPassword123!"
                )
        )).isInstanceOf(UnauthorizedException.class);

        verify(repository, never())
                .existsByEmailIgnoreCase(anyString());
    }

    @Test
    void changeOwnPassword_shouldStoreBcryptHashAndIncrementVersion() {
        User user = createUser(Role.USER);
        long originalVersion = user.getTokenVersion();

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        service.changeOwnPassword(
                user.getId(),
                new ChangePasswordRequest(
                        RAW_PASSWORD,
                        "NewPassword123!",
                        "NewPassword123!"
                )
        );

        assertThat(
                BCrypt.checkpw(
                        "NewPassword123!",
                        user.getPassword()
                )
        ).isTrue();

        assertThat(user.getTokenVersion())
                .isEqualTo(originalVersion + 1);
    }

    @Test
    void changeOwnPassword_shouldRejectConfirmationMismatch() {
        User user = createUser(Role.USER);

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.changeOwnPassword(
                user.getId(),
                new ChangePasswordRequest(
                        RAW_PASSWORD,
                        "NewPassword123!",
                        "OtherPassword123!"
                )
        )).isInstanceOf(BadRequestException.class);
    }

    @Test
    void changeOwnPassword_shouldRejectCurrentPasswordReuse() {
        User user = createUser(Role.USER);

        when(repository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.changeOwnPassword(
                user.getId(),
                new ChangePasswordRequest(
                        RAW_PASSWORD,
                        RAW_PASSWORD,
                        RAW_PASSWORD
                )
        )).isInstanceOf(BadRequestException.class);
    }

    @Test
    void operation_shouldRejectMissingAuthenticatedUser() {
        UUID userId = UUID.randomUUID();

        when(repository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateOwnName(
                userId,
                new UpdateNameRequest("Martin", "Fulop")
        )).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void createUserByAdmin_shouldPersistSelectedRoleAndHashedPassword() {
        AdminCreateUserRequest request =
                new AdminCreateUserRequest(
                        "Managed",
                        "Person",
                        "MANAGED@example.com",
                        RAW_PASSWORD,
                        Role.ADMIN
                );

        when(repository.existsByEmailIgnoreCase("managed@example.com"))
                .thenReturn(false);
        when(repository.saveAndFlush(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.createUserByAdmin(request);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(repository).saveAndFlush(captor.capture());

        User persisted = captor.getValue();

        assertThat(persisted.getRole()).isEqualTo(Role.ADMIN);
        assertThat(persisted.getEmail())
                .isEqualTo("managed@example.com");
        assertThat(
                BCrypt.checkpw(
                        RAW_PASSWORD,
                        persisted.getPassword()
                )
        ).isTrue();
    }

    @Test
    void createUserByAdmin_shouldRejectDuplicateEmail() {
        AdminCreateUserRequest request =
                new AdminCreateUserRequest(
                        "Managed",
                        "Person",
                        "managed@example.com",
                        RAW_PASSWORD,
                        Role.USER
                );

        when(repository.existsByEmailIgnoreCase(request.email()))
                .thenReturn(true);

        assertThatThrownBy(() -> service.createUserByAdmin(request))
                .isInstanceOf(ConflictException.class);

        verify(repository, never()).saveAndFlush(any(User.class));
    }

    private User createUser(Role role) {
        return new User(
                UUID.randomUUID(),
                "Initial",
                "Person",
                "initial@example.com",
                BCrypt.hashpw(RAW_PASSWORD, BCrypt.gensalt()),
                role
        );
    }
}
