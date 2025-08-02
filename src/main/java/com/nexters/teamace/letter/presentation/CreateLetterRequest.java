package com.nexters.teamace.letter.presentation;

import com.nexters.teamace.common.exception.ValidationErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateLetterRequest(
        @NotNull(message = ValidationErrorMessage.CHAT_ROOM_ID_NOT_NULL)
                @Positive(message = ValidationErrorMessage.CHAT_ROOM_ID_POSITIVE)
                Long chatRoomId,
        @NotNull(message = ValidationErrorMessage.FAIRY_ID_NOT_NULL)
                @Positive(message = ValidationErrorMessage.FAIRY_ID_ID_POSITIVE)
                Long fairyId,
        @NotBlank(message = ValidationErrorMessage.LETTER_CONTENTS_NOT_BLANK) String contents) {}
