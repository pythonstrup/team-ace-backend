package com.nexters.teamace.auth.infrastructure.jwt;

import com.nexters.teamace.auth.application.AuthenticatedUser;
import com.nexters.teamace.auth.application.TokenService;
import com.nexters.teamace.common.application.SystemHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class JwtTokenProvider implements TokenService {
    private static final String USER_ID = "userId";

    private final SystemHolder systemHolder;
    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtTokenProvider(final SystemHolder systemHolder, final JwtProperties jwtProperties) {
        this.systemHolder = systemHolder;
        this.jwtProperties = jwtProperties;

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(final AuthenticatedUser user) {
        return createToken(user, jwtProperties.accessTokenValidity());
    }

    public String createRefreshToken(final AuthenticatedUser user) {
        return createToken(user, jwtProperties.refreshTokenValidity());
    }

    private String createToken(final AuthenticatedUser user, final long validityInMilliseconds) {
        final long currentTimeMillis = systemHolder.currentTimeMillis();
        final long expiration = currentTimeMillis + validityInMilliseconds;

        final Map<String, Object> payload = new HashMap<>();
        payload.put(USER_ID, user.userId());

        return Jwts.builder()
                .subject(user.username())
                .claims(payload)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(expiration))
                .signWith(key)
                .compact();
    }

    public AuthenticatedUser getAuthenticatedUserFromToken(final String token) {
        final Claims claims = getClaims(token);
        final String username = claims.getSubject();
        final Long userId = claims.get(USER_ID, Long.class);
        return new AuthenticatedUser(username, userId);
    }

    public boolean validateToken(final String token) {
        try {
            getClaims(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
