package com.nexters.teamace.chat.application;

import com.nexters.teamace.chat.domain.ChatMessageGenerator;
import com.nexters.teamace.chat.domain.ChatRoom;
import com.nexters.teamace.chat.domain.ChatRoomRepository;
import com.nexters.teamace.conversation.application.ConversationContext;
import com.nexters.teamace.conversation.application.ConversationService;
import com.nexters.teamace.conversation.domain.ConversationType;
import com.nexters.teamace.conversation.domain.MessageConversation;
import com.nexters.teamace.user.application.GetUserResult;
import com.nexters.teamace.user.application.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageGenerator chatMessageGenerator;
    private final ConversationService conversationService;
    private final UserService userService;

    @Transactional
    public ChatRoomResult createChat(final ChatRoomCommand command) {
        final GetUserResult user = userService.getUserByUsername(command.username());
        final ChatRoom chatRoom = ChatRoom.of(user.id());

        final String firstMessage = chatMessageGenerator.generateFirstMessage();
        chatRoom.addSystemMessage(firstMessage);
        final ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomResult(savedChatRoom.getId(), firstMessage);
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
