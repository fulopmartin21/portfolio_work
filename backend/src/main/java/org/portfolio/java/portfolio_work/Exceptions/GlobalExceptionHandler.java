package org.portfolio.java.portfolio_work.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.portfolio.java.portfolio_work.Exceptions.BadRequestException.BadRequestExceptionSubType;
import org.portfolio.java.portfolio_work.Exceptions.InternalServerErrorException.InternalServerErrorExceptionSubType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse>
    handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = createApiError(
                HttpStatus.BAD_REQUEST,
                BadRequestExceptionSubType.MALFORMED_REQUEST_BODY,
                request.getRequestURI(),
                null
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(
            ApiException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = exception.getHttpStatus();

        if (status.is5xxServerError()) {
            log.error(
                    "API error occurred. Status: {}, subtype: {}, path: {}",
                    status.value(),
                    exception.getSubType().getNumber(),
                    request.getRequestURI(),
                    exception
            );
        } else {
            log.warn(
                    "API request failed. Status: {}, subtype: {}, path: {}",
                    status.value(),
                    exception.getSubType().getNumber(),
                    request.getRequestURI()
            );
        }

        ApiErrorResponse response = createApiError(
                status,
                exception.getSubType(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> validationErrors =
                new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        validationErrors.putIfAbsent(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        ApiErrorResponse response = createApiError(
                HttpStatus.BAD_REQUEST,
                BadRequestExceptionSubType.VALIDATION_FAILED,
                request.getRequestURI(),
                validationErrors
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error(
                "Unexpected exception occurred on path: {}",
                request.getRequestURI(),
                exception
        );

        ApiErrorResponse response = createApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                InternalServerErrorExceptionSubType.INTERNAL_SERVER_ERROR,
                request.getRequestURI(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private ApiErrorResponse createApiError(
            HttpStatus status,
            ApiErrorSubType subType,
            String path,
            Map<String, String> validationErrors
    ) {
        return new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                createErrorCode(status, subType),
                subType.getNumber(),
                subType.getMessage(),
                path,
                validationErrors
        );
    }

    private String createErrorCode(
            HttpStatus status,
            ApiErrorSubType subType
    ) {
        return status.name() + "-" + subType.getNumber();
    }
}