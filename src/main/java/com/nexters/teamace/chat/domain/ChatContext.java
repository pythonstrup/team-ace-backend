package com.nexters.teamace.chat.domain;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.CHAT_ROOM_ID_NOT_NULL;
import static com.nexters.teamace.common.exception.ValidationErrorMessage.CHAT_ROOM_ID_POSITIVE;
import static com.nexters.teamace.common.exception.ValidationErrorMessage.PREVIOUS_CHATS_NOT_NULL;

public record ChatContext(Long chatRoomId, Chats previousChats) {
    public ChatContext {
        if (chatRoomId == null) {
            throw new IllegalArgumentException(CHAT_ROOM_ID_NOT_NULL);
        }
        if (chatRoomId < 1) {
            throw new IllegalArgumentException(CHAT_ROOM_ID_POSITIVE);
        }
        if (previousChats == null) {
            throw new IllegalArgumentException(PREVIOUS_CHATS_NOT_NULL);
        }
    }
}
