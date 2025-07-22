package com.nexters.teamace.auth.application;

public record LoginResult(String username, String accessToken, String refreshToken) {}
