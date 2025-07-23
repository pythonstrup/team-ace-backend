package com.nexters.teamace.auth.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

/**
 * 회원가입 요청을 위한 Command 객체입니다.
 * <p>생성 시 username과 nickname의 유효성을 검증합니다.
 *
 * @param username 회원가입할 사용자명 (1-20자, null/blank 불허)
 * @param nickname 사용자 닉네임 (1-20자, null/blank 불허)
 * @throws IllegalArgumentException username 또는 nickname이 null, blank이거나 길이 제한을 초과하는 경우
 * @author pythonstrup
 * @since 2025-07-23
 */
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
