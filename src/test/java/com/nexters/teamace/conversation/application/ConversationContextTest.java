package com.nexters.teamace.conversation.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ConversationContext")
class ConversationContextTest {

    @Nested
    @DisplayName("constructor")
    class Describe_constructor {

        @Nested
        @DisplayName("when sessionKey is null")
        class Context_with_null_sessionKey {

            @Test
            @DisplayName("it throws IllegalArgumentException")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationContext(null, List.of()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("SessionKey cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("when sessionKey is empty")
        class Context_with_empty_sessionKey {

            @Test
            @DisplayName("it throws IllegalArgumentException")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationContext("   ", List.of()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("SessionKey cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("when previousMessages is null")
        class Context_with_null_previousMessages {

            @Test
            @DisplayName("it creates ConversationContext with empty list")
            void it_creates_context_with_empty_list() {
                // given
                String sessionKey = "session123";

                // when
                ConversationContext context = new ConversationContext(sessionKey, null);

                // then
                then(context)
                        .extracting("sessionKey", "previousMessages")
                        .containsExactly(sessionKey, List.of());
            }
        }

        @Nested
        @DisplayName("when valid sessionKey and previousMessages are provided")
        class Context_with_valid_sessionKey_and_previousMessages {

            @Test
            @DisplayName("it creates ConversationContext successfully")
            void it_creates_conversation_context() {
                // given
                String sessionKey = "session123";
                List<String> previousMessages = List.of("message1", "message2");

                // when
                ConversationContext context = new ConversationContext(sessionKey, previousMessages);

                // then
                then(context)
                        .extracting("sessionKey", "previousMessages")
                        .containsExactly(sessionKey, previousMessages);
            }
        }

        @Nested
        @DisplayName("when previousMessages is empty list")
        class Context_with_empty_previousMessages {

            @Test
            @DisplayName("it creates ConversationContext with empty list")
            void it_creates_context_with_empty_list() {
                // given
                String sessionKey = "session123";
                List<String> emptyMessages = List.of();

                // when
                ConversationContext context = new ConversationContext(sessionKey, emptyMessages);

                // then
                then(context)
                        .extracting("sessionKey", "previousMessages")
                        .containsExactly(sessionKey, emptyMessages);
            }
        }
    }
}
