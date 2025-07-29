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
    @DisplayName("엔티티로 변환")
    class Describe_toEntity {

        @Nested
        @DisplayName("사용자가 ID를 가질 때")
        class Context_when_user_has_id {

            @Test
            @DisplayName("모든 필드를 가진 UserEntity를 반환한다")
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
        @DisplayName("사용자가 ID를 가지지 않을 때")
        class Context_when_user_has_no_id {

            @Test
            @DisplayName("null ID를 가진 UserEntity를 반환한다")
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
    @DisplayName("도메인으로 변환")
    class Describe_toDomain {

        @Nested
        @DisplayName("엔티티가 모든 필드를 가질 때")
        class Context_when_entity_has_all_fields {

            @Test
            @DisplayName("모든 필드를 가진 User를 반환한다")
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
