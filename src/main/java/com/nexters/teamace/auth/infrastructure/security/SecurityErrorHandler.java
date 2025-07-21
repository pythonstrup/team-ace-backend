package com.nexters.teamace.auth.infrastructure.security;

import com.nexters.teamace.common.exception.CustomException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        CustomException exception = (CustomException) request.getAttribute("exception");
        if (exception != null) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
            return;
        }
        handlerExceptionResolver.resolveException(request, response, null, authException);
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        CustomException exception = (CustomException) request.getAttribute("exception");
        if (exception != null) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
            return;
        }
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }
}
