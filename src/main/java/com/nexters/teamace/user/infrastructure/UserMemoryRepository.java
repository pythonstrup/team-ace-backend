package com.nexters.teamace.user.infrastructure;

import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class UserMemoryRepository implements UserRepository {

    private long autoIncrement = 1L;
    private final Map<Long, User> data = new ConcurrentHashMap<>();

    @Override
    public User save(final User user) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, autoIncrement);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set user id", e);
        }
        data.put(autoIncrement++, user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return Optional.empty();
    }
}
