package org.portfolio.java.portfolio_work.UserManagement;

import org.portfolio.java.portfolio_work.UserManagement.Requests.AdminCreateUserRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangeEmailRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangePasswordRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.UpdateNameRequest;
import org.portfolio.java.portfolio_work.UserManagement.Responses.AdminCreatedUserResponse;

import java.util.UUID;

public interface IUserManagementService {

    void updateOwnName(UUID userId, UpdateNameRequest request);

    void changeOwnEmail(UUID userId, ChangeEmailRequest request);

    void changeOwnPassword(UUID userId, ChangePasswordRequest request);

    AdminCreatedUserResponse createUserByAdmin(AdminCreateUserRequest request);
}
