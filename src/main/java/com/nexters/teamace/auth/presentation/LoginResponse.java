package com.nexters.teamace.auth.presentation;

public record LoginResponse(String username, String accessToken, String refreshToken) {}
