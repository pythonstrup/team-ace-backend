package com.nexters.teamace.user.domain;

import static com.nexters.teamace.common.exception.CustomException.USER_NOT_FOUND;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);

    default User getByUsername(String username) {
        return findByUsername(username).orElseThrow(() -> USER_NOT_FOUND);
    }
}
