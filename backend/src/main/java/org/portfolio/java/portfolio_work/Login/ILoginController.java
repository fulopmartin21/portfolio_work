package org.portfolio.java.portfolio_work.Login;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.portfolio.java.portfolio_work.Exceptions.ApiErrorResponse;
import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.portfolio.java.portfolio_work.Login.Responses.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ILoginController
{
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Operation(
            summary = "Authenticates a registered user",
            description = """
                Authenticates a registered user using an e-mail address
                and password.

                If the credentials are valid, the endpoint generates and
                returns an RSA-signed JWT access token.

                The access token can be used to access protected endpoints
                through the Authorization header using the Bearer scheme.
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = """
                Authentication completed successfully.

                Returns:
                - JWT access token
                - Bearer token type
                - Token lifetime in seconds
                - Authenticated user's ID
                - Authenticated user's e-mail address
                """,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = LoginResponse.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = """
                Validation failed. Possible reasons:
                - Email address is missing
                - Email pattern is invalid
                - Email address is not between 5 and 100 characters
                - Password is missing
                - Password is not between 8 and 20 characters
                """,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = ApiErrorResponse.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = """
                Authentication failed.

                The supplied email address or password is incorrect.
                The response does not reveal which credential was invalid.
                """,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = ApiErrorResponse.class
                    )
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = """
                An unexpected internal server error occurred while
                authenticating the user or generating the access token.
                """,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = ApiErrorResponse.class
                    )
            )
    )
    public LoginResponse login( @Valid @RequestBody LoginRequest request);

}
