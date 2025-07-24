package com.nexters.teamace.auth.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

public record SignupCommand(String username, String nickname) {
    public SignupCommand {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException(USERNAME_NOT_BLANK);
        }
        if (username.length() > 20) {
            throw new IllegalArgumentException(USERNAME_SIZE);
        }
        if (!StringUtils.hasText(nickname)) {
            throw new IllegalArgumentException(NICKNAME_NOT_BLANK);
        }
        if (nickname.length() > 20) {
            throw new IllegalArgumentException(NICKNAME_SIZE);
        }
    }
}
