package com.nexters.teamace.chat.application;

import com.nexters.teamace.chat.domain.MessageType;
import java.util.List;

public record AllChatResult(Long chatRoomId, List<ChatResult> chats) {

    public record ChatResult(Long chatId, MessageType type, String message) {}
}
