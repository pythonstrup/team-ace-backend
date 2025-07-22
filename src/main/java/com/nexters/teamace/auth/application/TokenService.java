package com.nexters.teamace.auth.application;

public interface TokenService {
    String createAccessToken(String username);

    String createRefreshToken(String username);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
