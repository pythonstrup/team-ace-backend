package com.nexters.teamace.chat.infrastructure;

import com.nexters.teamace.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "chat_rooms")
@SQLDelete(
        sql =
                """
            UPDATE chat_rooms cr
                LEFT JOIN chats c ON cr.id = c.chat_room_id
            SET cr.deleted_at = NOW(),
                c.deleted_at =
                    CASE
                        WHEN c.id IS NOT NULL AND c.deleted_at IS NULL THEN NOW()
                        ELSE c.deleted_at
                    END
                WHERE cr.id = ?
        """)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomEntity extends BaseEntity {

    private Long userId;

    @OneToMany(
            mappedBy = "chatRoom",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ChatEntity> chats = new ArrayList<>();

    public ChatRoomEntity(final Long userId) {
        this.userId = userId;
    }

    public ChatRoomEntity(final Long id, final Long userId) {
        super(id);
        this.userId = userId;
    }

    public void addChat(final ChatEntity chat) {
        this.chats.add(chat);
        chat.setChatRoom(this);
    }
}
