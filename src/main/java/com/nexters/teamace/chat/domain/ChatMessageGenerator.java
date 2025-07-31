package com.nexters.teamace.chat.domain;

public interface ChatMessageGenerator {
    String generateFirstMessage();

    String generateResponseMessage(String userMessage, ChatContext context);
}
