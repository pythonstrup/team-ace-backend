package com.nexters.teamace.common.config;

import com.nexters.teamace.auth.infrastructure.security.CustomUserDetails;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    private static final String SYSTEM = "system";

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () ->
                Optional.ofNullable(SecurityContextHolder.getContext())
                        .map(SecurityContext::getAuthentication)
                        .filter(Authentication::isAuthenticated)
                        .map(Authentication::getPrincipal)
                        .filter(CustomUserDetails.class::isInstance)
                        .map(CustomUserDetails.class::cast)
                        .map(CustomUserDetails::getUsername)
                        .or(() -> Optional.of(SYSTEM));
    }
}
