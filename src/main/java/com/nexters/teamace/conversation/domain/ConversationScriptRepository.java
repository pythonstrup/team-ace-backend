package com.nexters.teamace.conversation.domain;

import static com.nexters.teamace.common.exception.CustomException.PROMPT_TEMPLATE_NOT_FOUND;

import java.util.Optional;

public interface ConversationScriptRepository {

    Optional<ConversationScript> findByType(ConversationType type);

    default ConversationScript getByType(ConversationType type) {
        return findByType(type).orElseThrow(() -> PROMPT_TEMPLATE_NOT_FOUND);
    }
}
