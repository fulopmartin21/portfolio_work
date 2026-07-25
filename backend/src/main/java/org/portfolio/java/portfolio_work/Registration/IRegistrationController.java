package org.portfolio.java.portfolio_work.Registration;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.portfolio.java.portfolio_work.Registration.Requests.CreateUserRequest;
import org.portfolio.java.portfolio_work.Registration.Responses.CreateUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface IRegistrationController
{
    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CreateUserResponse createUser(@Valid @RequestBody CreateUserRequest request);
}
