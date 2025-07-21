package com.nexters.teamace.auth.application;

public record LoginResult(String userId, String accessToken, String refreshToken) {}
