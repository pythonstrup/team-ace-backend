package com.nexters.teamace.auth.presentation;

import com.nexters.teamace.auth.application.AuthService;
import com.nexters.teamace.auth.application.LoginCommand;
import com.nexters.teamace.auth.application.LoginResult;
import com.nexters.teamace.auth.application.SignupCommand;
import com.nexters.teamace.auth.application.SignupResult;
import com.nexters.teamace.common.presentation.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody final LoginRequest request) {
        final LoginCommand command = new LoginCommand(request.username());
        final LoginResult result = authService.login(command);
        final LoginResponse response =
                new LoginResponse(result.username(), result.accessToken(), result.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @Valid @RequestBody final SignupRequest request) {
        final SignupCommand command = new SignupCommand(request.username(), request.nickname());
        final SignupResult result = authService.signup(command);
        final SignupResponse response =
                new SignupResponse(result.accessToken(), result.refreshToken(), result.username());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
