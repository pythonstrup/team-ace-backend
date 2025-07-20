package com.nexters.teamace.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorType {
    /** 400 BAD_REQUEST */
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, ErrorCode.E400, "Validation failed"),
    BINDING_ERROR(HttpStatus.BAD_REQUEST, ErrorCode.E400, "Validation failed"),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, ErrorCode.E400, "Validation failed"),

    /** 404 NOT_FOUND */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, ErrorCode.E404, "Resource not found"),

    /** 500 INTERNAL_SERVER_ERROR */
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error occurred"),
    ;

    @Getter private final HttpStatus status;
    @Getter private final ErrorCode code;
    @Getter private final String message;
}
