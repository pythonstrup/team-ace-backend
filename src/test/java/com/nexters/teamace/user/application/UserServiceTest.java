package com.nexters.teamace.user.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import com.nexters.teamace.common.exception.CustomException;
import com.nexters.teamace.common.utils.UseCaseIntegrationTest;
import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("UserService")
class UserServiceTest extends UseCaseIntegrationTest {

    @Autowired private UserService userService;

    @Autowired UserRepository userRepository;

    private String generateUserString() {
        return fixtureMonkey
                .giveMeBuilder(String.class)
                .set("$", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20))
                .sample();
    }

    @Nested
    @DisplayName("createUser")
    class Describe_createUser {

        @Nested
        @DisplayName("when valid username and nickname are provided")
        class Context_with_valid_username_and_nickname {

            @Test
            @DisplayName("it returns user with generated id")
            void it_returns_user_with_generated_id() {
                final String username = generateUserString();
                final String nickname = generateUserString();
                final CreateUserCommand command = new CreateUserCommand(username, nickname);

                final CreateUserResult result = userService.createUser(command);

                then(result)
                        .isNotNull()
                        .extracting("id", "username", "nickname")
                        .containsExactly(result.id(), username, nickname);
                then(result.id()).isNotNull();
            }
        }

        @Nested
        @DisplayName("when username is null")
        class Context_with_null_username {

            @Test
            @DisplayName("it returns user with null username")
            void it_returns_user_with_null_username() {
                final String nickname = generateUserString();
                final CreateUserCommand command = new CreateUserCommand(null, nickname);

                final CreateUserResult result = userService.createUser(command);

                then(result)
                        .isNotNull()
                        .extracting("id", "username", "nickname")
                        .containsExactly(result.id(), null, nickname);
            }
        }

        @Nested
        @DisplayName("when username already exists")
        class Context_when_username_already_exists {

            @Test
            @DisplayName("it throws CustomException")
            void it_throws_CustomException() {
                final String existingUsername = generateUserString();
                final String existingNickname = generateUserString();
                userRepository.save(new User(existingUsername, existingNickname));

                final String anotherNickname = generateUserString();
                final CreateUserCommand command =
                        new CreateUserCommand(existingUsername, anotherNickname);

                thenThrownBy(() -> userService.createUser(command))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue(
                                "errorType", CustomException.USER_ALREADY_EXISTS.getErrorType());
            }
        }
    }

    @Nested
    @DisplayName("getUserByUsername")
    class Describe_getUserByUsername {

        @Nested
        @DisplayName("when user exists")
        class Context_when_user_exists {

            @Test
            @DisplayName("it returns user")
            void it_returns_user() {
                final String username = generateUserString();
                final String nickname = generateUserString();
                final User user = userRepository.save(new User(username, nickname));

                final GetUserResult result = userService.getUserByUsername(user.getUsername());

                then(result)
                        .isNotNull()
                        .extracting("id", "username", "nickname")
                        .containsExactly(result.id(), username, nickname);
            }
        }

        @Nested
        @DisplayName("when user does not exist")
        class Context_when_user_does_not_exist {

            @Test
            @DisplayName("it throws CustomException")
            void it_throws_CustomException() {
                thenThrownBy(() -> userService.getUserByUsername("nonexistent"))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue(
                                "errorType", CustomException.USER_NOT_FOUND.getErrorType());
            }
        }
    }
}
