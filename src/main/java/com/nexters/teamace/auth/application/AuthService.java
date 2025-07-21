package com.nexters.teamace.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;

    public LoginResult login(final LoginCommand command) {
        final String userId = command.userId();
        log.info("Login attempt for userId: {}", userId);

        final String accessToken = tokenService.createAccessToken(userId);
        final String refreshToken = tokenService.createRefreshToken(userId);

        log.info("Login successful for userId: {}", userId);
        return new LoginResult(userId, accessToken, refreshToken);
    }
}
