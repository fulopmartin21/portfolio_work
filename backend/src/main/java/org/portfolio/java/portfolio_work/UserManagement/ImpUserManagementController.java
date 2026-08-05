package org.portfolio.java.portfolio_work.UserManagement;

import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangeEmailRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangePasswordRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.UpdateNameRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ImpUserManagementController
        implements IUserManagementController {

    private final IUserManagementService service;

    public ImpUserManagementController(IUserManagementService service) {
        this.service = service;
    }

    @Override
    public void updateOwnName(
            UpdateNameRequest request,
            JwtAuthenticationToken authentication
    ) {
        service.updateOwnName(extractUserId(authentication), request);
    }

    @Override
    public void changeOwnEmail(
            ChangeEmailRequest request,
            JwtAuthenticationToken authentication
    ) {
        service.changeOwnEmail(extractUserId(authentication), request);
    }

    @Override
    public void changeOwnPassword(
            ChangePasswordRequest request,
            JwtAuthenticationToken authentication
    ) {
        service.changeOwnPassword(extractUserId(authentication), request);
    }

    private UUID extractUserId(JwtAuthenticationToken authentication) {
        return UUID.fromString(authentication.getToken().getSubject());
    }
}
