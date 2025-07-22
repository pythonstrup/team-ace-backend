package com.nexters.teamace.auth.presentation;

public record SignupResponse(String accessToken, String refreshToken, String username) {}
