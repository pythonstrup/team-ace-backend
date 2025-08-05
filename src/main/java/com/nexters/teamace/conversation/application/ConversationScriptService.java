package com.nexters.teamace.conversation.application;

import com.nexters.teamace.conversation.domain.ConversationScript;
import com.nexters.teamace.conversation.domain.ConversationScriptRepository;
import com.nexters.teamace.conversation.domain.ConversationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ConversationScriptService {

    private final ConversationScriptRepository conversationScriptRepository;

    public ConversationScript getPromptTemplate(ConversationType type) {
        return conversationScriptRepository.getByType(type);
    }
}
