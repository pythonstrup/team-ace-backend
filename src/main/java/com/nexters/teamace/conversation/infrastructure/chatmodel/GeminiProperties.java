package com.nexters.teamace.conversation.infrastructure.chatmodel;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemini")
record GeminiProperties(String apiKey, String model) {}
