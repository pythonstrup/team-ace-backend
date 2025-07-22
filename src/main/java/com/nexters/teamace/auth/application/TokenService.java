package com.nexters.teamace.auth.application;

public interface TokenService {
    String createAccessToken(AuthenticatedUser user);

    String createRefreshToken(AuthenticatedUser user);

    boolean validateToken(String token);

    AuthenticatedUser getAuthenticatedUserFromToken(String token);
}
