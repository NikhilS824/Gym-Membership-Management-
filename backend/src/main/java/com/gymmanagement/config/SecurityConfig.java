package com.gymmanagement.config;

import com.gymmanagement.security.CustomUserDetailsService;
import com.gymmanagement.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/membership/verify/**").permitAll()

                        // Admin Only Endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/plans/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/plans/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/plans/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/members/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trainers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/trainers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/trainers/**").hasRole("ADMIN")

                        // Staff and Admin Endpoints
                        .requestMatchers("/api/members/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/plans/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/memberships/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/payments/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/attendance/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/trainers/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "STAFF")

                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
