package com.nexters.teamace.auth.presentation;

public record LoginResponse(String userId, String accessToken, String refreshToken) {}
