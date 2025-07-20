package com.nexters.teamace.chat.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.nexters.teamace.chat.application.ChatRoomCommand;
import com.nexters.teamace.chat.application.ChatRoomResult;
import com.nexters.teamace.chat.application.SendMessageCommand;
import com.nexters.teamace.chat.application.SendMessageResult;
import com.nexters.teamace.common.annotation.WithMockCustomUser;
import com.nexters.teamace.common.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ChatRoomControllerTest extends ControllerTest {

    @Test
    @DisplayName("채팅을 생성할 수 있다")
    @WithMockCustomUser
    void createChatRoom() throws Exception {
        // given
        final ChatRoomRequest request = new ChatRoomRequest("user123");
        final String requestBody = objectMapper.writeValueAsString(request);

        final ChatRoomResult chatRoom = new ChatRoomResult(1L, "첫번째 채팅");
        given(chatRoomService.createChat(any(ChatRoomCommand.class))).willReturn(chatRoom);

        final ChatRoomResponse response = new ChatRoomResponse(1L, "첫번째 채팅");

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/chat-rooms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", equalTo(asParsedJson(response))))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Chat")
                                                .description("채팅룸 생성")
                                                .requestFields(
                                                        fieldWithPath("username")
                                                                .type(JsonFieldType.STRING)
                                                                .description("사용자 ID"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.OBJECT)
                                                                .description("응답 데이터"),
                                                        fieldWithPath("data.chatRoomId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("생성된 채팅방 ID"),
                                                        fieldWithPath("data.chat")
                                                                .type(JsonFieldType.STRING)
                                                                .description("첫번째 채팅"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.NULL)
                                                                .description("에러 정보 (성공 시 null)"))
                                                .requestSchema(schema("ChatRoomRequest"))
                                                .responseSchema(schema("ChatRoomResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("username이 비어있으면 채팅 생성에 실패한다")
    @WithMockCustomUser
    void createChatRoom_usernameBlank_fail() throws Exception {
        // given
        final ChatRoomRequest request = new ChatRoomRequest("");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/chat-rooms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("E400"))
                .andExpect(jsonPath("$.error.data[0].field").value("username"))
                .andExpect(jsonPath("$.error.data[0].message").value("username must not be blank"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Chat")
                                                .description("채팅룸 생성")
                                                .requestFields(
                                                        fieldWithPath("username")
                                                                .type(JsonFieldType.STRING)
                                                                .description("사용자 ID (빈 값 불가)"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부 (false)"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.NULL)
                                                                .description("응답 데이터 (실패 시 null)"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.OBJECT)
                                                                .description("에러 정보"),
                                                        fieldWithPath("error.code")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 코드"),
                                                        fieldWithPath("error.message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 메시지"),
                                                        fieldWithPath("error.data")
                                                                .type(JsonFieldType.ARRAY)
                                                                .description("검증 실패 상세 정보"),
                                                        fieldWithPath("error.data[].field")
                                                                .type(JsonFieldType.STRING)
                                                                .description("검증 실패 필드명"),
                                                        fieldWithPath("error.data[].message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("검증 실패 메시지"))
                                                .requestSchema(schema("ChatRoomRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("인증 없이 채팅방 생성 요청 시 401 Unauthorized를 반환한다")
    void createChatRoom_withoutAuth_unauthorized() throws Exception {
        // given
        final ChatRoomRequest request = new ChatRoomRequest("user123");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/chat-rooms")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("E40100"))
                .andExpect(jsonPath("$.error.message").value("Invalid or expired token"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Chat")
                                                .description("채팅룸 생성 - 인증 실패")
                                                .requestFields(
                                                        fieldWithPath("username")
                                                                .type(JsonFieldType.STRING)
                                                                .description("사용자 ID"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부 (false)"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.NULL)
                                                                .description("응답 데이터 (실패 시 null)"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.OBJECT)
                                                                .description("에러 정보"),
                                                        fieldWithPath("error.code")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 코드 (E401)"),
                                                        fieldWithPath("error.message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 메시지"),
                                                        fieldWithPath("error.data")
                                                                .type(JsonFieldType.NULL)
                                                                .description("추가 에러 데이터 (null)"))
                                                .requestSchema(schema("ChatRoomRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("채팅 메시지를 전송할 수 있다")
    @WithMockCustomUser
    void sendMessage() throws Exception {
        // given
        final Long chatRoomId = 1L;
        final ChatMessageRequest request = new ChatMessageRequest("Hello AI!");
        final String requestBody = objectMapper.writeValueAsString(request);

        final SendMessageResult result = new SendMessageResult("Hello! How can I help you today?");
        given(chatRoomService.sendMessage(any(SendMessageCommand.class))).willReturn(result);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/chat-rooms/{chatRoomId}/messages", chatRoomId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Hello! How can I help you today?"))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Chat")
                                                .description("채팅 메시지 전송")
                                                .pathParameters(
                                                        parameterWithName("chatRoomId")
                                                                .description("채팅방 ID"))
                                                .requestFields(
                                                        fieldWithPath("message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("전송할 메시지"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.OBJECT)
                                                                .description("응답 데이터"),
                                                        fieldWithPath("data.message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("AI 응답 메시지"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.NULL)
                                                                .description("에러 정보 (성공 시 null)"))
                                                .requestSchema(schema("ChatMessageRequest"))
                                                .responseSchema(schema("ChatMessageResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("메시지가 비어있으면 전송에 실패한다")
    @WithMockCustomUser
    void sendMessage_messageBlank_fail() throws Exception {
        // given
        final Long chatRoomId = 1L;
        final ChatMessageRequest request = new ChatMessageRequest("");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/chat-rooms/{chatRoomId}/messages", chatRoomId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("E400"))
                .andExpect(jsonPath("$.error.data[0].field").value("message"))
                .andExpect(jsonPath("$.error.data[0].message").value("message must not be blank"));
    }

    @Test
    @DisplayName("인증 없이 메시지 전송 요청 시 401 Unauthorized를 반환한다")
    void sendMessage_withoutAuth_unauthorized() throws Exception {
        // given
        final Long chatRoomId = 1L;
        final ChatMessageRequest request = new ChatMessageRequest("Hello AI!");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/chat-rooms/{chatRoomId}/messages", chatRoomId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("E40100"))
                .andExpect(jsonPath("$.error.message").value("Invalid or expired token"));
    }
}
