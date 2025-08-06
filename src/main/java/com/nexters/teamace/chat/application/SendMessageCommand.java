package com.nexters.teamace.chat.application;

public record SendMessageCommand(Long userId, Long chatRoomId, String message) {}
