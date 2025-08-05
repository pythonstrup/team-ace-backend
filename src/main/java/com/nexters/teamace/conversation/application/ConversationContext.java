package com.nexters.teamace.conversation.application;

import com.nexters.teamace.conversation.domain.ConversationContextType;
import java.util.Map;

public record ConversationContext(
        String sessionKey, Map<ConversationContextType, String> variables) {

    public ConversationContext {
        if (sessionKey == null || sessionKey.trim().isEmpty()) {
            throw new IllegalArgumentException("SessionKey cannot be null or empty");
        }
        if (variables == null) {
            variables = Map.of();
        }
    }
}
