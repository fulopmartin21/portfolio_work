package org.portfolio.java.portfolio_work.Exceptions.ConflictException;

import lombok.Getter;
import org.portfolio.java.portfolio_work.Exceptions.ApiErrorSubType;

@Getter
public enum ConflictExceptionSubType implements ApiErrorSubType {

    CONFLICT(
            1,
            "The request conflicts with the current state of the resource."
    ),

    EMAIL_ALREADY_EXISTS(
            2,
            "A user with the provided email address already exists."
    ),

    USERNAME_ALREADY_EXISTS(
            3,
            "A user with the provided username already exists."
    );

    private final int number;
    private final String message;

    ConflictExceptionSubType(
            int number,
            String message
    ) {
        this.number = number;
        this.message = message;
    }
}