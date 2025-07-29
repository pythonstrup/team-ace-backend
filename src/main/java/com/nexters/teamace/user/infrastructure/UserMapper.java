package com.nexters.teamace.user.infrastructure;

import com.nexters.teamace.user.domain.User;
import org.springframework.stereotype.Component;

@Component
class UserMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getUsername(), user.getNickname());
    }

    public User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getNickname());
    }
}
