package com.nexters.teamace.chat.domain;

import static com.nexters.teamace.common.exception.CustomException.CHAT_ROOM_NOT_FOUND;

import java.util.Optional;

public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findById(Long id);

    default ChatRoom getById(Long id) {
        return findById(id).orElseThrow(() -> CHAT_ROOM_NOT_FOUND);
    }
}
