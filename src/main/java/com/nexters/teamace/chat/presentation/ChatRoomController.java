package com.nexters.teamace.chat.presentation;

import com.nexters.teamace.chat.application.ChatRoomCommand;
import com.nexters.teamace.chat.application.ChatRoomService;
import com.nexters.teamace.chat.application.CreateChatRoomResult;
import com.nexters.teamace.chat.application.SendMessageCommand;
import com.nexters.teamace.chat.application.SendMessageResult;
import com.nexters.teamace.common.presentation.ApiResponse;
import com.nexters.teamace.common.presentation.AuthUser;
import com.nexters.teamace.common.presentation.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ApiResponse<ChatRoomResponse> createChatRoom(@AuthUser final UserInfo user) {
        final ChatRoomCommand command = new ChatRoomCommand(user.username());
        final CreateChatRoomResult chatRoom = chatRoomService.createChat(command);
        return ApiResponse.success(
                new ChatRoomResponse(chatRoom.chatRoomId(), chatRoom.firstChat()));
    }

    @PostMapping("/{chatRoomId}/messages")
    public ApiResponse<ChatMessageResponse> sendMessage(
            @AuthUser final UserInfo user,
            @PathVariable final Long chatRoomId,
            @RequestBody @Valid final ChatMessageRequest request) {
        final SendMessageCommand command =
                new SendMessageCommand(user.userId(), chatRoomId, request.message());
        final SendMessageResult result = chatRoomService.sendMessage(command);
        return ApiResponse.success(new ChatMessageResponse(result.message()));
    }
}
