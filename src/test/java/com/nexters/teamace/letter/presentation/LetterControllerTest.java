package com.nexters.teamace.letter.presentation;

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
import com.nexters.teamace.common.annotation.WithMockCustomUser;
import com.nexters.teamace.common.presentation.UserInfo;
import com.nexters.teamace.common.utils.ControllerTest;
import com.nexters.teamace.letter.application.CreateLetterResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class LetterControllerTest extends ControllerTest {

    @Test
    @DisplayName("인증된 사용자는 편지를 생성할 수 있다")
    @WithMockCustomUser
    void createLetter_success() throws Exception {
        // given
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new UserInfo(1L, "test-user", "테스트유저"));

        CreateLetterResult result =
                new CreateLetterResult(1L, "눈물방울 요정", "sad.png", "따뜻한 위로의 편지입니다.");

        given(letterService.createLetter(any())).willReturn(result);

        CreateLetterRequest request = new CreateLetterRequest(100L, 1L, "슬픈 하루였어요.");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/letters")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        CreateLetterResponse expectedResponse =
                new CreateLetterResponse(1L, "눈물방울 요정", "sad.png", "따뜻한 위로의 편지입니다.");

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", equalTo(asParsedJson(expectedResponse))))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Letter")
                                                .description("편지 생성")
                                                .requestFields(
                                                        fieldWithPath("chatRoomId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("채팅방 ID"),
                                                        fieldWithPath("fairyId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("요정 ID"),
                                                        fieldWithPath("contents")
                                                                .type(JsonFieldType.STRING)
                                                                .description("편지 내용"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data.fairyId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("요정 ID"),
                                                        fieldWithPath("data.name")
                                                                .type(JsonFieldType.STRING)
                                                                .description("요정 이름"),
                                                        fieldWithPath("data.image")
                                                                .type(JsonFieldType.STRING)
                                                                .description("요정 이미지 URL"),
                                                        fieldWithPath("data.contents")
                                                                .type(JsonFieldType.STRING)
                                                                .description("생성된 편지 내용"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.NULL)
                                                                .description("에러 정보 (성공 시 null)"))
                                                .requestSchema(schema("CreateLetterRequest"))
                                                .responseSchema(schema("CreateLetterResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 401 Unauthorized 에러를 받는다")
    void createLetter_unauthorized() throws Exception {
        // given
        CreateLetterRequest request = new CreateLetterRequest(100L, 1L, "슬픈 하루였어요.");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/letters")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("chatRoomId가 null이면 400 Bad Request 에러를 받는다")
    @WithMockCustomUser
    void createLetter_fail_when_chatRoomId_is_null() throws Exception {
        // given
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new UserInfo(1L, "test-user", "테스트유저"));

        CreateLetterRequest request = new CreateLetterRequest(null, 1L, "슬픈 하루였어요.");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/letters")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.data[0].message").value("ChatRoom ID cannot be null"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Letter")
                                                .description("편지 생성")
                                                .requestFields(
                                                        fieldWithPath("chatRoomId")
                                                                .type(JsonFieldType.NULL)
                                                                .description("채팅방 ID (필수)"),
                                                        fieldWithPath("fairyId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("요정 ID"),
                                                        fieldWithPath("contents")
                                                                .type(JsonFieldType.STRING)
                                                                .description("편지 내용"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.NULL)
                                                                .description("응답 데이터 (실패 시 null)"),
                                                        fieldWithPath("error.code")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 코드"),
                                                        fieldWithPath("error.message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("전체 에러 메시지"),
                                                        fieldWithPath("error.data")
                                                                .type(JsonFieldType.ARRAY)
                                                                .description("필드별 에러 정보"),
                                                        fieldWithPath("error.data[].field")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러가 발생한 필드"),
                                                        fieldWithPath("error.data[].message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("필드별 에러 메시지"))
                                                .requestSchema(schema("CreateLetterRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("chatRoomId가 0 이하이면 400 Bad Request 에러를 받는다")
    @WithMockCustomUser
    void createLetter_fail_when_chatRoomId_is_not_positive() throws Exception {
        // given
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new UserInfo(1L, "test-user", "테스트유저"));

        CreateLetterRequest request = new CreateLetterRequest(0L, 1L, "슬픈 하루였어요.");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/letters")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(
                        jsonPath("$.error.data[0].message")
                                .value("ChatRoom ID must be greater than or equal to 1"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Letter")
                                                .description("편지 생성")
                                                .requestFields(
                                                        fieldWithPath("chatRoomId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("채팅방 ID (1 이상이어야 함)"),
                                                        fieldWithPath("fairyId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("요정 ID"),
                                                        fieldWithPath("contents")
                                                                .type(JsonFieldType.STRING)
                                                                .description("편지 내용"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.NULL)
                                                                .description("응답 데이터 (실패 시 null)"),
                                                        fieldWithPath("error.code")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 코드"),
                                                        fieldWithPath("error.message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("전체 에러 메시지"),
                                                        fieldWithPath("error.data")
                                                                .type(JsonFieldType.ARRAY)
                                                                .description("필드별 에러 정보"),
                                                        fieldWithPath("error.data[].field")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러가 발생한 필드"),
                                                        fieldWithPath("error.data[].message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("필드별 에러 메시지"))
                                                .requestSchema(schema("CreateLetterRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("fairyId가 null이면 400 Bad Request 에러를 받는다")
    @WithMockCustomUser
    void createLetter_fail_when_fairyId_is_null() throws Exception {
        // given
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new UserInfo(1L, "test-user", "테스트유저"));

        CreateLetterRequest request = new CreateLetterRequest(100L, null, "슬픈 하루였어요.");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/letters")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.data[0].message").value("Fairy ID cannot be null"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Letter")
                                                .description("편지 생성")
                                                .requestFields(
                                                        fieldWithPath("chatRoomId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("채팅방 ID"),
                                                        fieldWithPath("fairyId")
                                                                .type(JsonFieldType.NULL)
                                                                .description("요정 ID (필수)"),
                                                        fieldWithPath("contents")
                                                                .type(JsonFieldType.STRING)
                                                                .description("편지 내용"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.NULL)
                                                                .description("응답 데이터 (실패 시 null)"),
                                                        fieldWithPath("error.code")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 코드"),
                                                        fieldWithPath("error.message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("전체 에러 메시지"),
                                                        fieldWithPath("error.data")
                                                                .type(JsonFieldType.ARRAY)
                                                                .description("필드별 에러 정보"),
                                                        fieldWithPath("error.data[].field")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러가 발생한 필드"),
                                                        fieldWithPath("error.data[].message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("필드별 에러 메시지"))
                                                .requestSchema(schema("CreateLetterRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("contents가 빈 문자열이면 400 Bad Request 에러를 받는다")
    @WithMockCustomUser
    void createLetter_fail_when_contents_is_blank() throws Exception {
        // given
        given(authUserArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(new UserInfo(1L, "test-user", "테스트유저"));

        CreateLetterRequest request = new CreateLetterRequest(100L, 1L, "");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/letters")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(
                        jsonPath("$.error.data[0].message")
                                .value("Letter contents must not be blank"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Letter")
                                                .description("편지 생성")
                                                .requestFields(
                                                        fieldWithPath("chatRoomId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("채팅방 ID"),
                                                        fieldWithPath("fairyId")
                                                                .type(JsonFieldType.NUMBER)
                                                                .description("요정 ID"),
                                                        fieldWithPath("contents")
                                                                .type(JsonFieldType.STRING)
                                                                .description("편지 내용 (빈 문자열 불가)"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.NULL)
                                                                .description("응답 데이터 (실패 시 null)"),
                                                        fieldWithPath("error.code")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러 코드"),
                                                        fieldWithPath("error.message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("전체 에러 메시지"),
                                                        fieldWithPath("error.data")
                                                                .type(JsonFieldType.ARRAY)
                                                                .description("필드별 에러 정보"),
                                                        fieldWithPath("error.data[].field")
                                                                .type(JsonFieldType.STRING)
                                                                .description("에러가 발생한 필드"),
                                                        fieldWithPath("error.data[].message")
                                                                .type(JsonFieldType.STRING)
                                                                .description("필드별 에러 메시지"))
                                                .requestSchema(schema("CreateLetterRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }
}
