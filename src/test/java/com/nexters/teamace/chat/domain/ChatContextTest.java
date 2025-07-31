package com.nexters.teamace.chat.domain;

import static com.nexters.teamace.common.exception.ValidationErrorMessage.CHAT_ROOM_ID_NOT_NULL;
import static com.nexters.teamace.common.exception.ValidationErrorMessage.CHAT_ROOM_ID_POSITIVE;
import static com.nexters.teamace.common.exception.ValidationErrorMessage.PREVIOUS_CHATS_NOT_NULL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("채팅 컨텍스트")
class ChatContextTest {

    @Nested
    @DisplayName("유효한 파라미터로 생성")
    class Describe_valid_creation {

        @Nested
        @DisplayName("유효한 채팅방 ID와 빈 채팅 목록이 제공되면")
        class Context_with_valid_chatRoomId_and_empty_chats {

            @Test
            @DisplayName("채팅 컨텍스트를 생성한다")
            void it_creates_chat_context() {
                // Given
                final Long chatRoomId = 1L;
                final List<Chat> previousChats = List.of();

                // When
                final ChatContext chatContext = new ChatContext(chatRoomId, previousChats);

                // Then
                then(chatContext.chatRoomId()).isEqualTo(chatRoomId);
                then(chatContext.previousChats()).isEqualTo(previousChats);
                then(chatContext.previousChats()).isEmpty();
            }
        }

        @Nested
        @DisplayName("유효한 채팅방 ID와 채팅 목록이 제공되면")
        class Context_with_valid_chatRoomId_and_chats {

            @Test
            @DisplayName("채팅 컨텍스트를 생성한다")
            void it_creates_chat_context() {
                // Given
                final Long chatRoomId = 5L;
                final Chat chat1 = Chat.create(chatRoomId, MessageType.USER, "Hello");
                final Chat chat2 = Chat.create(chatRoomId, MessageType.SYSTEM, "Hi there");
                final List<Chat> previousChats = List.of(chat1, chat2);

                // When
                final ChatContext chatContext = new ChatContext(chatRoomId, previousChats);

                // Then
                then(chatContext.chatRoomId()).isEqualTo(chatRoomId);
                then(chatContext.previousChats()).isEqualTo(previousChats);
                then(chatContext.previousChats()).hasSize(2);
            }
        }
    }

    @Nested
    @DisplayName("채팅방 ID 검증")
    class Describe_chatRoomId_validation {

        @Nested
        @DisplayName("채팅방 ID가 null이면")
        class Context_when_chatRoomId_is_null {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_IllegalArgumentException() {
                // Given
                final Long chatRoomId = null;
                final List<Chat> previousChats = List.of();

                // When & Then
                thenThrownBy(() -> new ChatContext(chatRoomId, previousChats))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage(CHAT_ROOM_ID_NOT_NULL);
            }
        }

        @Nested
        @DisplayName("채팅방 ID가 0이면")
        class Context_when_chatRoomId_is_zero {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_IllegalArgumentException() {
                // Given
                final Long chatRoomId = 0L;
                final List<Chat> previousChats = List.of();

                // When & Then
                thenThrownBy(() -> new ChatContext(chatRoomId, previousChats))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage(CHAT_ROOM_ID_POSITIVE);
            }
        }

        @Nested
        @DisplayName("채팅방 ID가 음수이면")
        class Context_when_chatRoomId_is_negative {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_IllegalArgumentException() {
                // Given
                final Long chatRoomId = -1L;
                final List<Chat> previousChats = List.of();

                // When & Then
                thenThrownBy(() -> new ChatContext(chatRoomId, previousChats))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage(CHAT_ROOM_ID_POSITIVE);
            }
        }

        @Nested
        @DisplayName("채팅방 ID가 1이면")
        class Context_when_chatRoomId_is_one {

            @Test
            @DisplayName("채팅 컨텍스트를 생성한다")
            void it_creates_chat_context() {
                // Given
                final Long chatRoomId = 1L;
                final List<Chat> previousChats = List.of();

                // When
                final ChatContext chatContext = new ChatContext(chatRoomId, previousChats);

                // Then
                then(chatContext.chatRoomId()).isEqualTo(chatRoomId);
            }
        }
    }

    @Nested
    @DisplayName("이전 채팅 목록 검증")
    class Describe_previousChats_validation {

        @Nested
        @DisplayName("이전 채팅 목록이 null이면")
        class Context_when_previousChats_is_null {

            @Test
            @DisplayName("IllegalArgumentException을 던진다")
            void it_throws_IllegalArgumentException() {
                // Given
                final Long chatRoomId = 1L;
                final List<Chat> previousChats = null;

                // When & Then
                thenThrownBy(() -> new ChatContext(chatRoomId, previousChats))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage(PREVIOUS_CHATS_NOT_NULL);
            }
        }
    }
}
