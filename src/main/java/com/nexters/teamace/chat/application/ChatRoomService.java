package com.nexters.teamace.chat.application;

import com.nexters.teamace.chat.domain.Chat;
import com.nexters.teamace.chat.domain.ChatRoom;
import com.nexters.teamace.chat.domain.ChatRoomRepository;
import com.nexters.teamace.chat.domain.MessageType;
import com.nexters.teamace.common.exception.CustomException;
import com.nexters.teamace.conversation.application.ConversationContext;
import com.nexters.teamace.conversation.application.ConversationService;
import com.nexters.teamace.conversation.domain.ConversationType;
import com.nexters.teamace.conversation.domain.MessageConversation;
import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private static final List<String> FIRST_MESSAGES =
            List.of(
                    "안녕! 오늘 하루는 어땠어?",
                    "반가워! 무슨 일이야?",
                    "오늘도 수고했어! 어떤 이야기를 나누고 싶어?",
                    "안녕~ 오늘 기분은 어때?",
                    "만나서 반가워! 편하게 이야기해줘.");

    private final ConversationService conversationService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Transactional
    public ChatRoomResult createChat(final ChatRoomCommand command) {
        // 사용자 확인
        final User user =
                userRepository
                        .findByUsername(command.username())
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        // ChatRoom 생성
        final ChatRoom chatRoom = ChatRoom.of(user.getId());

        // 첫 메시지 생성
        final String firstMessage = FIRST_MESSAGES.get(random.nextInt(FIRST_MESSAGES.size()));
        final Chat firstChat = Chat.of(null, MessageType.SYSTEM, firstMessage);
        chatRoom.addChat(firstChat);

        // 저장
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
