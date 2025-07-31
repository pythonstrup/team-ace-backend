package com.nexters.teamace.chat.domain;

import java.util.List;

public record ChatContext(Long chatRoomId, List<Chat> previousChats) {}
