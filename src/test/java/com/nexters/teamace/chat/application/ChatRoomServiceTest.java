package com.nexters.teamace.chat.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import com.nexters.teamace.chat.domain.Chat;
import com.nexters.teamace.chat.domain.ChatRoom;
import com.nexters.teamace.chat.domain.ChatRoomRepository;
import com.nexters.teamace.chat.domain.MessageType;
import com.nexters.teamace.common.exception.CustomException;
import com.nexters.teamace.common.utils.UseCaseIntegrationTest;
import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChatRoomServiceTest extends UseCaseIntegrationTest {

    @Autowired private ChatRoomService chatRoomService;
    @Autowired private UserRepository userRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;

    private String generateUserString() {
        return fixtureMonkey
                .giveMeBuilder(String.class)
                .set("$", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20))
                .sample();
    }

    @Nested
    @DisplayName("createChat")
    class Describe_createChat {

        @Nested
        @DisplayName("유효한 username이 제공되었을 때")
        class Context_with_valid_username {

            @Test
            @DisplayName("랜덤한 첫 메시지와 함께 채팅방을 생성한다")
            void it_creates_chatroom_with_random_first_message() {
                // Given
                final String username = generateUserString();
                final String nickname = generateUserString();
                userRepository.save(new User(username, nickname));

                final ChatRoomCommand command = new ChatRoomCommand(username);

                // When
                final CreateChatRoomResult result = chatRoomService.createChat(command);

                // Then
                then(result.chatRoomId()).isNotNull();
                then(result.firstChat()).isNotNull();

                // 저장된 ChatRoom 검증
                final ChatRoom savedChatRoom =
                        chatRoomRepository.findById(result.chatRoomId()).orElseThrow();
                then(savedChatRoom.getChats().size()).isEqualTo(1);

                final Chat firstChat = savedChatRoom.getChats().getFirst();
                then(firstChat)
                        .extracting("type", "message")
                        .containsExactly(MessageType.SYSTEM, result.firstChat());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 username일 때")
        class Context_with_nonexistent_username {

            @Test
            @DisplayName("USER_NOT_FOUND 에러를 던진다")
            void it_throws_user_not_found_error() {
                // Given
                final String nonExistentUsername = "nonexistent";
                final ChatRoomCommand command = new ChatRoomCommand(nonExistentUsername);

                // When & Then
                thenThrownBy(() -> chatRoomService.createChat(command))
                        .isInstanceOf(CustomException.class)
                        .extracting("errorType")
                        .isEqualTo(CustomException.USER_NOT_FOUND.getErrorType());
            }
        }
    }
}
