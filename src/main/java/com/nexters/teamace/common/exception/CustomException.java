package com.nexters.teamace.common.exception;

import lombok.Getter;

public class CustomException extends RuntimeException {

    public static final CustomException INVALID_TOKEN =
            new CustomException(ErrorType.INVALID_TOKEN);
    public static final CustomException INVALID_REFRESH_TOKEN =
            new CustomException(ErrorType.INVALID_REFRESH_TOKEN);
    public static final CustomException USER_NOT_FOUND =
            new CustomException(ErrorType.USER_NOT_FOUND);
    public static final CustomException USER_ALREADY_EXISTS =
            new CustomException(ErrorType.USER_ALREADY_EXISTS);

    @Getter private final ErrorType errorType;
    @Getter private final Object data;

    private CustomException(final ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public CustomException(final ErrorType errorType, final Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }
}
