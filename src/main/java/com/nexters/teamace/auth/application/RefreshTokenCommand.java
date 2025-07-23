package com.nexters.teamace.auth.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

/**
 * 토큰 갱신 요청을 위한 Command 객체입니다.
 * <p>생성 시 refreshToken의 유효성을 검증합니다.
 *
 * @param refreshToken 갱신에 사용할 리프레시 토큰 (null/blank 불허)
 * @throws IllegalArgumentException refreshToken이 null 또는 blank인 경우
 * @author pythonstrup
 * @since 2025-07-23
 */
public record RefreshTokenCommand(String refreshToken) {
    public RefreshTokenCommand {
        if (!StringUtils.hasText(refreshToken)) {
            throw new IllegalArgumentException(REFRESH_TOKEN_NOT_BLANK);
        }
    }
}
