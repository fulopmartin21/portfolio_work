package org.portfolio.java.portfolio_work.Exceptions.InternalServerErrorException;

import lombok.Getter;
import org.portfolio.java.portfolio_work.Exceptions.ApiErrorSubType;

@Getter
public enum InternalServerErrorExceptionSubType implements ApiErrorSubType {

    INTERNAL_SERVER_ERROR(
            1,
            "An unexpected internal server error occurred."
    ),

    DATABASE_ERROR(
            2,
            "An error occurred while accessing the database."
    ),

    USER_CREATION_FAILED(
            3,
            "The user could not be created."
    ),

    PASSWORD_HASHING_FAILED(
            4,
            "The password could not be securely processed."
    );

    private final int number;
    private final String message;

    InternalServerErrorExceptionSubType(
            int number,
            String message
    ) {
        this.number = number;
        this.message = message;
    }
}