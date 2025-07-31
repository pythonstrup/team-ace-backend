package com.nexters.teamace.chat.infrastructure;

import com.nexters.teamace.chat.domain.ChatContext;
import com.nexters.teamace.chat.domain.ChatMessageGenerator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
class RandomChatMessageGenerator implements ChatMessageGenerator {

    private static final List<String> FIRST_MESSAGES =
            List.of(
                    "오늘 하루 중 가장 인상 깊었던 순간은 언제였나요?",
                    "지금 마음을 색으로 표현한다면 어떤 색일까요?",
                    "하루를 돌아볼 때, 마음속 날씨는 어떤가요?",
                    "지금 떠오르는 감정 단어 3개는 무엇인가요?",
                    "무언가 말하고 싶은데 말하지 못한 게 있다면 무엇일까요?");

    private static final List<String> SECOND_STAGE_MESSAGES =
            List.of(
                    "그때 어떤 일이 있었던 걸까요?",
                    "그 일이 일어났을 때, 몸이 어떻게 반응했나요?",
                    "이 감정을 느꼈을 때, 머릿속에 어떤 생각이 떠올랐나요?",
                    "이 감정이 이전에도 반복된 적이 있었나요?",
                    "혹시 누군가의 말이나 행동이 영향을 줬나요?");

    private static final List<String> THIRD_STAGE_MESSAGES =
            List.of(
                    "그 감정은 어떤 바람이나 욕구와 연결되어 있다고 느끼나요?",
                    "그 감정을 느낄 때, 당신에게 정말 중요한 것은 무엇이었나요?",
                    "지금 내 안에서 가장 주목받고 싶은 부분은 어떤 모습일까요?",
                    "감정이 말로 표현된다면, 뭐라고 말할까요?",
                    "그 감정을 요정처럼 상상한다면 어떤 모습일까요?");

    @Override
    public String generateFirstMessage() {
        return FIRST_MESSAGES.get(ThreadLocalRandom.current().nextInt(FIRST_MESSAGES.size()));
    }

    @Override
    public String generateResponseMessage(final String userMessage, final ChatContext context) {
        if (context == null) {
            throw new IllegalArgumentException("ChatContext cannot be null");
        }
        if (context.previousChats() == null) {
            throw new IllegalArgumentException("Previous chats cannot be null");
        }

        final int conversationCount = context.previousChats().size();
        final int secondStageCount = 2;
        final List<String> messages =
                (conversationCount <= secondStageCount)
                        ? SECOND_STAGE_MESSAGES
                        : THIRD_STAGE_MESSAGES;
        return messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
    }
}
