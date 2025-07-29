package com.nexters.teamace.auth.presentation;

import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.nexters.teamace.common.utils.E2ETest;
import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("인증 E2E 테스트")
class AuthE2ETest extends E2ETest {

    @Autowired private UserRepository userRepository;

    private String generateUserString() {
        return fixtureMonkey
                .giveMeBuilder(String.class)
                .set("$", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20))
                .sample();
    }

    @Nested
    @DisplayName("회원가입")
    class Describe_signup {

        @Nested
        @DisplayName("유효한 회원가입 요청이 주어졌을 때")
        class Context_with_valid_signup_request {

            @Test
            @DisplayName("인증된 사용자명으로 감사 필드가 설정된 사용자를 생성한다")
            void it_creates_user_with_audit_fields_set_to_authenticated_username() {
                // given
                final String username = generateUserString();
                final String nickname = generateUserString();
                final SignupRequest request = new SignupRequest(username, nickname);

                // when
                final RequestSpecification requestSpec =
                        RestAssured.given()
                                .contentType(ContentType.JSON)
                                .accept(ContentType.JSON)
                                .body(request);
                final Response response = requestSpec.when().post("/auth/signup");

                // then
                response.then()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .body("data.username", equalTo(username))
                        .body("data.accessToken", notNullValue())
                        .body("data.refreshToken", notNullValue())
                        .body("error", equalTo(null));

                // 데이터베이스에서 사용자 생성 확인
                final User savedUser = userRepository.getByUsername(username);
                then(savedUser)
                        .extracting(User::getUsername, User::getNickname)
                        .containsExactly(username, nickname);
            }
        }
    }
}
