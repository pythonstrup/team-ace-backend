package com.nexters.teamace.chat.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Chats implements Iterable<Chat> {

    private final List<Chat> chats;

    Chats() {
        this.chats = new ArrayList<>();
    }

    public Chats(final List<Chat> chats) {
        this.chats = new ArrayList<>(chats);
    }

    public void add(final Chat chat) {
        this.chats.add(chat);
    }

    @Override
    public Iterator<Chat> iterator() {
        return chats.iterator();
    }

    public Stream<Chat> stream() {
        return StreamSupport.stream(spliterator(), false);
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
