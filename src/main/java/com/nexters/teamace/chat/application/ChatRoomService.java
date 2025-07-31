package com.nexters.teamace.chat.application;

import com.nexters.teamace.chat.domain.ChatContext;
import com.nexters.teamace.chat.domain.ChatMessageGenerator;
import com.nexters.teamace.chat.domain.ChatRoom;
import com.nexters.teamace.chat.domain.ChatRoomRepository;
import com.nexters.teamace.user.application.GetUserResult;
import com.nexters.teamace.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageGenerator chatMessageGenerator;
    private final UserService userService;

    @Transactional
    public CreateChatRoomResult createChat(final ChatRoomCommand command) {
        final GetUserResult user = userService.getUserByUsername(command.username());
        final ChatRoom chatRoom = ChatRoom.create(user.id());

        final String firstMessage = chatMessageGenerator.generateFirstMessage();
        chatRoom.addSystemMessage(firstMessage);
        final ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return new CreateChatRoomResult(savedChatRoom.getId(), firstMessage);
    }

    @Transactional
    public SendMessageResult sendMessage(final SendMessageCommand command) {
        final ChatRoom chatRoom = chatRoomRepository.getById(command.chatRoomId());
        chatRoom.addUserMessage(command.message());

        final ChatContext chatContext = chatRoom.toChatContext();
        final String responseMessage =
                chatMessageGenerator.generateResponseMessage(command.message(), chatContext);

        chatRoom.addSystemMessage(responseMessage);
        chatRoomRepository.save(chatRoom);

        return new SendMessageResult(responseMessage);
    }
}
