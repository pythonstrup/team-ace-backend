package com.nexters.teamace.auth.application;

import com.nexters.teamace.common.exception.CustomException;
import com.nexters.teamace.user.application.CreateUserCommand;
import com.nexters.teamace.user.application.CreateUserResult;
import com.nexters.teamace.user.application.GetUserResult;
import com.nexters.teamace.user.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;

    public LoginResult login(final LoginCommand command) {
        final GetUserResult user = userService.getUserByUsername(command.username());
        final var authenticatedUser = new AuthenticatedUser(user.username(), user.id());

        final String accessToken = tokenService.createAccessToken(authenticatedUser);
        final String refreshToken = tokenService.createRefreshToken(authenticatedUser);

        return new LoginResult(user.username(), accessToken, refreshToken);
    }

    public SignupResult signup(final SignupCommand command) {
        final var createUserCommand = new CreateUserCommand(command.username(), command.nickname());
        final CreateUserResult user = userService.createUser(createUserCommand);
        final var authenticatedUser = new AuthenticatedUser(user.username(), user.id());

        final String accessToken = tokenService.createAccessToken(authenticatedUser);
        final String refreshToken = tokenService.createRefreshToken(authenticatedUser);

        return new SignupResult(user.username(), accessToken, refreshToken);
    }

    public RefreshTokenResult refreshToken(final RefreshTokenCommand command) {
        if (!tokenService.validateToken(command.refreshToken())) {
            throw CustomException.INVALID_REFRESH_TOKEN;
        }

        final AuthenticatedUser user =
                tokenService.getAuthenticatedUserFromToken(command.refreshToken());
        final String newAccessToken = tokenService.createAccessToken(user);

        return new RefreshTokenResult(newAccessToken);
    }
}
