package com.nexters.teamace.auth.presentation;

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
import com.nexters.teamace.auth.application.LoginCommand;
import com.nexters.teamace.auth.application.LoginResult;
import com.nexters.teamace.auth.application.RefreshTokenCommand;
import com.nexters.teamace.auth.application.RefreshTokenResult;
import com.nexters.teamace.auth.application.SignupCommand;
import com.nexters.teamace.auth.application.SignupResult;
import com.nexters.teamace.common.utils.ControllerTest;
import com.nexters.teamace.user.application.CreateUserCommand;
import com.nexters.teamace.user.application.CreateUserResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class AuthControllerTest extends ControllerTest {

    @Test
    @DisplayName("로그인을 할 수 있다")
    void login() throws Exception {
        // given
        final LoginRequest request = new LoginRequest("test-user");
        final String requestBody = objectMapper.writeValueAsString(request);

        final LoginResult loginResult =
                new LoginResult("test-user", "access-token", "refresh-token");
        given(authService.login(any(LoginCommand.class))).willReturn(loginResult);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        final LoginResponse expectedResponse =
                new LoginResponse("test-user", "access-token", "refresh-token");

        // then
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
                                                .tag("Auth")
                                                .description("로그인")
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
                                                        fieldWithPath("data.username")
                                                                .type(JsonFieldType.STRING)
                                                                .description("사용자 ID"),
                                                        fieldWithPath("data.accessToken")
                                                                .type(JsonFieldType.STRING)
                                                                .description("액세스 토큰"),
                                                        fieldWithPath("data.refreshToken")
                                                                .type(JsonFieldType.STRING)
                                                                .description("리프레시 토큰"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.NULL)
                                                                .description("에러 정보 (성공 시 null)"))
                                                .requestSchema(schema("LoginRequest"))
                                                .responseSchema(schema("LoginResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("username이 비어있으면 로그인에 실패한다")
    void login_usernameBlank_fail() throws Exception {
        // given
        final LoginRequest request = new LoginRequest("");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/auth/login")
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
                                                .tag("Auth")
                                                .description("로그인 - username 검증 실패")
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
                                                .requestSchema(schema("LoginRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("회원가입을 할 수 있다")
    void signup() throws Exception {
        // given
        final SignupRequest request = new SignupRequest("test-user", "테스트유저");
        final String requestBody = objectMapper.writeValueAsString(request);

        final CreateUserResult createUserResult = new CreateUserResult(1L, "test-user", "테스트유저");
        given(userService.createUser(any(CreateUserCommand.class))).willReturn(createUserResult);

        final SignupResult signupResult =
                new SignupResult("test-user", "access-token", "refresh-token");
        given(authService.signup(any(SignupCommand.class))).willReturn(signupResult);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        final SignupResponse expectedResponse =
                new SignupResponse("access-token", "refresh-token", "test-user");

        // then
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
                                                .tag("Auth")
                                                .description("회원가입")
                                                .requestFields(
                                                        fieldWithPath("username")
                                                                .type(JsonFieldType.STRING)
                                                                .description("사용자명 (3-20자)"),
                                                        fieldWithPath("nickname")
                                                                .type(JsonFieldType.STRING)
                                                                .description("닉네임 (2-20자)"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.OBJECT)
                                                                .description("응답 데이터"),
                                                        fieldWithPath("data.accessToken")
                                                                .type(JsonFieldType.STRING)
                                                                .description("액세스 토큰"),
                                                        fieldWithPath("data.refreshToken")
                                                                .type(JsonFieldType.STRING)
                                                                .description("리프레시 토큰"),
                                                        fieldWithPath("data.username")
                                                                .type(JsonFieldType.STRING)
                                                                .description("사용자명"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.NULL)
                                                                .description("에러 정보 (성공 시 null)"))
                                                .requestSchema(schema("SignupRequest"))
                                                .responseSchema(schema("SignupResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("username이 비어있으면 회원가입에 실패한다")
    void signup_usernameBlank_fail() throws Exception {
        // given
        final SignupRequest request = new SignupRequest("", "테스트유저");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/auth/signup")
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
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Auth")
                                                .description("회원가입 - username 검증 실패")
                                                .requestFields(
                                                        fieldWithPath("username")
                                                                .type(JsonFieldType.STRING)
                                                                .description("사용자명 (빈 값 불가)"),
                                                        fieldWithPath("nickname")
                                                                .type(JsonFieldType.STRING)
                                                                .description("닉네임"))
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
                                                .requestSchema(schema("SignupRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("토큰을 리프레시할 수 있다")
    void refreshToken() throws Exception {
        // given
        final RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");
        final String requestBody = objectMapper.writeValueAsString(request);

        final RefreshTokenResult refreshTokenResult = new RefreshTokenResult("new-access-token");
        given(authService.refreshToken(any(RefreshTokenCommand.class)))
                .willReturn(refreshTokenResult);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/auth/token/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        final RefreshTokenResponse expectedResponse = new RefreshTokenResponse("new-access-token");

        // then
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
                                                .tag("Auth")
                                                .description("토큰 리프레시")
                                                .requestFields(
                                                        fieldWithPath("refreshToken")
                                                                .type(JsonFieldType.STRING)
                                                                .description("리프레시 토큰"))
                                                .responseFields(
                                                        fieldWithPath("success")
                                                                .type(JsonFieldType.BOOLEAN)
                                                                .description("성공 여부"),
                                                        fieldWithPath("data")
                                                                .type(JsonFieldType.OBJECT)
                                                                .description("응답 데이터"),
                                                        fieldWithPath("data.accessToken")
                                                                .type(JsonFieldType.STRING)
                                                                .description("새로운 액세스 토큰"),
                                                        fieldWithPath("error")
                                                                .type(JsonFieldType.NULL)
                                                                .description("에러 정보 (성공 시 null)"))
                                                .requestSchema(schema("RefreshTokenRequest"))
                                                .responseSchema(schema("RefreshTokenResponse"))
                                                .build())));
    }

    @Test
    @DisplayName("refreshToken이 비어있으면 토큰 리프레시에 실패한다")
    void refreshToken_tokenBlank_fail() throws Exception {
        // given
        final RefreshTokenRequest request = new RefreshTokenRequest("");
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        final ResultActions resultActions =
                mockMvc.perform(
                        post("/api/v1/auth/token/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.code").value("E400"))
                .andExpect(jsonPath("$.error.data[0].field").value("refreshToken"))
                .andExpect(
                        jsonPath("$.error.data[0].message")
                                .value("refresh token must not be blank"))
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class_name}/{method_name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("Auth")
                                                .description("토큰 리프레시 - refreshToken 검증 실패")
                                                .requestFields(
                                                        fieldWithPath("refreshToken")
                                                                .type(JsonFieldType.STRING)
                                                                .description("리프레시 토큰 (빈 값 불가)"))
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
                                                .requestSchema(schema("RefreshTokenRequest"))
                                                .responseSchema(schema("ErrorResponse"))
                                                .build())));
    }
}
