package com.nexters.teamace.conversation.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConversationStage {
    EMOTION_RECOGNITION("1단계(감정인식)"),
    CONTEXT_EXPLORATION("2단계(맥락탐색)"),
    DESIRE_DISCOVERY("3단계(욕구발견)");

    private final String displayName;
}
