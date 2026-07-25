package org.portfolio.java.portfolio_work.Exceptions.ConflictException;

import org.portfolio.java.portfolio_work.Exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {

    public ConflictException(
            ConflictExceptionSubType subType
    ) {
        super(HttpStatus.CONFLICT, subType);
    }

    public ConflictException(
            ConflictExceptionSubType subType,
            Throwable cause
    ) {
        super(HttpStatus.CONFLICT, subType, cause);
    }
}