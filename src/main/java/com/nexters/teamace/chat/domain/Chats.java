package com.nexters.teamace.chat.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Chats {

    private final List<Chat> chats;

    public Chats() {
        this.chats = new ArrayList<>();
    }

    public Chats(final List<Chat> chats) {
        this.chats = new ArrayList<>(chats);
    }

    public void add(final Chat chat) {
        this.chats.add(chat);
    }

    public List<Chat> getChats() {
        return Collections.unmodifiableList(chats);
    }

    public int size() {
        return chats.size();
    }

    public boolean isEmpty() {
        return chats.isEmpty();
    }

    public Chat getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("No chats available");
        }
        return chats.get(0);
    }

    public Chat getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("No chats available");
        }
        return chats.get(chats.size() - 1);
    }
}
