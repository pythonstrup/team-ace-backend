package com.nexters.teamace.conversation.application;

import java.util.List;

public record ConversationContext(String sessionKey, List<String> previousMessages) {

    public ConversationContext {
        if (sessionKey == null || sessionKey.trim().isEmpty()) {
            throw new IllegalArgumentException("SessionKey cannot be null or empty");
        }
        if (previousMessages == null) {
            previousMessages = List.of();
        }
    }
}
