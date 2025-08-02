package com.nexters.teamace.letter.infrastructure;

import com.nexters.teamace.common.infrastructure.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "letters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LetterEntity extends BaseEntity {

    private Long chatRoomId;

    private Long fairyId;

    private String userInput;

    private String contents;

    public LetterEntity(
            final Long id,
            final Long chatRoomId,
            final Long fairyId,
            final String userInput,
            final String contents) {
        super(id);
        this.chatRoomId = chatRoomId;
        this.fairyId = fairyId;
        this.userInput = userInput;
        this.contents = contents;
    }
}
