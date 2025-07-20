package com.nexters.teamace.chat.presentation;

import com.nexters.teamace.common.exception.ValidationErrorMessage;
import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
        @NotBlank(message = ValidationErrorMessage.MESSAGE_NOT_BLANK) String message) {}
