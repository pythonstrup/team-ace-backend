package com.nexters.teamace.chat.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    public ChatRoomResult createChat(final ChatRoomCommand command) {
        return new ChatRoomResult(1L, "첫번째 채팅");
    }
}
