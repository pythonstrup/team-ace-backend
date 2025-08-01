package com.nexters.teamace.chat.infrastructure;

import com.nexters.teamace.chat.domain.Chat;
import com.nexters.teamace.chat.domain.ChatRoom;
import org.springframework.stereotype.Component;

@Component
class ChatRoomMapper {

    public ChatRoom toDomain(final ChatRoomEntity entity) {
        final ChatRoom chatRoom = ChatRoom.restore(entity.getId(), entity.getUserId());
        entity.getChats().forEach(chatEntity -> chatRoom.addChat(toChatDomain(chatEntity)));
        return chatRoom;
    }

    public ChatRoomEntity toEntity(final ChatRoom domain) {
        final ChatRoomEntity entity = new ChatRoomEntity(domain.getId(), domain.getUserId());
        domain.getChats().stream().map(this::toChatEntity).forEach(entity::addChat);
        return entity;
    }

    private Chat toChatDomain(final ChatEntity entity) {
        return Chat.restore(
                entity.getId(),
                entity.getChatRoom().getId(),
                entity.getType(),
                entity.getMessage());
    }

    private ChatEntity toChatEntity(final Chat domain) {
        return new ChatEntity(domain.getId(), domain.getType(), domain.getMessage());
    }
}
