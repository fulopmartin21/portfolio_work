package org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException;

import org.portfolio.java.portfolio_work.Exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

    public UnauthorizedException(
            UnauthorizedExceptionSubType subType
    ) {
        super(HttpStatus.UNAUTHORIZED, subType);
    }

    public UnauthorizedException(
            UnauthorizedExceptionSubType subType,
            Throwable cause
    ) {
        super(HttpStatus.UNAUTHORIZED, subType, cause);
    }
}