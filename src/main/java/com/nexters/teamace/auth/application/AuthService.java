package com.nexters.teamace.auth.application;

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

        final String accessToken = tokenService.createAccessToken(user.username());
        final String refreshToken = tokenService.createRefreshToken(user.username());

        return new LoginResult(user.username(), accessToken, refreshToken);
    }

    public SignupResult signup(final SignupCommand command) {
        final var createUserCommand = new CreateUserCommand(command.username(), command.nickname());
        final CreateUserResult user = userService.createUser(createUserCommand);

        final String accessToken = tokenService.createAccessToken(user.username());
        final String refreshToken = tokenService.createRefreshToken(user.username());

        return new SignupResult(user.username(), accessToken, refreshToken);
    }
}
