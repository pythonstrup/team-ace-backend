package com.nexters.teamace.chat.application;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.*;

import org.springframework.util.StringUtils;

public record SendMessageCommand(Long userId, Long chatRoomId, String message) {
    public SendMessageCommand {
        if (userId == null) {
            throw new IllegalArgumentException(USER_ID_NOT_NULL);
        }
        if (userId < 1) {
            throw new IllegalArgumentException(USER_ID_POSITIVE);
        }
        if (chatRoomId == null) {
            throw new IllegalArgumentException(CHAT_ROOM_ID_NOT_NULL);
        }
        if (chatRoomId < 1) {
            throw new IllegalArgumentException(CHAT_ROOM_ID_POSITIVE);
        }
        if (!StringUtils.hasText(message)) {
            throw new IllegalArgumentException(MESSAGE_NOT_BLANK);
        }
    }
}
