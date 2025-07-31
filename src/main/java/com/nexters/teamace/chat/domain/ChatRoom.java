package com.nexters.teamace.chat.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatRoom {

    @Getter @EqualsAndHashCode.Include private final Long id;
    @Getter private final Long userId;
    @Getter private final Chats chats;

    private ChatRoom(final Long id, final Long userId, final Chats chats) {
        this.id = id;
        this.userId = userId;
        this.chats = chats;
    }

    public static ChatRoom create(final Long userId) {
        return new ChatRoom(null, userId, new Chats());
    }

    public static ChatRoom restore(final Long id, final Long userId, final Chats chats) {
        return new ChatRoom(id, userId, chats);
    }

    public void addSystemMessage(final String message) {
        final Chat systemChat = Chat.create(this.id, MessageType.SYSTEM, message);
        this.chats.add(systemChat);
    }

    public void addUserMessage(final String message) {
        final Chat userChat = Chat.create(this.id, MessageType.USER, message);
        this.chats.add(userChat);
    }

    public ChatContext toChatContext() {
        if (this.id == null) {
            throw new IllegalStateException(
                    "ChatRoom ID must not be null when creating ChatContext");
        }
        return new ChatContext(this.id, this.chats.getChats());
    }
}
