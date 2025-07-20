package com.nexters.teamace.conversation.domain;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ConversationScript {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    @Getter private final ConversationType type;
    @Getter private final String content;

    public ConversationScript(final ConversationType type, final String content) {
        if (type == null) {
            throw new IllegalArgumentException("ConversationType cannot be null");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        this.type = type;
        this.content = content;
    }

    public String render(final Map<ConversationContextType, String> variables) {
        if (variables == null || variables.isEmpty()) {
            return content;
        }

        String result = content;
        Matcher matcher = VARIABLE_PATTERN.matcher(content);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String variableValue =
                    variables.getOrDefault(ConversationContextType.valueOf(variableName), "");
            result = result.replace("{{" + variableName + "}}", variableValue);
        }

        return result;
    }
}
