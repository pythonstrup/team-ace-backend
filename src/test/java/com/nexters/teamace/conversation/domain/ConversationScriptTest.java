package com.nexters.teamace.conversation.domain;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ConversationScript")
class ConversationScriptTest {

    @Nested
    @DisplayName("생성자")
    class Describe_constructor {

        @Nested
        @DisplayName("타입이 null일 때")
        class Context_with_null_type {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationScript(null, "content"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("ConversationType cannot be null");
            }
        }

        @Nested
        @DisplayName("콘텐츠가 null일 때")
        class Context_with_null_content {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationScript(ConversationType.CHAT_ASSISTANT, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Content cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("콘텐츠가 비어있을 때")
        class Context_with_empty_content {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationScript(ConversationType.CHAT_ASSISTANT, "   "))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Content cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("유효한 타입과 콘텐츠가 주어졌을 때")
        class Context_with_valid_type_and_content {

            @Test
            @DisplayName("ConversationScript를 성공적으로 생성한다")
            void it_creates_conversation_script() {
                // given
                var type = ConversationType.CHAT_ASSISTANT;
                String content = "Sample content";

                // when
                var script = new ConversationScript(type, content);

                // then
                then(script).extracting("type", "content").containsExactly(type, content);
            }
        }
    }

    @Nested
    @DisplayName("렌더링")
    class Describe_render {

        @Nested
        @DisplayName("콘텐츠에 변수가 없을 때")
        class Context_with_no_variables_in_content {

            @Test
            @DisplayName("원본 콘텐츠를 반환한다")
            void it_returns_original_content() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT, "Plain text content");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.PREVIOUS_CONVERSATIONS, "Previous");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Plain text content");
            }
        }

        @Nested
        @DisplayName("콘텐츠에 단일 변수가 있을 때")
        class Context_with_single_variable {

            @Test
            @DisplayName("변수를 값으로 역치한다")
            void it_replaces_variable_with_value() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT, "Stage: {{CONVERSATION_STAGE}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.CONVERSATION_STAGE, "1단계");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Stage: 1단계");
            }
        }

        @Nested
        @DisplayName("콘텐츠에 복수 변수가 있을 때")
        class Context_with_multiple_variables {

            @Test
            @DisplayName("모든 변수를 값으로 역치한다")
            void it_replaces_all_variables_with_values() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT,
                                "Previous: {{PREVIOUS_CONVERSATIONS}}, Stage: {{CONVERSATION_STAGE}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.PREVIOUS_CONVERSATIONS, "History");
                variables.put(ConversationContextType.CONVERSATION_STAGE, "1단계");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Previous: History, Stage: 1단계");
            }
        }

        @Nested
        @DisplayName("변수가 여러 번 나타날 때")
        class Context_with_repeated_variable {

            @Test
            @DisplayName("모든 발생을 대체한다")
            void it_replaces_all_occurrences() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT,
                                "First: {{CONVERSATION_STAGE}}, Second: {{CONVERSATION_STAGE}}, Third: {{CONVERSATION_STAGE}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.CONVERSATION_STAGE, "1단계");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("First: 1단계, Second: 1단계, Third: 1단계");
            }
        }

        @Nested
        @DisplayName("맵에서 변수가 누락된 때")
        class Context_with_missing_variable_in_map {

            @Test
            @DisplayName("빈 문자열로 대체한다")
            void it_replaces_with_empty_string() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT,
                                "Stage: {{CONVERSATION_STAGE}}, Previous: {{PREVIOUS_CONVERSATIONS}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.CONVERSATION_STAGE, "1단계");
                // PREVIOUS_CONVERSATIONS not provided

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Stage: 1단계, Previous: ");
            }
        }

        @Nested
        @DisplayName("템플릿에 알려지지 않은 변수가 있을 때")
        class Context_with_unknown_variable {

            @Test
            @DisplayName("플레이스홀더를 유지하고 에러를 로그한다")
            void it_keeps_placeholder_visible() {
                // given
                String templateContent =
                        "Hello {{UNKNOWN_VARIABLE}}, stage is {{CONVERSATION_STAGE}}";
                ConversationScript script =
                        new ConversationScript(ConversationType.CHAT_ASSISTANT, templateContent);

                Map<ConversationContextType, String> variables =
                        Map.of(ConversationContextType.CONVERSATION_STAGE, "1단계");

                // when
                String result = script.render(variables);

                // then
                then(result)
                        .contains("{{UNKNOWN_VARIABLE}}") // Unknown variable preserved
                        .contains("1단계") // Known variable replaced
                        .doesNotContain("{{CONVERSATION_STAGE}}"); // Known variable replaced
            }
        }

        @Nested
        @DisplayName("변수 값이 빈 문자열일 때")
        class Context_with_empty_variable_value {

            @Test
            @DisplayName("빈 문자열로 대체하고 경고를 로그한다")
            void it_replaces_with_empty_string_and_warns() {
                // given
                String templateContent = "Stage: {{CONVERSATION_STAGE}}";
                ConversationScript script =
                        new ConversationScript(ConversationType.CHAT_ASSISTANT, templateContent);

                Map<ConversationContextType, String> variables =
                        Map.of(
                                ConversationContextType.CONVERSATION_STAGE, "" // Empty value
                                );

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Stage: ");
            }
        }
    }

    @Nested
    @DisplayName("validateVariables")
    class Describe_validateVariables {

        @Test
        @DisplayName("모든 필수 변수가 제공되면 통과한다")
        void it_passes_when_all_variables_provided() {
            // given
            String content = "Stage: {{CONVERSATION_STAGE}}, Previous: {{PREVIOUS_CONVERSATIONS}}";
            ConversationScript script =
                    new ConversationScript(ConversationType.CHAT_ASSISTANT, content);

            Map<ConversationContextType, String> variables =
                    Map.of(
                            ConversationContextType.CONVERSATION_STAGE, "1단계",
                            ConversationContextType.PREVIOUS_CONVERSATIONS, "이전 대화");

            // when & then - should not throw exception
            script.validateVariables(variables);
        }

        @Test
        @DisplayName("필수 변수가 누락되면 예외를 발생시킨다")
        void it_throws_exception_when_variables_missing() {
            // given
            String content = "Stage: {{CONVERSATION_STAGE}}, Previous: {{PREVIOUS_CONVERSATIONS}}";
            ConversationScript script =
                    new ConversationScript(ConversationType.CHAT_ASSISTANT, content);

            Map<ConversationContextType, String> variables =
                    Map.of(
                            ConversationContextType.CONVERSATION_STAGE, "1단계"
                            // PREVIOUS_CONVERSATIONS is missing
                            );

            // when & then
            thenThrownBy(() -> script.validateVariables(variables))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(
                            "Missing required variables for template 'CHAT_ASSISTANT'")
                    .hasMessageContaining("PREVIOUS_CONVERSATIONS");
        }
    }
}
