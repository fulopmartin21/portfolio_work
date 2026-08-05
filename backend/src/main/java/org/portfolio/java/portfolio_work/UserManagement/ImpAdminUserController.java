package org.portfolio.java.portfolio_work.UserManagement;

import org.portfolio.java.portfolio_work.UserManagement.Requests.AdminCreateUserRequest;
import org.portfolio.java.portfolio_work.UserManagement.Responses.AdminCreatedUserResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImpAdminUserController implements IAdminUserController {

    private final IUserManagementService service;

    public ImpAdminUserController(IUserManagementService service) {
        this.service = service;
    }

    @Override
    public AdminCreatedUserResponse createUser(
            AdminCreateUserRequest request
    ) {
        return service.createUserByAdmin(request);
    }
}
