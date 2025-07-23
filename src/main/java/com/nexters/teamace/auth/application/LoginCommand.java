package com.nexters.teamace.auth.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

/**
 * 로그인 요청을 위한 Command 객체입니다.
 * <p>생성 시 username의 유효성을 검증합니다.
 *
 * @param username 로그인할 사용자명 (1-20자, null/blank 불허)
 * @throws IllegalArgumentException username이 null, blank이거나 길이 제한을 초과하는 경우
 * @author pythonstrup
 * @since 2025-07-23
 */
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
