package org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException;

import lombok.Getter;
import org.portfolio.java.portfolio_work.Exceptions.ApiErrorSubType;

@Getter
public enum UnauthorizedExceptionSubType implements ApiErrorSubType {

    UNAUTHORIZED(
            1,
            "Authentication is required."
    ),

    INVALID_CREDENTIALS(
            2,
            "The provided email address or password is incorrect."
    );

    private final int number;
    private final String message;

    UnauthorizedExceptionSubType(
            int number,
            String message
    ) {
        this.number = number;
        this.message = message;
    }
}