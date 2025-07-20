package com.nexters.teamace.chat.application;

import com.nexters.teamace.conversation.application.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ConversationService conversationService;

    public ChatRoomResult createChat(final ChatRoomCommand command) {
        return new ChatRoomResult(1L, "첫번째 채팅");
    }

    public SendMessageResult sendMessage(final SendMessageCommand command) {
        final String aiResponse = conversationService.chat(command.message());
        return new SendMessageResult(aiResponse);
    }
}
