package com.pranav244872.fitness_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pranav244872.fitness_tracker.filter.ApiSecretFilter;
import com.pranav244872.fitness_tracker.filter.RateLimitFilter;
import com.pranav244872.fitness_tracker.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final ApiSecretFilter apiSecretFilter;
    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          AuthenticationProvider authenticationProvider,
                          ApiSecretFilter apiSecretFilter,
                          RateLimitFilter rateLimitFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.apiSecretFilter = apiSecretFilter;
        this.rateLimitFilter = rateLimitFilter;
    }

    // Admin dashboard security (HTTP Basic Auth)
    @Bean
    @Order(1)
    SecurityFilterChain adminFilterChain(HttpSecurity http,
                                         @Value("${ADMIN_USERNAME:admin}") String adminUser,
                                         @Value("${ADMIN_PASSWORD:admin}") String adminPass) throws Exception {
        http
            .securityMatcher("/api/admin/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(basic -> {})
            .userDetailsService(username -> {
                if (username.equals(adminUser)) {
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(adminUser)
                            .password("{noop}" + adminPass)
                            .roles("ADMIN")
                            .build();
                }
                throw new org.springframework.security.core.userdetails.UsernameNotFoundException("Not found");
            })
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // Main API security (JWT)
    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/register", "/api/auth/login", "/api/health").permitAll()
                    .requestMatchers("/admin/**").permitAll()
                    .requestMatchers("/api/music/**").authenticated()
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(apiSecretFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(rateLimitFilter, ApiSecretFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
