package com.nexters.teamace.conversation.application;

public interface ConversationClient {

    String chat(String message);

    String chat(String script, String message);
}
