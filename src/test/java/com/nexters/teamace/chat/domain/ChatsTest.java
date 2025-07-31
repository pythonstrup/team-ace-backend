package com.nexters.teamace.chat.domain;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChatsTest {

    @Nested
    @DisplayName("기본 생성자")
    class Describe_default_constructor {

        @Test
        @DisplayName("빈 채팅 목록을 생성한다")
        void it_creates_empty_chat_list() {
            // When
            final Chats chats = new Chats();

            // Then
            then(chats.isEmpty()).isTrue();
            then(chats.size()).isZero();
        }
    }

    @Nested
    @DisplayName("리스트를 받는 생성자")
    class Describe_list_constructor {

        @Nested
        @DisplayName("채팅 목록이 제공되면")
        class Context_with_chat_list {

            @Test
            @DisplayName("제공된 채팅들로 목록을 생성한다")
            void it_creates_chat_list_with_provided_chats() {
                // Given
                final Chat chat1 = Chat.create(1L, MessageType.USER, "Hello");
                final Chat chat2 = Chat.create(1L, MessageType.SYSTEM, "Hi there");
                final List<Chat> chatList = List.of(chat1, chat2);

                // When
                final Chats chats = new Chats(chatList);

                // Then
                then(chats.size()).isEqualTo(2);
                then(chats.isEmpty()).isFalse();
                then(chats.getChats()).containsExactly(chat1, chat2);
            }
        }

        @Nested
        @DisplayName("빈 목록이 제공되면")
        class Context_with_empty_list {

            @Test
            @DisplayName("빈 채팅 목록을 생성한다")
            void it_creates_empty_chat_list() {
                // Given
                final List<Chat> emptyList = List.of();

                // When
                final Chats chats = new Chats(emptyList);

                // Then
                then(chats.isEmpty()).isTrue();
                then(chats.size()).isZero();
            }
        }
    }

    @Nested
    @DisplayName("채팅 추가")
    class Describe_add {

        @Nested
        @DisplayName("기존 목록에 채팅을 추가하면")
        class Context_when_adding_to_existing_list {

            @Test
            @DisplayName("채팅이 목록 끝에 추가된다")
            void it_adds_chat_to_end_of_list() {
                // Given
                final Chat existingChat = Chat.create(1L, MessageType.USER, "Hello");
                final Chats chats = new Chats(List.of(existingChat));
                final Chat newChat = Chat.create(1L, MessageType.SYSTEM, "Hi there");

                // When
                chats.add(newChat);

                // Then
                then(chats.size()).isEqualTo(2);
                then(chats.getFirst()).isEqualTo(existingChat);
                then(chats.getLast()).isEqualTo(newChat);
            }
        }
    }

    @Nested
    @DisplayName("채팅 목록 조회")
    class Describe_getChats {

        @Test
        @DisplayName("수정 불가능한 채팅 목록을 반환한다")
        void it_returns_unmodifiable_chat_list() {
            // Given
            final Chat chat = Chat.create(1L, MessageType.USER, "Hello");
            final Chats chats = new Chats(List.of(chat));

            // When
            final List<Chat> result = chats.getChats();

            // Then
            then(result).containsExactly(chat);
            thenThrownBy(() -> result.add(Chat.create(2L, MessageType.SYSTEM, "Test")))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("첫 번째 채팅 조회")
    class Describe_getFirst {

        @Nested
        @DisplayName("채팅이 있으면")
        class Context_when_chats_exist {

            @Test
            @DisplayName("첫 번째 채팅을 반환한다")
            void it_returns_first_chat() {
                // Given
                final Chat firstChat = Chat.create(1L, MessageType.USER, "First");
                final Chat secondChat = Chat.create(1L, MessageType.SYSTEM, "Second");
                final Chats chats = new Chats(List.of(firstChat, secondChat));

                // When
                final Chat result = chats.getFirst();

                // Then
                then(result).isEqualTo(firstChat);
            }
        }

        @Nested
        @DisplayName("채팅이 없으면")
        class Context_when_no_chats_exist {

            @Test
            @DisplayName("IllegalStateException을 던진다")
            void it_throws_IllegalStateException() {
                // Given
                final Chats chats = new Chats();

                // When & Then
                thenThrownBy(chats::getFirst)
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("No chats available");
            }
        }
    }

    @Nested
    @DisplayName("마지막 채팅 조회")
    class Describe_getLast {

        @Nested
        @DisplayName("채팅이 있으면")
        class Context_when_chats_exist {

            @Test
            @DisplayName("마지막 채팅을 반환한다")
            void it_returns_last_chat() {
                // Given
                final Chat firstChat = Chat.create(1L, MessageType.USER, "First");
                final Chat lastChat = Chat.create(1L, MessageType.SYSTEM, "Last");
                final Chats chats = new Chats(List.of(firstChat, lastChat));

                // When
                final Chat result = chats.getLast();

                // Then
                then(result).isEqualTo(lastChat);
            }
        }

        @Nested
        @DisplayName("채팅이 없으면")
        class Context_when_no_chats_exist {

            @Test
            @DisplayName("IllegalStateException을 던진다")
            void it_throws_IllegalStateException() {
                // Given
                final Chats chats = new Chats();

                // When & Then
                thenThrownBy(chats::getLast)
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("No chats available");
            }
        }
    }

    @Nested
    @DisplayName("크기 조회")
    class Describe_size {

        @Test
        @DisplayName("채팅 개수를 반환한다")
        void it_returns_chat_count() {
            // Given
            final Chat chat1 = Chat.create(1L, MessageType.USER, "Hello");
            final Chat chat2 = Chat.create(1L, MessageType.SYSTEM, "Hi");
            final Chat chat3 = Chat.create(1L, MessageType.USER, "How are you?");
            final Chats chats = new Chats(List.of(chat1, chat2, chat3));

            // When
            final int size = chats.size();

            // Then
            then(size).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("비어있는지 확인")
    class Describe_isEmpty {

        @Nested
        @DisplayName("채팅이 없으면")
        class Context_when_no_chats {

            @Test
            @DisplayName("참을 반환한다")
            void it_returns_true() {
                // Given
                final Chats chats = new Chats();

                // When
                final boolean isEmpty = chats.isEmpty();

                // Then
                then(isEmpty).isTrue();
            }
        }

        @Nested
        @DisplayName("채팅이 있으면")
        class Context_when_chats_exist {

            @Test
            @DisplayName("거짓을 반환한다")
            void it_returns_false() {
                // Given
                final Chat chat = Chat.create(1L, MessageType.USER, "Hello");
                final Chats chats = new Chats(List.of(chat));

                // When
                final boolean isEmpty = chats.isEmpty();

                // Then
                then(isEmpty).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("동등성 비교")
    class Describe_equals_and_hashCode {

        @Nested
        @DisplayName("같은 채팅 목록을 가지면")
        class Context_with_same_chat_list {

            @Test
            @DisplayName("동등성 비교에서 참을 반환한다")
            void it_returns_true_for_equals() {
                // Given
                final Chat chat1 = Chat.create(1L, MessageType.USER, "Hello");
                final Chat chat2 = Chat.create(1L, MessageType.SYSTEM, "Hi");
                final Chats chats1 = new Chats(List.of(chat1, chat2));
                final Chats chats2 = new Chats(List.of(chat1, chat2));

                // Then
                then(chats1).isEqualTo(chats2);
                then(chats1.hashCode()).isEqualTo(chats2.hashCode());
            }
        }

        @Nested
        @DisplayName("다른 채팅 목록을 가지면")
        class Context_with_different_chat_list {

            @Test
            @DisplayName("동등성 비교에서 거짓을 반환한다")
            void it_returns_false_for_equals() {
                // Given
                final Chat chat1 = Chat.restore(1L, 1L, MessageType.USER, "Hello");
                final Chat chat2 = Chat.restore(2L, 1L, MessageType.SYSTEM, "Hi");
                final Chats chats1 = new Chats(List.of(chat1));
                final Chats chats2 = new Chats(List.of(chat2));

                // Then
                then(chats1).isNotEqualTo(chats2);
            }
        }
    }
}
