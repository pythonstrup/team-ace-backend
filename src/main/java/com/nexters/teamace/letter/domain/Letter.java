package com.nexters.teamace.letter.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Letter {

    @Getter @EqualsAndHashCode.Include private final Long id;
    @Getter private final Long chatRoomId;
    @Getter private final Long fairyId;
    @Getter private final String userInput;
    @Getter private final String contents;

    private Letter(
            final Long id,
            final Long chatRoomId,
            final Long fairyId,
            final String userInput,
            final String contents) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.fairyId = fairyId;
        this.userInput = userInput;
        this.contents = contents;
    }

    public static Letter create(final Long chatRoomId, final Long fairyId, final String userInput) {
        return new Letter(null, chatRoomId, fairyId, userInput, userInput);
    }

    public static Letter restore(
            final Long id,
            final Long chatRoomId,
            final Long fairyId,
            final String userInput,
            final String contents) {
        return new Letter(id, chatRoomId, fairyId, userInput, contents);
    }
}
