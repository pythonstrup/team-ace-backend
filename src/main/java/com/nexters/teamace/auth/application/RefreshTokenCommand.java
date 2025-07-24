package com.nexters.teamace.auth.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

public record RefreshTokenCommand(String refreshToken) {
    public RefreshTokenCommand {
        if (!StringUtils.hasText(refreshToken)) {
            throw new IllegalArgumentException(REFRESH_TOKEN_NOT_BLANK);
        }
    }
}
