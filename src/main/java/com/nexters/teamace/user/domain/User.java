package com.nexters.teamace.user.domain;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public class User {

    @Getter private Long id;
    @Getter private String username;
    @Getter private String nickname;

    public User(final String username, final String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
}
