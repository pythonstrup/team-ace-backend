package com.nexters.teamace.conversation.infrastructure;

import com.nexters.teamace.conversation.domain.ConversationScript;
import com.nexters.teamace.conversation.domain.ConversationScriptRepository;
import com.nexters.teamace.conversation.domain.ConversationType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
class PromptResourceLoader implements ConversationScriptRepository {

    @Override
    public Optional<ConversationScript> findByType(final ConversationType type) {
        try {
            final var resource = new ClassPathResource(type.getResourcePath());
            if (!resource.exists()) {
                return Optional.empty();
            }
            final String template = resource.getContentAsString(StandardCharsets.UTF_8);
            final ConversationScript conversationScript = new ConversationScript(type, template);
            return Optional.of(conversationScript);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
