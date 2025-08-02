package com.nexters.teamace.letter.application;

public record CreateLetterCommand(Long userId, Long chatRoomId, Long fairyId, String contents) {
    public CreateLetterCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (chatRoomId == null) {
            throw new IllegalArgumentException("ChatRoom ID cannot be null");
        }
        if (chatRoomId <= 0) {
            throw new IllegalArgumentException("ChatRoom ID must be greater than 0");
        }
        if (fairyId == null) {
            throw new IllegalArgumentException("Fairy ID cannot be null");
        }
        if (fairyId <= 0) {
            throw new IllegalArgumentException("Fairy ID must be greater than 0");
        }
        if (contents == null || contents.isBlank()) {
            throw new IllegalArgumentException("Letter contents must not be blank");
        }
    }
}
