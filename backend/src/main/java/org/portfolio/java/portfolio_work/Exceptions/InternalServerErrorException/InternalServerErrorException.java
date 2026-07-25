package org.portfolio.java.portfolio_work.Exceptions.InternalServerErrorException;

import org.portfolio.java.portfolio_work.Exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApiException {

    public InternalServerErrorException(
            InternalServerErrorExceptionSubType subType
    ) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, subType);
    }

    public InternalServerErrorException(
            InternalServerErrorExceptionSubType subType,
            Throwable cause
    ) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                subType,
                cause
        );
    }
}