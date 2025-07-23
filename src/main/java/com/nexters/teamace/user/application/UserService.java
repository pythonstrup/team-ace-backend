package com.nexters.teamace.user.application;

import static com.nexters.teamace.common.exception.CustomException.*;

import com.nexters.teamace.user.domain.User;
import com.nexters.teamace.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public CreateUserResult createUser(final CreateUserCommand command) {
        if (userRepository.findByUsername(command.username()).isPresent()) {
            throw USER_ALREADY_EXISTS;
        }

        final User user = new User(command.username(), command.nickname());
        final User savedUser = userRepository.save(user);

        return new CreateUserResult(
                savedUser.getId(), savedUser.getUsername(), savedUser.getNickname());
    }

    public GetUserResult getUserByUsername(final String username) {
        final User user = userRepository.getByUsername(username);
        return new GetUserResult(user.getId(), user.getUsername(), user.getNickname());
    }
}
