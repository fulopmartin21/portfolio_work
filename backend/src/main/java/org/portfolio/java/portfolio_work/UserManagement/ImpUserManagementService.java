package org.portfolio.java.portfolio_work.UserManagement;

import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Exceptions.BadRequestException.BadRequestException;
import org.portfolio.java.portfolio_work.Exceptions.BadRequestException.BadRequestExceptionSubType;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictException;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictExceptionSubType;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedException;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedExceptionSubType;
import org.portfolio.java.portfolio_work.UserManagement.Requests.AdminCreateUserRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangeEmailRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangePasswordRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.UpdateNameRequest;
import org.portfolio.java.portfolio_work.UserManagement.Responses.AdminCreatedUserResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
public class ImpUserManagementService implements IUserManagementService {

    private final UserManagementRepository repository;

    public ImpUserManagementService(UserManagementRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void updateOwnName(UUID userId, UpdateNameRequest request) {
        User user = findAuthenticatedUser(userId);

        user.updateName(
                request.firstName().trim(),
                request.lastName().trim()
        );
    }

    @Override
    @Transactional
    public void changeOwnEmail(UUID userId, ChangeEmailRequest request) {
        User user = findAuthenticatedUser(userId);

        verifyCurrentPassword(request.currentPassword(), user);

        String normalizedEmail =
                request.newEmail().trim().toLowerCase(Locale.ROOT);

        if (user.getEmail().equalsIgnoreCase(normalizedEmail)) {
            throw new BadRequestException(
                    BadRequestExceptionSubType.NEW_EMAIL_EQUALS_CURRENT_EMAIL
            );
        }

        if (repository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ConflictException(
                    ConflictExceptionSubType.EMAIL_ALREADY_EXISTS
            );
        }

        user.changeEmail(normalizedEmail);
    }

    @Override
    @Transactional
    public void changeOwnPassword(
            UUID userId,
            ChangePasswordRequest request
    ) {
        User user = findAuthenticatedUser(userId);

        verifyCurrentPassword(request.currentPassword(), user);

        if (!request.newPassword()
                .equals(request.newPasswordConfirmation())) {
            throw new BadRequestException(
                    BadRequestExceptionSubType.PASSWORD_CONFIRMATION_MISMATCH
            );
        }

        if (BCrypt.checkpw(request.newPassword(), user.getPassword())) {
            throw new BadRequestException(
                    BadRequestExceptionSubType.NEW_PASSWORD_EQUALS_CURRENT_PASSWORD
            );
        }

        user.changePassword(
                BCrypt.hashpw(
                        request.newPassword(),
                        BCrypt.gensalt()
                )
        );
    }

    @Override
    @Transactional
    public AdminCreatedUserResponse createUserByAdmin(AdminCreateUserRequest request) {
        String normalizedEmail =
                request.email().trim().toLowerCase(Locale.ROOT);

        if (repository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ConflictException(
                    ConflictExceptionSubType.EMAIL_ALREADY_EXISTS
            );
        }

        User user = new User(
                request.firstName().trim(),
                request.lastName().trim(),
                normalizedEmail,
                request.password(),
                request.role()
        );

        User savedUser = repository.saveAndFlush(user);

        return AdminCreatedUserResponse.from(savedUser);
    }

    private User findAuthenticatedUser(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException(
                        UnauthorizedExceptionSubType.INVALID_CREDENTIALS
                ));
    }

    private void verifyCurrentPassword(
            String currentPassword,
            User user
    ) {
        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            throw new UnauthorizedException(
                    UnauthorizedExceptionSubType.INVALID_CREDENTIALS
            );
        }
    }
}
