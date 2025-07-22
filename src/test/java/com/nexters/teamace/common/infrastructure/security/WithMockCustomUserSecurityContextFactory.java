package com.nexters.teamace.common.infrastructure.security;

import com.nexters.teamace.auth.infrastructure.security.CustomUserDetails;
import com.nexters.teamace.common.annotation.WithMockCustomUser;
import java.util.Collections;
import java.util.List;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        final var principal = new CustomUserDetails(annotation.username(), annotation.userId());
        final Object credentials = null;
        final List<GrantedAuthority> authorities = Collections.emptyList();

        final var authentication =
                new TestingAuthenticationToken(principal, credentials, authorities);
        context.setAuthentication(authentication);

        return context;
    }
}
