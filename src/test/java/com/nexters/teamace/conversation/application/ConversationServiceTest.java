package com.nexters.teamace.conversation.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.nexters.teamace.conversation.domain.ConversationContextType;
import com.nexters.teamace.conversation.domain.ConversationScript;
import com.nexters.teamace.conversation.domain.ConversationType;
import com.nexters.teamace.conversation.domain.MessageConversation;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ConversationService")
@ExtendWith(MockitoExtension.class)
class ConversationServiceTest {

    @Mock private ConversationClient conversationClient;
    @Mock private ConversationScriptService conversationScriptService;
    @Mock private ConversationScript conversationScript;

    @InjectMocks private ConversationService conversationService;

    @Nested
    @DisplayName("채팅")
    class Describe_chat {

        @Nested
        @DisplayName("모든 필수 변수가 제공되었을 때")
        class Context_with_all_required_variables {

            @Test
            @DisplayName("대화를 성공적으로 처리한다")
            void it_processes_conversation_successfully() {
                // given
                Map<ConversationContextType, String> variables =
                        Map.of(ConversationContextType.CONVERSATION_STAGE, "1단계");
                ConversationContext context = new ConversationContext("session123", variables);

                given(conversationScriptService.getPromptTemplate(ConversationType.CHAT_ASSISTANT))
                        .willReturn(conversationScript);
                given(conversationScript.render(variables)).willReturn("rendered script");
                given(
                                conversationClient.chat(
                                        MessageConversation.class, "rendered script", "Hello"))
                        .willReturn(new MessageConversation("AI response"));

                // when
                MessageConversation result =
                        conversationService.chat(
                                MessageConversation.class,
                                ConversationType.CHAT_ASSISTANT,
                                context,
                                "Hello");

                // then
                then(result.message()).isEqualTo("AI response");
                verify(conversationScriptService)
                        .getPromptTemplate(ConversationType.CHAT_ASSISTANT);
                verify(conversationScript).validateVariables(variables);
                verify(conversationScript).render(variables);
                verify(conversationClient)
                        .chat(MessageConversation.class, "rendered script", "Hello");
            }
        }

        @Nested
        @DisplayName("필수 변수가 누락되었을 때")
        class Context_with_missing_required_variables {

            @Test
            @DisplayName("IllegalArgumentException을 발생시킨다")
            void it_throws_illegal_argument_exception() {
                // given
                Map<ConversationContextType, String> variables =
                        Map.of(); // CONVERSATION_STAGE is missing - required for CHAT_ASSISTANT
                ConversationContext context = new ConversationContext("session123", variables);

                given(conversationScriptService.getPromptTemplate(ConversationType.CHAT_ASSISTANT))
                        .willReturn(conversationScript);
                doThrow(
                                new IllegalArgumentException(
                                        "Missing required variables for template 'CHAT_ASSISTANT': CONVERSATION_STAGE"))
                        .when(conversationScript)
                        .validateVariables(variables);

                // when & then
                thenThrownBy(
                                () ->
                                        conversationService.chat(
                                                MessageConversation.class,
                                                ConversationType.CHAT_ASSISTANT,
                                                context,
                                                "Hello"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(
                                "Missing required variables for template 'CHAT_ASSISTANT'")
                        .hasMessageContaining("CONVERSATION_STAGE");
            }
        }

        @Nested
        @DisplayName("필수 변수가 빈 값을 가질 때")
        class Context_with_empty_required_variable {

            @Test
            @DisplayName("빈 문자열을 유효한 값으로 허용한다")
            void it_accepts_empty_string_as_valid_value() {
                // given
                Map<ConversationContextType, String> variables =
                        Map.of(ConversationContextType.CONVERSATION_STAGE, "1단계");
                ConversationContext context = new ConversationContext("session123", variables);

                given(conversationScriptService.getPromptTemplate(any()))
                        .willReturn(conversationScript);
                given(conversationScript.render(any())).willReturn("rendered script");
                given(conversationClient.chat(any(), anyString(), anyString()))
                        .willReturn(new MessageConversation("AI response"));

                // when & then - should not throw exception
                MessageConversation result =
                        conversationService.chat(
                                MessageConversation.class,
                                ConversationType.CHAT_ASSISTANT,
                                context,
                                ""); // Empty userMessage is valid

                then(result).isNotNull();
            }
        }

        @Nested
        @DisplayName("EMOTION_ANALYSIS 타입을 사용할 때")
        class Context_with_emotion_analysis_type {

            @Test
            @DisplayName("EMOTION_ANALYSIS의 필수 변수를 검증한다")
            void it_validates_emotion_analysis_variables() {
                // given
                Map<ConversationContextType, String> variables =
                        Map.of(
                                ConversationContextType.PREVIOUS_CONVERSATIONS,
                                "Previous chat history");
                ConversationContext context = new ConversationContext("session123", variables);

                given(conversationScriptService.getPromptTemplate(any()))
                        .willReturn(conversationScript);
                given(conversationScript.render(any())).willReturn("rendered script");
                given(conversationClient.chat(any(), anyString(), anyString()))
                        .willReturn(new MessageConversation("AI response"));

                // when
                MessageConversation result =
                        conversationService.chat(
                                MessageConversation.class,
                                ConversationType.EMOTION_ANALYSIS,
                                context);

                // then
                then(result).isNotNull();
            }

            @Test
            @DisplayName("빈 PREVIOUS_CONVERSATIONS를 유효한 값으로 허용한다")
            void it_accepts_empty_previous_conversations() {
                // given
                Map<ConversationContextType, String> variables =
                        Map.of(
                                ConversationContextType.PREVIOUS_CONVERSATIONS,
                                ""); // Empty string is valid for first conversation
                ConversationContext context = new ConversationContext("session123", variables);

                given(conversationScriptService.getPromptTemplate(any()))
                        .willReturn(conversationScript);
                given(conversationScript.render(any())).willReturn("rendered script");
                given(conversationClient.chat(any(), anyString(), anyString()))
                        .willReturn(new MessageConversation("AI response"));

                // when & then - should not throw exception
                MessageConversation result =
                        conversationService.chat(
                                MessageConversation.class,
                                ConversationType.EMOTION_ANALYSIS,
                                context);

                then(result).isNotNull();
            }

            @Test
            @DisplayName("PREVIOUS_CONVERSATIONS가 누락되면 예외를 발생시킨다")
            void it_throws_exception_when_previous_conversations_missing() {
                // given
                Map<ConversationContextType, String> variables =
                        Map.of(); // PREVIOUS_CONVERSATIONS is missing - required for
                // EMOTION_ANALYSIS
                ConversationContext context = new ConversationContext("session123", variables);

                given(
                                conversationScriptService.getPromptTemplate(
                                        ConversationType.EMOTION_ANALYSIS))
                        .willReturn(conversationScript);
                doThrow(
                                new IllegalArgumentException(
                                        "Missing required variables for template 'EMOTION_ANALYSIS': PREVIOUS_CONVERSATIONS"))
                        .when(conversationScript)
                        .validateVariables(variables);

                // when & then
                thenThrownBy(
                                () ->
                                        conversationService.chat(
                                                MessageConversation.class,
                                                ConversationType.EMOTION_ANALYSIS,
                                                context))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(
                                "Missing required variables for template 'EMOTION_ANALYSIS'")
                        .hasMessageContaining("PREVIOUS_CONVERSATIONS");
            }
        }
    }
}
