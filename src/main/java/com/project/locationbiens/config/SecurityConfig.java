package com.project.locationbiens.config;

import com.project.locationbiens.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure CSRF protection and authorization rules
        http.csrf(csrf -> csrf
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())) // Configure CSRF protection to use cookies and handle CSRF tokens in requests
            .addFilterAfter(csrfCookieFilter(), CsrfFilter.class) // Add a custom filter to handle CSRF tokens in cookies
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to the root, HTML files, CSS, JS resources and error pages
                        .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/error").permitAll()
                        // Allow unauthenticated access to the /api/auth/** endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Allow authenticated access to the /api/goods/mine endpoint
                        .requestMatchers(HttpMethod.GET, "/api/goods/mine").authenticated()
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

    // Define a custom filter to handle CSRF tokens in cookies
    private OncePerRequestFilter csrfCookieFilter() {
    return new OncePerRequestFilter() {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain filterChain) throws ServletException, IOException {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
            if (csrfToken != null) {
                csrfToken.getToken();
            }
            filterChain.doFilter(request, response);
        }
    };
}

}