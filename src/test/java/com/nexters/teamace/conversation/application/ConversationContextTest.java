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
    @DisplayName("생성자")
    class Describe_constructor {

        @Nested
        @DisplayName("세션 키가 null일 때")
        class Context_with_null_sessionKey {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationContext(null, List.of()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("SessionKey cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("세션 키가 비어있을 때")
        class Context_with_empty_sessionKey {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationContext("   ", List.of()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("SessionKey cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("이전 메시지가 null일 때")
        class Context_with_null_previousMessages {

            @Test
            @DisplayName("빈 리스트로 ConversationContext를 생성한다")
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
        @DisplayName("유효한 세션 키와 이전 메시지가 주어졌을 때")
        class Context_with_valid_sessionKey_and_previousMessages {

            @Test
            @DisplayName("ConversationContext를 성공적으로 생성한다")
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
        @DisplayName("이전 메시지가 빈 리스트일 때")
        class Context_with_empty_previousMessages {

            @Test
            @DisplayName("빈 리스트로 ConversationContext를 생성한다")
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
