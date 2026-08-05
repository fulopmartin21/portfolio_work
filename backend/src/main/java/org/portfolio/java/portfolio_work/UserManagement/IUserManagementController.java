package org.portfolio.java.portfolio_work.UserManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.portfolio.java.portfolio_work.Configuration.OpenApiConfig;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangeEmailRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.ChangePasswordRequest;
import org.portfolio.java.portfolio_work.UserManagement.Requests.UpdateNameRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public interface IUserManagementController {

    @Operation(
            summary = "Updates the authenticated user's name",
            description = """
                    Updates the first and last name of the authenticated user.

                    The user is identified from the JWT subject claim. The
                    request cannot target another user.

                    Name changes do not invalidate the current access token.
                    """
    )
    @ApiResponse(responseCode = "204",
            description = "Name updated successfully.")
    @ApiResponse(responseCode = "400",
            description = "Validation failed.",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401",
            description = "JWT is missing, invalid, expired or revoked.",
            content = @Content(mediaType = "application/json"))
    @PatchMapping("/users/me/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateOwnName(
            @Valid @RequestBody UpdateNameRequest request,
            JwtAuthenticationToken authentication
    );

    @Operation(
            summary = "Changes the authenticated user's email address",
            description = """
                    Verifies the current password and changes the email address.

                    The new email address must be unique and different from the
                    current email address.

                    The operation increments tokenVersion and immediately
                    invalidates all previously issued access tokens.
                    A new login is required after success.
                    """
    )
    @ApiResponse(responseCode = "204",
            description = "Email address changed successfully.")
    @ApiResponse(responseCode = "400",
            description = "Validation failed or the new email equals the current email.",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401",
            description = "JWT or current password is invalid.",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "409",
            description = "The new email address is already in use.",
            content = @Content(mediaType = "application/json"))
    @PatchMapping("/users/me/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changeOwnEmail(
            @Valid @RequestBody ChangeEmailRequest request,
            JwtAuthenticationToken authentication
    );

    @Operation(
            summary = "Changes the authenticated user's password",
            description = """
                    Verifies the current password and stores a new BCrypt hash.

                    The new password and confirmation must match, and the new
                    password must differ from the current password.

                    The operation increments tokenVersion and immediately
                    invalidates all previously issued access tokens.
                    A new login is required after success.
                    """
    )
    @ApiResponse(responseCode = "204",
            description = "Password changed successfully.")
    @ApiResponse(responseCode = "400",
            description = """
                    Validation failed. Possible reasons:
                    - New password confirmation does not match
                    - New password equals the current password
                    - New password does not satisfy password rules
                    """,
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401",
            description = "JWT or current password is invalid.",
            content = @Content(mediaType = "application/json"))
    @PatchMapping("/users/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changeOwnPassword(
            @Valid @RequestBody ChangePasswordRequest request,
            JwtAuthenticationToken authentication
    );
}
