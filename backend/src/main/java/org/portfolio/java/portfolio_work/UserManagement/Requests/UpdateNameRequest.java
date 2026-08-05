package org.portfolio.java.portfolio_work.UserManagement.Requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for updating the authenticated user's first and last name.")
public record UpdateNameRequest(

        @Schema(example = "Martin", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "First name is required.")
        @Size(
                min = 2,
                max = 100,
                message = "First name must contain between 2 and 100 characters."
        )
        String firstName,

        @Schema(example = "Fulop", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Last name is required.")
        @Size(
                min = 2,
                max = 100,
                message = "Last name must contain between 2 and 100 characters."
        )
        String lastName
) {
}
