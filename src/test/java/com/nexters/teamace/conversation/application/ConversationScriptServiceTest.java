package com.nexters.teamace.conversation.application;

import static org.assertj.core.api.BDDAssertions.then;

import com.nexters.teamace.common.utils.UseCaseIntegrationTest;
import com.nexters.teamace.conversation.domain.ConversationScript;
import com.nexters.teamace.conversation.domain.ConversationType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ConversationScriptService")
class ConversationScriptServiceTest extends UseCaseIntegrationTest {

    @Autowired private ConversationScriptService conversationScriptService;

    @Nested
    @DisplayName("getPromptTemplate")
    class Describe_getPromptTemplate {

        @Nested
        @DisplayName("when ConversationType is CHAT_ASSISTANT")
        class Context_with_chat_assistant_type {

            @Test
            @DisplayName("it returns ConversationScript with chat assistant template")
            void it_returns_conversation_script_with_chat_assistant_template() {
                // when
                ConversationScript result =
                        conversationScriptService.getPromptTemplate(
                                ConversationType.CHAT_ASSISTANT);

                // then
                then(result).extracting("type").isEqualTo(ConversationType.CHAT_ASSISTANT);
            }
        }

        @Nested
        @DisplayName("when ConversationType is EMOTION_ANALYSIS")
        class Context_with_emotion_analysis_type {

            @Test
            @DisplayName("it returns ConversationScript with emotion analysis template")
            void it_returns_conversation_script_with_emotion_analysis_template() {
                // when
                ConversationScript result =
                        conversationScriptService.getPromptTemplate(
                                ConversationType.EMOTION_ANALYSIS);

                // then
                then(result)
                        .isNotNull()
                        .extracting("type")
                        .isEqualTo(ConversationType.EMOTION_ANALYSIS);
            }
        }
    }

    @Nested
    @DisplayName("renderScript")
    class Describe_renderScript {

        @Nested
        @DisplayName("when valid ConversationType and ConversationContext are provided")
        class Context_with_valid_type_and_context {

            @Test
            @DisplayName("it renders template with variables replaced")
            void it_renders_template_with_variables_replaced() {
                // given
                String sessionKey = "test-session";
                List<String> previousMessages = List.of("이전 메시지 1", "이전 메시지 2");
                ConversationContext context = new ConversationContext(sessionKey, previousMessages);
                String userMessage = "안녕하세요";

                // when
                String renderedScript =
                        conversationScriptService.renderScript(
                                ConversationType.CHAT_ASSISTANT, context, userMessage);

                // then
                then(renderedScript)
                        .isNotNull()
                        .contains(userMessage, "이전 메시지 1\n이전 메시지 2")
                        .doesNotContain("{{MESSAGE}}", "{{PREVIOUS_CONVERSATIONS}}");
            }
        }
    }
}
