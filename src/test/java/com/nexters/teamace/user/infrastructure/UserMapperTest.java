package com.nexters.teamace.user.infrastructure;

import static org.assertj.core.api.BDDAssertions.then;

import com.nexters.teamace.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserMapper")
class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Nested
    @DisplayName("toEntity")
    class Describe_toEntity {

        @Nested
        @DisplayName("when user has id")
        class Context_when_user_has_id {

            @Test
            @DisplayName("it returns UserEntity with all fields")
            void it_returns_UserEntity_with_all_fields() {
                final Long id = 1L;
                final String username = "testuser";
                final String nickname = "Test User";
                final User user = new User(id, username, nickname);

                final UserEntity entity = userMapper.toEntity(user);

                then(entity)
                        .extracting("id", "username", "nickname")
                        .containsExactly(id, username, nickname);
            }
        }

        @Nested
        @DisplayName("when user has no id")
        class Context_when_user_has_no_id {

            @Test
            @DisplayName("it returns UserEntity with null id")
            void it_returns_UserEntity_with_null_id() {
                final String username = "testuser";
                final String nickname = "Test User";
                final User user = new User(username, nickname);

                final UserEntity entity = userMapper.toEntity(user);

                then(entity)
                        .extracting("id", "username", "nickname")
                        .containsExactly(null, username, nickname);
            }
        }
    }

    @Nested
    @DisplayName("toDomain")
    class Describe_toDomain {

        @Nested
        @DisplayName("when entity has all fields")
        class Context_when_entity_has_all_fields {

            @Test
            @DisplayName("it returns User with all fields")
            void it_returns_User_with_all_fields() {
                final Long id = 1L;
                final String username = "testuser";
                final String nickname = "Test User";
                final UserEntity entity = new UserEntity(id, username, nickname);

                final User user = userMapper.toDomain(entity);

                then(user)
                        .extracting("id", "username", "nickname")
                        .containsExactly(id, username, nickname);
            }
        }
    }
}
