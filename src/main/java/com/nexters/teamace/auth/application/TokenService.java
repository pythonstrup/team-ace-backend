package com.nexters.teamace.auth.application;

public interface TokenService {
    String createAccessToken(String userId);

    String createRefreshToken(String userId);

    boolean validateToken(String token);

    String getUserIdFromToken(String token);
}
