package com.nexters.teamace.auth.presentation;

import com.nexters.teamace.common.exception.ValidationErrorMessage;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = ValidationErrorMessage.REFRESH_TOKEN_NOT_BLANK) String refreshToken) {}
