package com.nexters.teamace.chat.presentation;

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
import com.nexters.teamace.common.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class ChatRoomControllerTest extends ControllerTest {

    @Test
    @DisplayName("채팅을 생성할 수 있다")
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
                                                        fieldWithPath("userId")
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
    @DisplayName("userId가 비어있으면 채팅 생성에 실패한다")
    void createChatRoom_userIdBlank_fail() throws Exception {
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
                .andExpect(jsonPath("$.error.data[0].field").value("userId"))
                .andExpect(jsonPath("$.error.data[0].message").value("userId must not be blank"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Chat")
                                                .description("채팅룸 생성 - userId 검증 실패")
                                                .requestFields(
                                                        fieldWithPath("userId")
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
}
