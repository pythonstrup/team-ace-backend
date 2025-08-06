package com.nexters.teamace.chat.application;

public record SendMessageCommand(Long uerId, Long chatRoomId, String message) {}
