package com.nexters.teamace.auth.infrastructure.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
record JwtProperties(String secret, long accessTokenValidity, long refreshTokenValidity) {}
