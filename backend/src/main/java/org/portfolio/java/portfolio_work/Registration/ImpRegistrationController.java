package org.portfolio.java.portfolio_work.Registration;

import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Registration.Requests.CreateUserRequest;
import org.portfolio.java.portfolio_work.Registration.Responses.CreateUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImpRegistrationController implements IRegistrationController
{
    private final IRegistrationService registrationService;

    @Autowired
    public ImpRegistrationController(IRegistrationService registrationService)
    {
        this.registrationService = registrationService;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        return new CreateUserResponse(registrationService.createUser(new User(request)));
    }
}
