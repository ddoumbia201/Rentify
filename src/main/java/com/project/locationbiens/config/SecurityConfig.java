package com.project.locationbiens.config;

import com.project.locationbiens.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection and configure authorization rules
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to the root, HTML files, CSS, JS resources and error pages
                        .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/error").permitAll()
                        // Allow unauthenticated access to the /api/auth/** endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Allow only users with the ADMIN role to access /api/admin/** endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Allow unauthenticated access to GET requests for /api/goods and /api/goods/{id}
                        .requestMatchers(HttpMethod.GET, "/api/goods", "/api/goods/**").permitAll()
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                // Use HTTP Basic authentication for simplicity
                //.httpBasic(Customizer.withDefaults());
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use BCryptPasswordEncoder for password hashing
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Return a custom UserDetailsService implementation
        return email -> userRepository.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().replace("ROLE_", "")) // Set the user's role
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Return the AuthenticationManager from the AuthenticationConfiguration
        return config.getAuthenticationManager();
    }
}
