package com.nexters.teamace.conversation.infrastructure.chatmodel;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
class GeminiChatModel implements ChatModel {

    private final Client client;
    private final String modelName;

    public GeminiChatModel(final GeminiProperties properties) {
        this.client = Client.builder().apiKey(properties.apiKey()).build();
        this.modelName = properties.model();
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        String userMessage =
                prompt.getInstructions().stream()
                        .map(org.springframework.ai.content.Content::getText)
                        .collect(Collectors.joining("\n"));

        GenerateContentResponse response =
                client.models.generateContent(modelName, userMessage, null);

        String responseText = response.text();
        if (responseText == null || responseText.isEmpty()) {
            return new ChatResponse(List.of());
        }

        Generation generation = new Generation(new AssistantMessage(responseText));
        List<Generation> generations = List.of(generation);

        return new ChatResponse(generations);
    }
}
