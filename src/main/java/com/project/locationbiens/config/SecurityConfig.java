package com.project.locationbiens.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection and configure authorization rules
        http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                // Allow unauthenticated access to the /api/auth/** endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Allow only users with the ADMIN role to access /api/admin/** endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Require authentication for all other requests
                .anyRequest().authenticated()
        )
        // Use HTTP Basic authentication for simplicity
        .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use BCryptPasswordEncoder for password hashing
        return new BCryptPasswordEncoder();
    }
}
