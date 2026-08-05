package org.portfolio.java.portfolio_work.Exceptions.BadRequestException;
import lombok.Getter;
import org.portfolio.java.portfolio_work.Exceptions.ApiErrorSubType;

@Getter
public enum BadRequestExceptionSubType implements ApiErrorSubType {

    BAD_REQUEST(
            1,
            "Bad request."
    ),

    INVALID_USERNAME(
            2,
            "The provided username is invalid."
    ),

    INVALID_EMAIL(
            3,
            "The provided email address is invalid."
    ),

    INVALID_PASSWORD(
            4,
            "The provided password does not meet the requirements."
    ),

    VALIDATION_FAILED(
            5,
            "One or more request fields are invalid."
    ),

    MALFORMED_REQUEST_BODY(
            6,
            "The request body is malformed."
    ),
    NEW_EMAIL_EQUALS_CURRENT_EMAIL(
        7,
            "The new email address must differ from the current email address."
            ),

    PASSWORD_CONFIRMATION_MISMATCH(
        8,
        "The new password and confirmation do not match."
    ),

    NEW_PASSWORD_EQUALS_CURRENT_PASSWORD(
        9,
        "The new password must differ from the current password."
    );

    private final int number;
    private final String message;

    BadRequestExceptionSubType(
            int number,
            String message
    ) {
        this.number = number;
        this.message = message;
    }
}