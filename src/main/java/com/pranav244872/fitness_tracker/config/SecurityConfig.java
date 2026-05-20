package com.pranav244872.fitness_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

    // Admin dashboard security (HTTP Basic Auth) - matched first
    @Bean
    @Order(1)
    SecurityFilterChain adminFilterChain(HttpSecurity http,
                                         @Value("${ADMIN_USERNAME:admin}") String adminUser,
                                         @Value("${ADMIN_PASSWORD:admin}") String adminPass) throws Exception {

        // Create a dedicated AuthenticationProvider for admin
        InMemoryUserDetailsManager adminUserDetails = new InMemoryUserDetailsManager(
                User.builder()
                        .username(adminUser)
                        .password(adminPass)
                        .roles("ADMIN")
                        .build()
        );

        @SuppressWarnings("deprecation")
        DaoAuthenticationProvider adminAuthProvider = new DaoAuthenticationProvider();
        adminAuthProvider.setUserDetailsService(adminUserDetails);
        adminAuthProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        http
            .securityMatcher("/api/admin/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(basic -> {})
            .authenticationProvider(adminAuthProvider)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // Main API security (JWT) - catch-all
    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/register", "/api/auth/login", "/api/health").permitAll()
                    .requestMatchers("/admin/**").permitAll()
                    .requestMatchers("/api/music/*/stream").permitAll()
                    .requestMatchers("/api/music/**").authenticated()
                    .requestMatchers("/favicon.ico", "/**.ico", "/**.png", "/**.jpg", "/**.css", "/**.js", "/**.svg").permitAll()
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
