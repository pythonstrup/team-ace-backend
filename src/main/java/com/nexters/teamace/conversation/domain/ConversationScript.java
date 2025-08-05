package com.nexters.teamace.conversation.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        String result = content;
        Matcher matcher = VARIABLE_PATTERN.matcher(content);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String variableValue = resolveVariableValue(variableName, variables);
            result = result.replace("{{" + variableName + "}}", variableValue);
        }

        return result;
    }

    private String resolveVariableValue(
            String variableName, Map<ConversationContextType, String> variables) {
        try {
            final ConversationContextType contextType =
                    ConversationContextType.valueOf(variableName);
            return getVariableValue(variableName, contextType, variables);
        } catch (IllegalArgumentException e) {
            return handleUnknownVariable(variableName);
        }
    }

    private String getVariableValue(
            String variableName,
            ConversationContextType contextType,
            Map<ConversationContextType, String> variables) {
        final String variableValue = variables.getOrDefault(contextType, "");

        if (variableValue.isEmpty()) {
            log.warn(
                    "Empty value provided for variable '{}' in template type '{}'",
                    variableName,
                    type);
        }

        return variableValue;
    }

    private String handleUnknownVariable(String variableName) {
        log.error(
                "Unknown variable '{}' in template type '{}'. Variable names must be defined in ConversationContextType enum.",
                variableName,
                type);
        return "{{" + variableName + "}}";
    }

    public void validateVariables(final Map<ConversationContextType, String> providedVariables) {
        final Set<String> requiredVariables = extractRequiredVariables();
        final Set<String> missingVariables =
                findMissingVariables(requiredVariables, providedVariables);

        throwExceptionIfVariablesMissing(missingVariables);
    }

    private Set<String> extractRequiredVariables() {
        final Set<String> variables = new HashSet<>();
        final Matcher matcher = VARIABLE_PATTERN.matcher(content);

        while (matcher.find()) {
            variables.add(matcher.group(1));
        }

        return variables;
    }

    private Set<String> findMissingVariables(
            final Set<String> requiredVariables,
            final Map<ConversationContextType, String> providedVariables) {
        return requiredVariables.stream()
                .filter(this::isValidContextType)
                .filter(
                        required ->
                                !providedVariables.containsKey(
                                        ConversationContextType.valueOf(required)))
                .collect(Collectors.toSet());
    }

    private boolean isValidContextType(final String variable) {
        try {
            ConversationContextType.valueOf(variable);
            return true;
        } catch (IllegalArgumentException e) {
            log.warn("Template '{}' contains unknown variable: {}", type, variable);
            return false;
        }
    }

    private void throwExceptionIfVariablesMissing(final Set<String> missingVariables) {
        if (!missingVariables.isEmpty()) {
            final String variableNames = String.join(", ", missingVariables);
            throw new IllegalArgumentException(
                    String.format(
                            "Missing required variables for template '%s': %s",
                            type, variableNames));
        }
    }
}
