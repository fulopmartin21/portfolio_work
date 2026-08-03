package org.portfolio.java.portfolio_work.Login.Responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Successful JWT authentication response.")
public record LoginResponse(

        @Schema(
                description = "RSA-signed JWT access token.",
                example = "eyJhbGciOiJSUzI1NiJ9..."
        )
        String accessToken,

        @Schema(
                description = "Authentication scheme used with the token.",
                example = "Bearer"
        )
        String tokenType,

        @Schema(
                description = "Access-token lifetime in seconds.",
                example = "900"
        )
        long expiresIn,

        @Schema(
                description = "Public information about the authenticated user."
        )
        LoginUserResponse user
) {
}