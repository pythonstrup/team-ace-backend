package com.nexters.teamace.chat.presentation;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.USER_ID_NOT_BLANK;

import jakarta.validation.constraints.NotBlank;

public record ChatRoomRequest(@NotBlank(message = USER_ID_NOT_BLANK) String userId) {}
