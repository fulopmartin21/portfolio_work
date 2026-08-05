package org.portfolio.java.portfolio_work.UserManagement.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for changing the authenticated user's password.")
public record ChangePasswordRequest(

        @Schema(
                example = "Password123!",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Current password is required.")
        String currentPassword,

        @Schema(
                example = "NewPassword123!",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "New password is required.")
        @Size(
                min = 8,
                max = 20,
                message = "New password must contain between 8 and 20 characters."
        )
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
                message = "New password must contain an uppercase letter, lowercase letter, number and special character."
        )
        String newPassword,

        @Schema(
                example = "NewPassword123!",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "New password confirmation is required.")
        String newPasswordConfirmation
) {
}
