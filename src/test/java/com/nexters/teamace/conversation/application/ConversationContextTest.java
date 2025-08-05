package com.nexters.teamace.conversation.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import com.nexters.teamace.conversation.domain.ConversationContextType;
import java.util.Map;
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
                thenThrownBy(() -> new ConversationContext(null, Map.of()))
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
                thenThrownBy(() -> new ConversationContext("   ", Map.of()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("SessionKey cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("변수 맵이 null일 때")
        class Context_with_null_variables {

            @Test
            @DisplayName("빈 맵으로 ConversationContext를 생성한다")
            void it_creates_context_with_empty_map() {
                // given
                String sessionKey = "session123";

                // when
                ConversationContext context = new ConversationContext(sessionKey, null);

                // then
                then(context)
                        .extracting("sessionKey", "variables")
                        .containsExactly(sessionKey, Map.of());
            }
        }

        @Nested
        @DisplayName("유효한 세션 키와 변수 맵이 주어졌을 때")
        class Context_with_valid_sessionKey_and_variables {

            @Test
            @DisplayName("ConversationContext를 성공적으로 생성한다")
            void it_creates_conversation_context() {
                // given
                String sessionKey = "session123";
                Map<ConversationContextType, String> variables =
                        Map.of(
                                ConversationContextType.PREVIOUS_CONVERSATIONS, "previous chat",
                                ConversationContextType.CONVERSATION_STAGE, "1단계");

                // when
                ConversationContext context = new ConversationContext(sessionKey, variables);

                // then
                then(context)
                        .extracting("sessionKey", "variables")
                        .containsExactly(sessionKey, variables);
            }
        }

        @Nested
        @DisplayName("변수 맵이 빈 맵일 때")
        class Context_with_empty_variables {

            @Test
            @DisplayName("빈 맵으로 ConversationContext를 생성한다")
            void it_creates_context_with_empty_map() {
                // given
                String sessionKey = "session123";
                Map<ConversationContextType, String> emptyVariables = Map.of();

                // when
                ConversationContext context = new ConversationContext(sessionKey, emptyVariables);

                // then
                then(context)
                        .extracting("sessionKey", "variables")
                        .containsExactly(sessionKey, emptyVariables);
            }
        }
    }
}
