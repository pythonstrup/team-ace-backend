package com.nexters.teamace.conversation.infrastructure;

import com.nexters.teamace.conversation.application.ConversationClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

@Component
class ChatClientAdapter implements ConversationClient {

    private final ChatClient chatClient;

    public ChatClientAdapter(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @Override
    public String chat(String message) {
        return chatClient.prompt().user(message).call().content();
    }

    @Override
    public String chat(String script, String message) {
        return chatClient.prompt().system(script).user(message).call().content();
    }
}
