package com.nexters.teamace.user.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import com.nexters.teamace.common.exception.CustomException;
import com.nexters.teamace.common.utils.UseCaseIntegrationTest;
import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("UserService")
class UserServiceTest extends UseCaseIntegrationTest {

    @Autowired private UserService userService;

    @Autowired UserRepository userRepository;

    @Nested
    @DisplayName("createUser")
    class Describe_createUser {

        @Nested
        @DisplayName("when valid username and nickname are provided")
        class Context_with_valid_username_and_nickname {

            @Test
            @DisplayName("it returns user with generated id")
            void it_returns_user_with_generated_id() {
                final CreateUserCommand command =
                        new CreateUserCommand("validuser123", "Valid User");

                final CreateUserResult result = userService.createUser(command);

                then(result)
                        .isNotNull()
                        .extracting("id", "username", "nickname")
                        .containsExactly(result.id(), "validuser123", "Valid User");
                then(result.id()).isNotNull();
            }
        }

        @Nested
        @DisplayName("when username is null")
        class Context_with_null_username {

            @Test
            @DisplayName("it returns user with null username")
            void it_returns_user_with_null_username() {
                final CreateUserCommand command = new CreateUserCommand(null, "Test User");

                final CreateUserResult result = userService.createUser(command);

                then(result)
                        .isNotNull()
                        .extracting("id", "username", "nickname")
                        .containsExactly(result.id(), null, "Test User");
            }
        }

        @Nested
        @DisplayName("when username already exists")
        class Context_when_username_already_exists {

            @Test
            @DisplayName("it throws CustomException")
            void it_throws_CustomException() {
                userRepository.save(new User("existinguser", "Existing User"));

                final CreateUserCommand command =
                        new CreateUserCommand("existinguser", "Another User");

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
                final User user = userRepository.save(new User("existinguser", "Existing User"));

                final GetUserResult result = userService.getUserByUsername(user.getUsername());

                then(result)
                        .isNotNull()
                        .extracting("id", "username", "nickname")
                        .containsExactly(result.id(), "existinguser", "Existing User");
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
