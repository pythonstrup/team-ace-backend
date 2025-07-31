package com.nexters.teamace.chat.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {

    @Getter @EqualsAndHashCode.Include private final Long id;
    @Getter private final Long chatRoomId;
    @Getter private final MessageType type;
    @Getter private final String message;

    private Chat(
            final Long id, final Long chatRoomId, final MessageType type, final String message) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.type = type;
        this.message = message;
    }

    public static Chat create(final Long chatRoomId, final MessageType type, final String message) {
        return new Chat(null, chatRoomId, type, message);
    }

    public static Chat restore(
            final Long id, final Long chatRoomId, final MessageType type, final String message) {
        return new Chat(id, chatRoomId, type, message);
    }
}
