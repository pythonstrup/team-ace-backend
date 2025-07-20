package com.nexters.teamace.common.presentation;

import com.nexters.teamace.common.exception.ErrorType;

public record ApiResponse<T>(boolean success, T data, ErrorMessage error) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(
            final ErrorType errorType, final String message, final Object data) {
        return new ApiResponse<>(false, null, new ErrorMessage(errorType.getCode(), message, data));
    }

    public static <T> ApiResponse<T> error(final ErrorType errorType, final String message) {
        return error(errorType, message, null);
    }

    public static <T> ApiResponse<T> error(final ErrorType errorType) {
        return error(errorType, errorType.getMessage(), null);
    }
}
