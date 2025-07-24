package com.nexters.teamace.user.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

public record CreateUserCommand(String username, String nickname) {
    public CreateUserCommand {
        if (username != null) {
            if (!StringUtils.hasText(username)) {
                throw new IllegalArgumentException(USERNAME_NOT_BLANK);
            }
            if (username.length() > 20) {
                throw new IllegalArgumentException(USERNAME_SIZE);
            }
        }
        if (nickname != null) {
            if (!StringUtils.hasText(nickname)) {
                throw new IllegalArgumentException(NICKNAME_NOT_BLANK);
            }
            if (nickname.length() > 20) {
                throw new IllegalArgumentException(NICKNAME_SIZE);
            }
        }
    }
}
