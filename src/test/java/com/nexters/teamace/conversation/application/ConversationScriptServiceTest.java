package com.nexters.teamace.conversation.application;

import static org.assertj.core.api.BDDAssertions.then;

import com.nexters.teamace.common.utils.UseCaseIntegrationTest;
import com.nexters.teamace.conversation.domain.ConversationContextType;
import com.nexters.teamace.conversation.domain.ConversationScript;
import com.nexters.teamace.conversation.domain.ConversationType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ConversationScriptService")
class ConversationScriptServiceTest extends UseCaseIntegrationTest {

    @Autowired private ConversationScriptService conversationScriptService;

    @Nested
    @DisplayName("프롬프트 템플릿 조회")
    class Describe_getPromptTemplate {

        @Nested
        @DisplayName("ConversationType이 CHAT_ASSISTANT일 때")
        class Context_with_chat_assistant_type {

            @Test
            @DisplayName("채팅 어시스턴트 템플릿을 가진 ConversationScript를 반환한다")
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
        @DisplayName("ConversationType이 EMOTION_ANALYSIS일 때")
        class Context_with_emotion_analysis_type {

            @Test
            @DisplayName("감정 분석 템플릿을 가진 ConversationScript를 반환한다")
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
    @DisplayName("스크립트 렌더링")
    class Describe_renderScript {

        @Nested
        @DisplayName("유효한 ConversationType과 ConversationContext가 주어졌을 때")
        class Context_with_valid_type_and_context {

            @Test
            @DisplayName("변수가 대체된 템플릿을 렌더링한다")
            void it_renders_template_with_variables_replaced() {
                // given
                String previousConversations = "이전 메시지 1\n이전 메시지 2";
                Map<ConversationContextType, String> variables =
                        Map.of(
                                ConversationContextType.PREVIOUS_CONVERSATIONS,
                                previousConversations,
                                ConversationContextType.CONVERSATION_STAGE,
                                "1단계(감정인식)");

                // when
                ConversationScript script =
                        conversationScriptService.getPromptTemplate(
                                ConversationType.CHAT_ASSISTANT);
                String renderedScript = script.render(variables);

                // then
                then(renderedScript)
                        .isNotNull()
                        .contains(previousConversations)
                        .doesNotContain("{{PREVIOUS_CONVERSATIONS}}", "{{CONVERSATION_STAGE}}");
            }
        }
    }
}
