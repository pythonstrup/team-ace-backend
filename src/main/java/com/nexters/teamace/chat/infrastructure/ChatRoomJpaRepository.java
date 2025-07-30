package com.nexters.teamace.chat.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Query("SELECT cr FROM ChatRoomEntity cr LEFT JOIN FETCH cr.chats WHERE cr.id = :id")
    Optional<ChatRoomEntity> findByIdWithChats(@Param("id") Long id);
}
