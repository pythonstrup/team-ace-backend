package com.nexters.teamace.auth.infrastructure.security;

import static com.nexters.teamace.common.exception.CustomException.INVALID_TOKEN;

import com.nexters.teamace.auth.application.AuthenticatedUser;
import com.nexters.teamace.auth.application.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {

        extractValidToken(request)
                .ifPresent(
                        token -> {
                            final AuthenticatedUser user =
                                    tokenService.getAuthenticatedUserFromToken(token);
                            setAuthentication(user);
                        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractValidToken(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
                .filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX.length()))
                .filter(StringUtils::hasText)
                .filter(tokenService::validateToken)
                .or(
                        () -> {
                            request.setAttribute("exception", INVALID_TOKEN);
                            return Optional.empty();
                        });
    }

    private void setAuthentication(final AuthenticatedUser user) {
        final String credentials = "";
        final var userDetails = new CustomUserDetails(user.username(), user.userId());
        final var authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, credentials, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
