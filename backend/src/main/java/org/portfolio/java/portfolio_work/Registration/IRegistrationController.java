package org.portfolio.java.portfolio_work.Registration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Creates a new user",
            description = """
                    Creates a new user.
                    
                    The given e-mail address must be unique!
                    
                    Returns the ID of the newly created user.
                    """
    )
    @ApiResponse(
            responseCode = "409",
            description = """
                    The given e-mail address is already in use!
                    """,
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(responseCode = "400",
        description = """
                Validation failed. Possible reasons:
                - Firstname or Lastname is not between 5 and 100 characters
                - Email pattern isn't valid or the e-mail address is not between 5 and 100 characters
                - Password is not between 8 and 20 characters
                """,
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(responseCode = "201",
        description = """
                User created successfully! Return the new user's ID.
                """,
            content = @Content(mediaType = "application/json")
    )
    public CreateUserResponse createUser(@Valid @RequestBody CreateUserRequest request);
}
