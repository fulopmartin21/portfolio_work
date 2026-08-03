package org.portfolio.java.portfolio_work.Login.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Credentials required for user authentication.")
public class LoginRequest {

    @Schema(
            description = "Registered user's e-mail address.",
            example = "lajos@lajos.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email address is required.")
    @Email(message = "Email address must have a valid format.")
    @Size(
            min = 5,
            max = 100,
            message = "Email address must contain between 5 and 100 characters."
    )
    private String email;

    @Schema(
            description = "User's raw password.",
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
    private String password;
}