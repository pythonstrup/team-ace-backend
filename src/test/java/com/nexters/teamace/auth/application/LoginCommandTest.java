package com.nexters.teamace.auth.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LoginCommand")
class LoginCommandTest {

    @Nested
    @DisplayName("when creating with valid username")
    class Context_when_creating_with_valid_username {

        @Test
        @DisplayName("it creates successfully")
        void it_creates_successfully() {
            final LoginCommand command = new LoginCommand("validuser");

            then(command.username()).isEqualTo("validuser");
        }
    }

    @Nested
    @DisplayName("when creating with null username")
    class Context_when_creating_with_null_username {

        @Test
        @DisplayName("it throws IllegalArgumentException")
        void it_throws_IllegalArgumentException() {
            thenThrownBy(() -> new LoginCommand(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("username must not be blank");
        }
    }

    @Nested
    @DisplayName("when creating with blank username")
    class Context_when_creating_with_blank_username {

        @Test
        @DisplayName("it throws IllegalArgumentException")
        void it_throws_IllegalArgumentException() {
            thenThrownBy(() -> new LoginCommand("  "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("username must not be blank");
        }
    }

    @Nested
    @DisplayName("when creating with username longer than 20 characters")
    class Context_when_creating_with_username_longer_than_20_characters {

        @Test
        @DisplayName("it throws IllegalArgumentException")
        void it_throws_IllegalArgumentException() {
            final String longUsername = "a".repeat(21);

            thenThrownBy(() -> new LoginCommand(longUsername))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Username must be between 1 and 20 characters");
        }
    }
}
