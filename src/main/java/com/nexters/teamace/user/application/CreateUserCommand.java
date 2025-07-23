package com.nexters.teamace.user.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

/**
 * 사용자 생성 요청을 위한 Command 객체입니다.
 *
 * <p>생성 시 username과 nickname의 유효성을 검증합니다. null 값은 허용되지만 blank는 허용되지 않습니다.
 *
 * @param username 생성할 사용자명 (null 허용, null이 아닌 경우 1-20자, blank 불허)
 * @param nickname 사용자 닉네임 (null 허용, null이 아닌 경우 1-20자, blank 불허)
 * @throws IllegalArgumentException username 또는 nickname이 null이 아니면서 blank이거나 길이 제한을 초과하는 경우
 * @author pythonstrup
 * @since 2025-07-23
 */
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
