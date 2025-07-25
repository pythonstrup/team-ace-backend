package com.nexters.teamace.conversation.application;

import static com.nexters.teamace.conversation.domain.ConversationContextType.MESSAGE;
import static com.nexters.teamace.conversation.domain.ConversationContextType.PREVIOUS_CONVERSATIONS;

import com.nexters.teamace.conversation.domain.ConversationContextType;
import com.nexters.teamace.conversation.domain.ConversationScript;
import com.nexters.teamace.conversation.domain.ConversationScriptRepository;
import com.nexters.teamace.conversation.domain.ConversationType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ConversationScriptService {

    private final ConversationScriptRepository conversationScriptRepository;

    public ConversationScript getPromptTemplate(ConversationType type) {
        return conversationScriptRepository.getByType(type);
    }

    public String renderScript(
            ConversationType type, ConversationContext context, String userMessage) {
        final ConversationScript template = getPromptTemplate(type);
        final Map<ConversationContextType, String> variables =
                createConversationVariables(context, userMessage);
        return template.render(variables);
    }

    private Map<ConversationContextType, String> createConversationVariables(
            ConversationContext context, String userMessage) {
        return Map.ofEntries(
                Map.entry(MESSAGE, userMessage),
                Map.entry(PREVIOUS_CONVERSATIONS, String.join("\n", context.previousMessages())));
    }
}
