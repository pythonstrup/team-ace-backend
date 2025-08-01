package com.nexters.teamace.chat.domain;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChatRoomTest {

    @Nested
    @DisplayName("새로 저장할 채팅방 생성")
    class Describe_create {

        @Nested
        @DisplayName("유효한 사용자 ID가 제공되면")
        class Context_with_valid_userId {

            @Test
            @DisplayName("빈 채팅 목록을 가진 채팅방을 생성한다")
            void it_creates_ChatRoom_with_empty_chats() {
                // Given
                final Long userId = 1L;

                // When
                final ChatRoom chatRoom = ChatRoom.create(userId);

                // Then
                then(chatRoom).extracting("id", "userId").containsExactly(null, userId);
                then(chatRoom.getChats().isEmpty()).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("영속되어 있는 채팅방 생성")
    class Describe_restore {

        @Nested
        @DisplayName("유효한 ID, 사용자 ID, 채팅 목록이 제공되면")
        class Context_with_valid_parameters {

            @Test
            @DisplayName("제공된 데이터로 채팅방을 생성한다")
            void it_restores_ChatRoom_with_provided_data() {
                // Given
                final Long id = 1L;
                final Long userId = 2L;
                final List<Chat> chatList =
                        List.of(
                                Chat.restore(1L, id, MessageType.SYSTEM, "Welcome!"),
                                Chat.restore(2L, id, MessageType.USER, "Hello"));
                final Chats chats = new Chats(chatList);

                // When
                final ChatRoom chatRoom = ChatRoom.restore(id, userId, chats);

                // Then
                then(chatRoom).extracting("id", "userId").containsExactly(id, userId);
                then(chatRoom.getChats().size()).isEqualTo(2);
            }
        }
    }

    @Nested
    @DisplayName("시스템 메시지 추가")
    class Describe_addSystemMessage {

        @Nested
        @DisplayName("유효한 메시지가 제공되면")
        class Context_with_valid_message {

            @Test
            @DisplayName("채팅 목록에 시스템 채팅을 추가한다")
            void it_adds_system_chat_to_chats() {
                // Given
                final Long userId = 1L;
                final String message = "System message";
                final ChatRoom chatRoom = ChatRoom.create(userId);

                // When
                chatRoom.addSystemMessage(message);

                // Then
                then(chatRoom.getChats().size()).isEqualTo(1);
                then(chatRoom.getChats().getFirst())
                        .extracting("chatRoomId", "type", "message")
                        .containsExactly(chatRoom.getId(), MessageType.SYSTEM, message);
            }
        }
    }

    @Nested
    @DisplayName("사용자 메시지 추가")
    class Describe_addUserMessage {

        @Nested
        @DisplayName("유효한 메시지가 제공되면")
        class Context_with_valid_message {

            @Test
            @DisplayName("채팅 목록에 사용자 채팅을 추가한다")
            void it_adds_user_chat_to_chats() {
                // Given
                final Long userId = 1L;
                final String message = "User message";
                final ChatRoom chatRoom = ChatRoom.create(userId);

                // When
                chatRoom.addUserMessage(message);

                // Then
                then(chatRoom.getChats().size()).isEqualTo(1);
                then(chatRoom.getChats().getFirst())
                        .extracting("chatRoomId", "type", "message")
                        .containsExactly(chatRoom.getId(), MessageType.USER, message);
            }
        }
    }

    @Nested
    @DisplayName("채팅 컨텍스트 변환")
    class Describe_toChatContext {

        @Nested
        @DisplayName("채팅방에 여러 채팅이 있으면")
        class Context_with_multiple_chats {

            @Test
            @DisplayName("모든 채팅을 포함한 채팅 컨텍스트를 반환한다")
            void it_returns_ChatContext_with_all_chats() {
                // Given
                final Long chatRoomId = 1L;
                final Long userId = 2L;
                final ChatRoom chatRoom = ChatRoom.restore(chatRoomId, userId, new Chats());
                chatRoom.addSystemMessage("System message");
                chatRoom.addUserMessage("User message");

                // When
                final ChatContext chatContext = chatRoom.toChatContext();

                // Then
                then(chatContext).extracting("chatRoomId").isEqualTo(chatRoom.getId());
                then(chatContext.previousChats().size()).isEqualTo(2);
                then(chatContext.previousChats())
                        .extracting("type", "message")
                        .containsExactly(
                                tuple(MessageType.SYSTEM, "System message"),
                                tuple(MessageType.USER, "User message"));
            }
        }

        @Nested
        @DisplayName("채팅방에 채팅이 없으면")
        class Context_with_no_chats {

            @Test
            @DisplayName("빈 채팅 목록을 가진 채팅 컨텍스트를 반환한다")
            void it_returns_ChatContext_with_empty_chat_list() {
                // Given
                final Long chatRoomId = 1L;
                final Long userId = 2L;
                final ChatRoom chatRoom = ChatRoom.restore(chatRoomId, userId, new Chats());

                // When
                final ChatContext chatContext = chatRoom.toChatContext();

                // Then
                then(chatContext).extracting("chatRoomId").isEqualTo(chatRoom.getId());
                then(chatContext.previousChats()).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("동등성 비교")
    class Describe_equals_and_hashCode {

        @Nested
        @DisplayName("채팅방들이 같은 ID를 가지면")
        class Context_with_same_id {

            @Test
            @DisplayName("동등성 비교에서 참을 반환한다")
            void it_returns_true_for_equals() {
                // Given
                final Long id = 1L;
                final ChatRoom chatRoom1 = ChatRoom.restore(id, 1L, new Chats());
                final ChatRoom chatRoom2 = ChatRoom.restore(id, 2L, new Chats());

                // Then
                then(chatRoom1).isEqualTo(chatRoom2);
                then(chatRoom1.hashCode()).isEqualTo(chatRoom2.hashCode());
            }
        }

        @Nested
        @DisplayName("채팅방들이 다른 ID를 가지면")
        class Context_with_different_id {

            @Test
            @DisplayName("동등성 비교에서 거짓을 반환한다")
            void it_returns_false_for_equals() {
                // Given
                final ChatRoom chatRoom1 = ChatRoom.restore(1L, 1L, new Chats());
                final ChatRoom chatRoom2 = ChatRoom.restore(2L, 1L, new Chats());

                // Then
                then(chatRoom1).isNotEqualTo(chatRoom2);
                then(chatRoom1.hashCode()).isNotEqualTo(chatRoom2.hashCode());
            }
        }
    }
}
