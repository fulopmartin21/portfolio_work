package org.portfolio.java.portfolio_work.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String errorCode,
        int subType,
        String message,
        String path,
        Map<String, String> validationErrors
) {
}