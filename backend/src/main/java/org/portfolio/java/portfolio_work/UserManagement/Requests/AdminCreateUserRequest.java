package org.portfolio.java.portfolio_work.UserManagement.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.portfolio.java.portfolio_work.Entities.Role;

@Schema(description = "Administrator request for creating a managed user account.")
public record AdminCreateUserRequest(

        @Schema(example = "Managed", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "First name is required.")
        @Size(
                min = 2,
                max = 100,
                message = "First name must contain between 2 and 100 characters."
        )
        String firstName,

        @Schema(example = "Person", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Last name is required.")
        @Size(
                min = 2,
                max = 100,
                message = "Last name must contain between 2 and 100 characters."
        )
        String lastName,

        @Schema(
                example = "managed.user@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Email address is required.")
        @Email(message = "Email address must have a valid format.")
        @Size(
                min = 5,
                max = 100,
                message = "Email address must contain between 5 and 100 characters."
        )
        String email,

        @Schema(
                example = "Password123!",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password is required.")
        @Size(
                min = 8,
                max = 20,
                message = "Password must contain between 8 and 20 characters."
        )
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
                message = "Password must contain an uppercase letter, lowercase letter, number and special character."
        )
        String password,

        @Schema(
                example = "USER",
                allowableValues = {"USER", "ADMIN"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Role is required.")
        Role role
) {
}
