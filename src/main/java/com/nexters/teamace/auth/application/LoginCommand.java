package com.nexters.teamace.auth.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

public record LoginCommand(String username) {
    public LoginCommand {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException(USERNAME_NOT_BLANK);
        }
        if (username.length() > 20) {
            throw new IllegalArgumentException(USERNAME_SIZE);
        }
    }
}
