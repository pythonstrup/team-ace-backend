package com.nexters.teamace.chat.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatRoom {

    @Getter @EqualsAndHashCode.Include private final Long id;
    @Getter private final Long userId;
    @Getter private final Chats chats = new Chats();

    private ChatRoom(final Long id, final Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public static ChatRoom of(final Long userId) {
        return new ChatRoom(null, userId);
    }

    public static ChatRoom restore(final Long id, final Long userId) {
        return new ChatRoom(id, userId);
    }

    public void addChat(final Chat chat) {
        this.chats.add(chat);
    }

    public void addSystemMessage(final String message) {
        final Chat systemChat = Chat.of(this.id, MessageType.SYSTEM, message);
        this.chats.add(systemChat);
    }

    public void addUserMessage(final String message) {
        final Chat userChat = Chat.of(this.id, MessageType.USER, message);
        this.chats.add(userChat);
    }
}
