package com.nexters.teamace.conversation.domain;

import lombok.Getter;

public enum ConversationType {
    CHAT_ASSISTANT("prompts/chat-assistant.txt", "채팅 어시스턴트"),
    EMOTION_ANALYSIS("prompts/emotion-analysis.txt", "감정 분석"),
    ;

    @Getter private final String resourcePath;
    private final String description;

    ConversationType(String resourcePath, String description) {
        this.resourcePath = resourcePath;
        this.description = description;
    }
}
