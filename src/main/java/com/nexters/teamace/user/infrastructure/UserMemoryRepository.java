package com.nexters.teamace.user.infrastructure;

import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
class UserMemoryRepository implements UserRepository {

    private AtomicLong autoIncrement = new AtomicLong(1);
    private final Map<Long, User> data = new ConcurrentHashMap<>();

    @Override
    public User save(final User user) {
        final long id = autoIncrement.getAndIncrement();
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set user id", e);
        }
        data.put(id, user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return data.values().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equals(username))
                .findFirst();
    }
}
