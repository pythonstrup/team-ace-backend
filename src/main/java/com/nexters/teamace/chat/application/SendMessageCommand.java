package com.nexters.teamace.chat.application;

public record SendMessageCommand(Long chatRoomId, String message) {}
