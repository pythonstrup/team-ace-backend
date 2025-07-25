package com.nexters.teamace.chat.application;

import com.nexters.teamace.conversation.application.ConversationContext;
import com.nexters.teamace.conversation.application.ConversationService;
import com.nexters.teamace.conversation.domain.ConversationType;
import com.nexters.teamace.conversation.domain.MessageConversation;
import java.util.List;
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
        final var conversationType = ConversationType.CHAT_ASSISTANT;
        final ConversationContext conversationContext =
                new ConversationContext("채팅룸아이디", List.of());
        final MessageConversation conversation =
                (MessageConversation)
                        conversationService.chat(
                                conversationType.getType(),
                                conversationType,
                                conversationContext,
                                command.message());
        return new SendMessageResult(conversation.message());
    }
}
