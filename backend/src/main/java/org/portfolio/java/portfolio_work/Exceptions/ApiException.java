package org.portfolio.java.portfolio_work.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ApiErrorSubType subType;

    protected ApiException(
            HttpStatus httpStatus,
            ApiErrorSubType subType
    ) {
        super(subType.getMessage());
        this.httpStatus = httpStatus;
        this.subType = subType;
    }

    protected ApiException(
            HttpStatus httpStatus,
            ApiErrorSubType subType,
            Throwable cause
    ) {
        super(subType.getMessage(), cause);
        this.httpStatus = httpStatus;
        this.subType = subType;
    }
}