package com.nexters.teamace.conversation.application;

public interface ConversationClient {

    <T> T chat(Class<T> type, String script, String message);
}
