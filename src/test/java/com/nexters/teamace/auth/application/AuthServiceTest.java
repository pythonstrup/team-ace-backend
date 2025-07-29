package com.nexters.teamace.auth.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

import com.nexters.teamace.common.exception.CustomException;
import com.nexters.teamace.common.utils.UseCaseIntegrationTest;
import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("AuthService")
class AuthServiceTest extends UseCaseIntegrationTest {

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;

    private String generateUserString() {
        return fixtureMonkey
                .giveMeBuilder(String.class)
                .set("$", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20))
                .sample();
    }

    @Nested
    @DisplayName("로그인")
    class Describe_login {

        @Nested
        @DisplayName("기존 사용자일 때")
        class Context_when_existing_user {

            @Test
            @DisplayName("토큰과 함께 로그인 결과를 반환한다")
            void it_returns_login_result_with_tokens() {
                // 2025년 1월 1일 00:00:00 UTC
                given(systemHolder.currentTimeMillis()).willReturn(1735689600000L);

                final String username = generateUserString();
                final String nickname = generateUserString();
                userRepository.save(new User(username, nickname));

                final LoginCommand command = new LoginCommand(username);
                final LoginResult result = authService.login(command);

                then(result)
                        .isNotNull()
                        .extracting("username", "accessToken", "refreshToken")
                        .satisfies(
                                values -> {
                                    then(values.get(0)).isEqualTo(username);

                                    // Access Token 검증
                                    then((String) values.get(1))
                                            .isNotNull()
                                            .isNotEmpty()
                                            .matches(
                                                    "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$") // JWT 형식
                                            .contains("eyJhbGciOiJIUzM4NCJ9"); // 헤더 부분은 항상 동일

                                    // Refresh Token 검증
                                    then((String) values.get(2))
                                            .isNotNull()
                                            .isNotEmpty()
                                            .matches(
                                                    "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$") // JWT 형식
                                            .contains("eyJhbGciOiJIUzM4NCJ9"); // 헤더 부분은 항상 동일
                                });
            }
        }

        @Nested
        @DisplayName("존재하지 않는 사용자일 때")
        class Context_when_non_existing_user {

            @Test
            @DisplayName("USER_NOT_FOUND 에러를 던진다")
            void it_throws_CustomException() {
                final LoginCommand command = new LoginCommand("nonexistent");

                thenThrownBy(() -> authService.login(command))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue(
                                "errorType", CustomException.USER_NOT_FOUND.getErrorType());
            }
        }
    }

    @Nested
    @DisplayName("회원가입")
    class Describe_signup {

        @Nested
        @DisplayName("유효한 사용자명과 닉네임일 때")
        class Context_when_valid_username_and_nickname {

            @Test
            @DisplayName("토큰과 함께 회원가입 결과를 반환한다")
            void it_returns_signup_result_with_tokens() {
                // 2025년 1월 1일 00:00:00 UTC
                given(systemHolder.currentTimeMillis()).willReturn(1735689600000L);

                final String username = generateUserString();
                final String nickname = generateUserString();
                final SignupCommand command = new SignupCommand(username, nickname);
                final SignupResult result = authService.signup(command);

                then(result)
                        .isNotNull()
                        .extracting("username", "accessToken", "refreshToken")
                        .satisfies(
                                values -> {
                                    then(values.get(0)).isEqualTo(username);
                                    then((String) values.get(1)).isNotNull().isNotEmpty();
                                    then((String) values.get(2)).isNotNull().isNotEmpty();
                                });

                final User savedUser = userRepository.getByUsername(username);
                then(savedUser.getNickname()).isEqualTo(nickname);
            }
        }

        @Nested
        @DisplayName("사용자명이 이미 존재할 때")
        class Context_when_username_already_exists {

            @Test
            @DisplayName("USER_ALREADY_EXISTS 에러를 던진다")
            void it_throws_CustomException() {
                final String existingUsername = generateUserString();
                final String existingNickname = generateUserString();
                userRepository.save(new User(existingUsername, existingNickname));

                final String anotherNickname = generateUserString();
                final SignupCommand command = new SignupCommand(existingUsername, anotherNickname);

                thenThrownBy(() -> authService.signup(command))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue(
                                "errorType", CustomException.USER_ALREADY_EXISTS.getErrorType());
            }
        }
    }

    @Nested
    @DisplayName("토큰 갱신")
    class Describe_refreshToken {

        @Nested
        @DisplayName("유효한 리프레시 토큰일 때")
        class Context_when_valid_refresh_token {

            @Test
            @DisplayName("새로운 액세스 토큰을 반환한다")
            void it_returns_new_access_token() {
                // 현재 시간을 사용하여 유효한 토큰 생성
                final long currentTime = System.currentTimeMillis();

                // 모든 호출에 대해 현재 시간 반환
                given(systemHolder.currentTimeMillis()).willReturn(currentTime);

                final String username = generateUserString();
                final String nickname = generateUserString();
                userRepository.save(new User(username, nickname));

                final LoginCommand loginCommand = new LoginCommand(username);
                final LoginResult loginResult = authService.login(loginCommand);

                final RefreshTokenCommand command =
                        new RefreshTokenCommand(loginResult.refreshToken());
                final RefreshTokenResult result = authService.refreshToken(command);

                then(result.accessToken()).isNotNull().isNotEmpty();
            }
        }

        @Nested
        @DisplayName("유효하지 않은 리프레시 토큰일 때")
        class Context_when_invalid_refresh_token {

            @Test
            @DisplayName("INVALID_REFRESH_TOKEN 에러를 던진다")
            void it_throws_CustomException() {
                final RefreshTokenCommand command = new RefreshTokenCommand("invalid-token");

                thenThrownBy(() -> authService.refreshToken(command))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue(
                                "errorType", CustomException.INVALID_REFRESH_TOKEN.getErrorType());
            }
        }
    }
}
