package com.nexters.teamace.conversation.application;

import com.nexters.teamace.conversation.domain.ConversationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationClient conversationClient;
    private final ConversationScriptService conversationScriptService;

    public String chat(String message) {
        return conversationClient.chat(message);
    }

    public String chat(final ConversationType conversationType, final String message) {
        final String script = conversationScriptService.renderScript(conversationType, message);
        return conversationClient.chat(script, message);
    }

    public String chat(
            final ConversationType conversationType,
            final ConversationContext context,
            final String message) {
        final String script =
                conversationScriptService.renderScript(conversationType, context, message);
        return conversationClient.chat(script, message);
    }
}
