package com.nexters.teamace.conversation.infrastructure.chatmodel;

import static com.google.genai.types.Content.fromParts;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import java.util.List;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
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
        // Use Spring AI's built-in methods for cleaner code
        final String systemMessage = prompt.getSystemMessage().getText();
        final String userMessage = prompt.getUserMessage().getText();

        // Create config with system instruction and optimized settings
        final var systemInstruction = fromParts(Part.fromText(systemMessage));
        final GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .systemInstruction(systemInstruction)
                        .responseMimeType(MediaType.APPLICATION_JSON_VALUE) // JSON 응답 보장
                        .temperature(0.7f)
                        .topP(0.9f)
                        .topK(40f)
                        .maxOutputTokens(2048)
                        .candidateCount(1)
                        .build();

        final GenerateContentResponse response =
                client.models.generateContent(modelName, userMessage, config);

        final String responseText = response.text();
        if (responseText == null || responseText.isEmpty()) {
            return new ChatResponse(List.of());
        }

        final Generation generation = new Generation(new AssistantMessage(responseText));
        final List<Generation> generations = List.of(generation);

        return new ChatResponse(generations);
    }
}
