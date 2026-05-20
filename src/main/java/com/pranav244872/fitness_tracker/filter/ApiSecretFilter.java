package com.pranav244872.fitness_tracker.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Order(1)
public class ApiSecretFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ApiSecretFilter.class);
    private static final String HEADER_NAME = "X-App-Secret";

    @Value("${APP_SECRET:}")
    private String appSecret;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Exempt paths: health check, admin dashboard static files, admin API (uses Basic Auth)
        if (path.equals("/api/health")
                || path.startsWith("/admin")
                || path.startsWith("/api/admin")
                || path.startsWith("/api/music/")
                || path.equals("/favicon.ico")
                || path.endsWith(".ico")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".svg")
                || appSecret.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String providedSecret = request.getHeader(HEADER_NAME);
        if (providedSecret == null || !providedSecret.equals(appSecret)) {
            log.warn("Rejected request to {} - missing or invalid X-App-Secret from IP: {}",
                    path, request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Forbidden: Invalid app secret\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
