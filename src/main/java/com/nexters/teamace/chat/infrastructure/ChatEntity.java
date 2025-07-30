package com.nexters.teamace.chat.infrastructure;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

import com.nexters.teamace.chat.domain.MessageType;
import com.nexters.teamace.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "chats")
@SQLDelete(sql = "UPDATE chats SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatEntity extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoom;

    @Enumerated(STRING)
    private MessageType type;

    private String message;

    public ChatEntity(final MessageType type, final String message) {
        this.type = type;
        this.message = message;
    }

    public ChatEntity(final Long id, final MessageType type, final String message) {
        super(id);
        this.type = type;
        this.message = message;
    }

    public void setChatRoom(final ChatRoomEntity chatRoom) {
        this.chatRoom = chatRoom;
    }
}
