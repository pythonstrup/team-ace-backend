package com.nexters.teamace.chat.infrastructure;

import com.nexters.teamace.chat.domain.ChatRoom;
import com.nexters.teamace.chat.domain.ChatRoomRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomMapper chatRoomMapper;

    @Override
    public ChatRoom save(final ChatRoom chatRoom) {
        final ChatRoomEntity entity = chatRoomMapper.toEntity(chatRoom);
        final ChatRoomEntity savedEntity = chatRoomJpaRepository.save(entity);
        return chatRoomMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ChatRoom> findById(final Long id) {
        return chatRoomJpaRepository.findByIdWithChats(id).map(chatRoomMapper::toDomain);
    }
}
