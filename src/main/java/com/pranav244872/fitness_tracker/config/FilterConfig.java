package com.pranav244872.fitness_tracker.config;

import com.pranav244872.fitness_tracker.filter.ApiSecretFilter;
import com.pranav244872.fitness_tracker.filter.RateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prevents Spring Boot from auto-registering our security filters as Servlet filters.
 * They should ONLY run within the Spring Security filter chain (where they're explicitly added),
 * not as standalone servlet filters (which would cause them to run on all requests including admin).
 */
@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<ApiSecretFilter> disableApiSecretFilterAutoRegistration(ApiSecretFilter filter) {
        FilterRegistrationBean<ApiSecretFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    FilterRegistrationBean<RateLimitFilter> disableRateLimitFilterAutoRegistration(RateLimitFilter filter) {
        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
