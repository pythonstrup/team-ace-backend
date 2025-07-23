package com.nexters.teamace.auth.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RefreshTokenCommand")
class RefreshTokenCommandTest {

    @Nested
    @DisplayName("when creating with valid refresh token")
    class Context_when_creating_with_valid_refresh_token {

        @Test
        @DisplayName("it creates successfully")
        void it_creates_successfully() {
            final String validToken =
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0In0.test";
            final RefreshTokenCommand command = new RefreshTokenCommand(validToken);

            then(command.refreshToken()).isEqualTo(validToken);
        }
    }

    @Nested
    @DisplayName("when creating with null refresh token")
    class Context_when_creating_with_null_refresh_token {

        @Test
        @DisplayName("it throws IllegalArgumentException")
        void it_throws_IllegalArgumentException() {
            thenThrownBy(() -> new RefreshTokenCommand(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("refresh token must not be blank");
        }
    }

    @Nested
    @DisplayName("when creating with blank refresh token")
    class Context_when_creating_with_blank_refresh_token {

        @Test
        @DisplayName("it throws IllegalArgumentException")
        void it_throws_IllegalArgumentException() {
            thenThrownBy(() -> new RefreshTokenCommand("  "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("refresh token must not be blank");
        }
    }

    @Nested
    @DisplayName("when creating with empty refresh token")
    class Context_when_creating_with_empty_refresh_token {

        @Test
        @DisplayName("it throws IllegalArgumentException")
        void it_throws_IllegalArgumentException() {
            thenThrownBy(() -> new RefreshTokenCommand(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("refresh token must not be blank");
        }
    }
}
