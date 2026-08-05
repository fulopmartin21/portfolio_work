package org.portfolio.java.portfolio_work.UserManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.portfolio.java.portfolio_work.Configuration.OpenApiConfig;
import org.portfolio.java.portfolio_work.UserManagement.Requests.AdminCreateUserRequest;
import org.portfolio.java.portfolio_work.UserManagement.Responses.AdminCreatedUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public interface IAdminUserController {

    @Operation(
            summary = "Creates a managed user account",
            description = """
                    Creates a new USER or ADMIN account.

                    This endpoint requires the ADMIN role. Unlike public
                    registration, the administrator may select the role assigned
                    to the newly created user.
                    """
    )
    @ApiResponse(
            responseCode = "201",
            description = "Managed user created successfully.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = AdminCreatedUserResponse.class
                    )
            )
    )
    @ApiResponse(responseCode = "400",
            description = "Validation failed or the role is invalid.",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401",
            description = "JWT is missing, invalid, expired or revoked.",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403",
            description = "The authenticated user does not have the ADMIN role.",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "409",
            description = "The email address is already in use.",
            content = @Content(mediaType = "application/json"))
    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    AdminCreatedUserResponse createUser(
            @Valid @RequestBody AdminCreateUserRequest request
    );
}
