package com.nexters.teamace.auth.presentation;

import com.nexters.teamace.common.exception.ValidationErrorMessage;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = ValidationErrorMessage.USER_ID_NOT_BLANK) String userId) {}
