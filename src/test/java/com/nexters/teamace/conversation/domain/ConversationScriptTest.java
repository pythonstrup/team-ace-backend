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
    @DisplayName("constructor")
    class Describe_constructor {

        @Nested
        @DisplayName("when type is null")
        class Context_with_null_type {

            @Test
            @DisplayName("it throws IllegalArgumentException")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationScript(null, "content"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("ConversationType cannot be null");
            }
        }

        @Nested
        @DisplayName("when content is null")
        class Context_with_null_content {

            @Test
            @DisplayName("it throws IllegalArgumentException")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationScript(ConversationType.CHAT_ASSISTANT, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Content cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("when content is empty")
        class Context_with_empty_content {

            @Test
            @DisplayName("it throws IllegalArgumentException")
            void it_throws_illegal_argument_exception() {
                thenThrownBy(() -> new ConversationScript(ConversationType.CHAT_ASSISTANT, "   "))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Content cannot be null or empty");
            }
        }

        @Nested
        @DisplayName("when valid type and content are provided")
        class Context_with_valid_type_and_content {

            @Test
            @DisplayName("it creates ConversationScript successfully")
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
    @DisplayName("render")
    class Describe_render {

        @Nested
        @DisplayName("when variables is null")
        class Context_with_null_variables {

            @Test
            @DisplayName("it returns original content")
            void it_returns_original_content() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT,
                                "Hello {{MESSAGE}}, this is {{PREVIOUS_CONVERSATIONS}}");

                // when
                String result = script.render(null);

                // then
                then(result).isEqualTo("Hello {{MESSAGE}}, this is {{PREVIOUS_CONVERSATIONS}}");
            }
        }

        @Nested
        @DisplayName("when variables is empty")
        class Context_with_empty_variables {

            @Test
            @DisplayName("it returns original content")
            void it_returns_original_content() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT, "Template with {{MESSAGE}}");
                Map<ConversationContextType, String> emptyVariables = new HashMap<>();

                // when
                String result = script.render(emptyVariables);

                // then
                then(result).isEqualTo("Template with {{MESSAGE}}");
            }
        }

        @Nested
        @DisplayName("when content has no variables")
        class Context_with_no_variables_in_content {

            @Test
            @DisplayName("it returns original content")
            void it_returns_original_content() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT, "Plain text content");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.MESSAGE, "Hello");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Plain text content");
            }
        }

        @Nested
        @DisplayName("when content has single variable")
        class Context_with_single_variable {

            @Test
            @DisplayName("it replaces variable with value")
            void it_replaces_variable_with_value() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT, "User message: {{MESSAGE}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.MESSAGE, "Hello World");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("User message: Hello World");
            }
        }

        @Nested
        @DisplayName("when content has multiple variables")
        class Context_with_multiple_variables {

            @Test
            @DisplayName("it replaces all variables with values")
            void it_replaces_all_variables_with_values() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT,
                                "Previous: {{PREVIOUS_CONVERSATIONS}}, Current: {{MESSAGE}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.PREVIOUS_CONVERSATIONS, "History");
                variables.put(ConversationContextType.MESSAGE, "New message");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Previous: History, Current: New message");
            }
        }

        @Nested
        @DisplayName("when variable appears multiple times")
        class Context_with_repeated_variable {

            @Test
            @DisplayName("it replaces all occurrences")
            void it_replaces_all_occurrences() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT,
                                "First: {{MESSAGE}}, Second: {{MESSAGE}}, Third: {{MESSAGE}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.MESSAGE, "Repeated");

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("First: Repeated, Second: Repeated, Third: Repeated");
            }
        }

        @Nested
        @DisplayName("when variable is missing in map")
        class Context_with_missing_variable_in_map {

            @Test
            @DisplayName("it replaces with empty string")
            void it_replaces_with_empty_string() {
                // given
                var script =
                        new ConversationScript(
                                ConversationType.CHAT_ASSISTANT,
                                "Message: {{MESSAGE}}, Previous: {{PREVIOUS_CONVERSATIONS}}");
                Map<ConversationContextType, String> variables = new HashMap<>();
                variables.put(ConversationContextType.MESSAGE, "Hello");
                // PREVIOUS_CONVERSATIONS not provided

                // when
                String result = script.render(variables);

                // then
                then(result).isEqualTo("Message: Hello, Previous: ");
            }
        }
    }
}
