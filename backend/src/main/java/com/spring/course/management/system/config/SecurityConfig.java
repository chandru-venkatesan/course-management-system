package com.spring.course.management.system.config;

import com.spring.course.management.system.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.spring.course.management.system.security.RequestLoggingFilter;
import com.spring.course.management.system.security.RateLimitFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;

    private final RequestLoggingFilter requestLoggingFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RateLimitFilter rateLimitFilter,
            RequestLoggingFilter requestLoggingFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;

        this.rateLimitFilter = rateLimitFilter;

        this.requestLoggingFilter =
                requestLoggingFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // CSRF

                .csrf(csrf -> csrf.disable())

                // SESSION

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // AUTHORIZATION

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC APIs

                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/api/auth/logout",

                                // Swagger / OpenAPI
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Anyone can register
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users"
                        ).permitAll()



                        // USER MANAGEMENT
                        // ADMIN ONLY


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/users",
                                "/api/users/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/users/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/users/**"
                        ).hasRole("ADMIN")



                        // COURSE APIs


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/courses",
                                "/api/courses/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TRAINER",
                                "STUDENT"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/courses",
                                "/api/courses/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TRAINER"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/courses",
                                "/api/courses/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TRAINER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/courses",
                                "/api/courses/**"
                        ).hasRole("ADMIN")



                        // STUDENT APIs


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/students",
                                "/api/students/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TRAINER",
                                "STUDENT"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/students",
                                "/api/students/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/students",
                                "/api/students/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/students",
                                "/api/students/**"
                        ).hasRole("ADMIN")



                        // TRAINER APIs


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/trainers",
                                "/api/trainers/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TRAINER"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/trainers",
                                "/api/trainers/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/trainers",
                                "/api/trainers/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/trainers",
                                "/api/trainers/**"
                        ).hasRole("ADMIN")



                        // ENROLLMENT APIs


                        .requestMatchers(
                                "/api/enrollments",
                                "/api/enrollments/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "STUDENT"
                        )



                        // SUBSCRIPTION APIs


                        .requestMatchers(
                                "/api/subscriptions",
                                "/api/subscriptions/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "STUDENT"
                        )



                        // PLATFORM APIs


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/platforms",
                                "/api/platforms/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "TRAINER",
                                "STUDENT"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/platforms",
                                "/api/platforms/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/platforms",
                                "/api/platforms/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/platforms",
                                "/api/platforms/**"
                        ).hasRole("ADMIN")



                        // EVERYTHING ELSE


                        .anyRequest().authenticated()
                )


                       // JWT FILTER

                .addFilterBefore(
                       rateLimitFilter,
                     UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )


                  // CUSTOM REQUEST LOGGING FILTER


                .addFilterAfter(
                        requestLoggingFilter,
                        JwtAuthenticationFilter.class
                );

        return http.build();
    }
}