package com.nexters.teamace.chat.presentation;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.USERNAME_NOT_BLANK;

import jakarta.validation.constraints.NotBlank;

public record ChatRoomRequest(@NotBlank(message = USERNAME_NOT_BLANK) String username) {}
