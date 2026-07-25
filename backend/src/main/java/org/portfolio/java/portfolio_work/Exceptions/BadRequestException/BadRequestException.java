package org.portfolio.java.portfolio_work.Exceptions.BadRequestException;

import org.portfolio.java.portfolio_work.Exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {

    public BadRequestException(
            BadRequestExceptionSubType subType
    ) {
        super(HttpStatus.BAD_REQUEST, subType);
    }

    public BadRequestException(
            BadRequestExceptionSubType subType,
            Throwable cause
    ) {
        super(HttpStatus.BAD_REQUEST, subType, cause);
    }
}