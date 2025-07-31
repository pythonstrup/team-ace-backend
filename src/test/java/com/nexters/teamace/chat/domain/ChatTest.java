package com.nexters.teamace.chat.domain;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChatTest {

    @Nested
    @DisplayName("새로운 채팅 생성")
    class Describe_of {

        @Nested
        @DisplayName("사용자 메시지로 채팅을 생성하면")
        class Context_when_creating_user_message {

            @Test
            @DisplayName("ID가 null인 사용자 채팅을 생성한다")
            void it_creates_user_chat_with_null_id() {
                // Given
                final Long chatRoomId = 1L;
                final MessageType type = MessageType.USER;
                final String message = "안녕하세요!";

                // When
                final Chat chat = Chat.create(chatRoomId, type, message);

                // Then
                then(chat)
                        .extracting("id", "chatRoomId", "type", "message")
                        .containsExactly(null, chatRoomId, type, message);
            }
        }

        @Nested
        @DisplayName("시스템 메시지로 채팅을 생성하면")
        class Context_when_creating_system_message {

            @Test
            @DisplayName("ID가 null인 시스템 채팅을 생성한다")
            void it_creates_system_chat_with_null_id() {
                // Given
                final Long chatRoomId = 2L;
                final MessageType type = MessageType.SYSTEM;
                final String message = "반갑습니다!";

                // When
                final Chat chat = Chat.create(chatRoomId, type, message);

                // Then
                then(chat)
                        .extracting("id", "chatRoomId", "type", "message")
                        .containsExactly(null, chatRoomId, type, message);
            }
        }
    }

    @Nested
    @DisplayName("기존 채팅 객체 생성")
    class Describe_restore {

        @Nested
        @DisplayName("모든 필드가 제공되면")
        class Context_when_all_fields_provided {

            @Test
            @DisplayName("제공된 데이터로 채팅을 생성한다")
            void it_restores_chat_with_provided_data() {
                // Given
                final Long id = 100L;
                final Long chatRoomId = 5L;
                final MessageType type = MessageType.USER;
                final String message = "복원된 메시지";

                // When
                final Chat chat = Chat.restore(id, chatRoomId, type, message);

                // Then
                then(chat)
                        .extracting("id", "chatRoomId", "type", "message")
                        .containsExactly(id, chatRoomId, type, message);
            }
        }

        @Nested
        @DisplayName("시스템 메시지를 생성하면")
        class Context_when_restoring_system_message {

            @Test
            @DisplayName("시스템 타입의 채팅을 생성한다")
            void it_restores_system_message_chat() {
                // Given
                final Long id = 200L;
                final Long chatRoomId = 10L;
                final MessageType type = MessageType.SYSTEM;
                final String message = "시스템 복원 메시지";

                // When
                final Chat chat = Chat.restore(id, chatRoomId, type, message);

                // Then
                then(chat.getType()).isEqualTo(MessageType.SYSTEM);
                then(chat.getMessage()).isEqualTo(message);
                then(chat.getId()).isEqualTo(id);
                then(chat.getChatRoomId()).isEqualTo(chatRoomId);
            }
        }
    }

    @Nested
    @DisplayName("동등성 비교")
    class Describe_equals_and_hashCode {

        @Nested
        @DisplayName("같은 ID를 가진 채팅들이면")
        class Context_with_same_id {

            @Test
            @DisplayName("동등성 비교에서 참을 반환한다")
            void it_returns_true_for_equals() {
                // Given
                final Long id = 1L;
                final Chat chat1 = Chat.restore(id, 1L, MessageType.USER, "Message 1");
                final Chat chat2 = Chat.restore(id, 2L, MessageType.SYSTEM, "Message 2");

                // Then
                then(chat1).isEqualTo(chat2);
                then(chat1.hashCode()).isEqualTo(chat2.hashCode());
            }
        }

        @Nested
        @DisplayName("다른 ID를 가진 채팅들이면")
        class Context_with_different_id {

            @Test
            @DisplayName("동등성 비교에서 거짓을 반환한다")
            void it_returns_false_for_equals() {
                // Given
                final Chat chat1 = Chat.restore(1L, 1L, MessageType.USER, "Message");
                final Chat chat2 = Chat.restore(2L, 1L, MessageType.USER, "Message");

                // Then
                then(chat1).isNotEqualTo(chat2);
                then(chat1.hashCode()).isNotEqualTo(chat2.hashCode());
            }
        }

        @Nested
        @DisplayName("ID가 null인 채팅들이면")
        class Context_with_null_id {

            @Test
            @DisplayName("동등성 비교에서 참을 반환한다")
            void it_returns_true_for_equals() {
                // Given
                final Chat chat1 = Chat.create(1L, MessageType.USER, "Message 1");
                final Chat chat2 = Chat.create(2L, MessageType.SYSTEM, "Message 2");

                // Then
                then(chat1).isEqualTo(chat2);
                then(chat1.hashCode()).isEqualTo(chat2.hashCode());
            }
        }

        @Nested
        @DisplayName("한 채팅은 ID가 있고 다른 채팅은 ID가 null이면")
        class Context_with_mixed_id_states {

            @Test
            @DisplayName("동등성 비교에서 거짓을 반환한다")
            void it_returns_false_for_equals() {
                // Given
                final Chat chatWithId = Chat.restore(1L, 1L, MessageType.USER, "Message");
                final Chat chatWithoutId = Chat.create(1L, MessageType.USER, "Message");

                // Then
                then(chatWithId).isNotEqualTo(chatWithoutId);
                then(chatWithId.hashCode()).isNotEqualTo(chatWithoutId.hashCode());
            }
        }
    }

    @Nested
    @DisplayName("필드 접근")
    class Describe_field_access {

        @Test
        @DisplayName("모든 필드에 정확하게 접근할 수 있다")
        void it_provides_access_to_all_fields() {
            // Given
            final Long id = 42L;
            final Long chatRoomId = 100L;
            final MessageType type = MessageType.USER;
            final String message = "테스트 메시지";

            // When
            final Chat chat = Chat.restore(id, chatRoomId, type, message);

            // Then
            then(chat.getId()).isEqualTo(id);
            then(chat.getChatRoomId()).isEqualTo(chatRoomId);
            then(chat.getType()).isEqualTo(type);
            then(chat.getMessage()).isEqualTo(message);
        }
    }

    @Nested
    @DisplayName("메시지 타입별 채팅")
    class Describe_message_types {

        @Test
        @DisplayName("USER 타입 채팅을 생성할 수 있다")
        void it_can_create_user_type_chat() {
            // Given
            final Long chatRoomId = 1L;
            final String message = "사용자 메시지";

            // When
            final Chat chat = Chat.create(chatRoomId, MessageType.USER, message);

            // Then
            then(chat.getType()).isEqualTo(MessageType.USER);
            then(chat.getMessage()).isEqualTo(message);
            then(chat.getChatRoomId()).isEqualTo(chatRoomId);
        }

        @Test
        @DisplayName("SYSTEM 타입 채팅을 생성할 수 있다")
        void it_can_create_system_type_chat() {
            // Given
            final Long chatRoomId = 1L;
            final String message = "시스템 메시지";

            // When
            final Chat chat = Chat.create(chatRoomId, MessageType.SYSTEM, message);

            // Then
            then(chat.getType()).isEqualTo(MessageType.SYSTEM);
            then(chat.getMessage()).isEqualTo(message);
            then(chat.getChatRoomId()).isEqualTo(chatRoomId);
        }
    }
}
