package org.portfolio.java.portfolio_work.UserManagement.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for changing the authenticated user's email address.")
public record ChangeEmailRequest(

        @Schema(
                example = "new.email@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "New email address is required.")
        @Email(message = "New email address must have a valid format.")
        @Size(
                min = 5,
                max = 100,
                message = "Email address must contain between 5 and 100 characters."
        )
        String newEmail,

        @Schema(
                example = "Password123!",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Current password is required.")
        String currentPassword
) {
}
