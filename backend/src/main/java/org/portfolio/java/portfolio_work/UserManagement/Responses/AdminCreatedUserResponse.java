package org.portfolio.java.portfolio_work.UserManagement.Responses;

import io.swagger.v3.oas.annotations.media.Schema;
import org.portfolio.java.portfolio_work.Entities.Role;
import org.portfolio.java.portfolio_work.Entities.User;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Public information about an administrator-created user.")
public record AdminCreatedUserResponse(

        @Schema(example = "c94eef82-74da-470e-920b-3ca32f15bfe0")
        UUID id,

        @Schema(example = "Managed")
        String firstName,

        @Schema(example = "Person")
        String lastName,

        @Schema(example = "managed.user@example.com")
        String email,

        @Schema(example = "USER")
        Role role,

        Instant createdAt,

        @Schema(example = "administrator-user-id")
        String createdBy
) {
    public static AdminCreatedUserResponse from(User user) {
        return new AdminCreatedUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getCreatedBy()
        );
    }
}
