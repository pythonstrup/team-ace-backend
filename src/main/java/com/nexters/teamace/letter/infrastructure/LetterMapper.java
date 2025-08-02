package com.nexters.teamace.letter.infrastructure;

import com.nexters.teamace.letter.domain.Letter;
import org.springframework.stereotype.Component;

@Component
public class LetterMapper {

    public LetterEntity toEntity(Letter letter) {
        return new LetterEntity(
                letter.getId(),
                letter.getChatRoomId(),
                letter.getFairyId(),
                letter.getUserInput(),
                letter.getContents());
    }

    public Letter toDomain(LetterEntity entity) {
        return Letter.restore(
                entity.getId(),
                entity.getChatRoomId(),
                entity.getFairyId(),
                entity.getUserInput(),
                entity.getContents());
    }
}
