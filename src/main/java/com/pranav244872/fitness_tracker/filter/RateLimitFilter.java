package com.pranav244872.fitness_tracker.filter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
@Order(2)
public class RateLimitFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int MAX_REQUESTS_PER_MINUTE = 120;
    private static final long WINDOW_MS = 60_000;

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        // Exempt admin and health
        if (path.startsWith("/admin") || path.equals("/api/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = getClientIp(request);
        TokenBucket bucket = buckets.computeIfAbsent(clientIp, k -> new TokenBucket());

        if (!bucket.tryConsume()) {
            log.warn("Rate limit exceeded for IP: {} on path: {}", clientIp, path);
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class TokenBucket {
        private final AtomicInteger tokens = new AtomicInteger(MAX_REQUESTS_PER_MINUTE);
        private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());

        boolean tryConsume() {
            long now = System.currentTimeMillis();
            if (now - windowStart.get() > WINDOW_MS) {
                tokens.set(MAX_REQUESTS_PER_MINUTE);
                windowStart.set(now);
            }
            return tokens.getAndDecrement() > 0;
        }
    }
}
