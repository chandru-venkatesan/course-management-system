package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.AuthResponse;
import com.spring.course.management.system.dto.LoginRequest;
import com.spring.course.management.system.dto.RefreshTokenRequest;
import com.spring.course.management.system.exception.InvalidTokenException;
import com.spring.course.management.system.model.User;
import com.spring.course.management.system.repository.UserRepository;
import com.spring.course.management.system.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // CONSTRUCTOR
    public AuthService(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            UserRepository userRepository,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    // LOGIN
    public AuthResponse login(
            LoginRequest request) {

        // Step 1: Authenticate email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        // Step 2: Load actual User entity
        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow(() ->
                        new InvalidTokenException(
                                "User not found"
                        )
                );


        // Step 3: Generate access token
        String accessToken =
                jwtService.generateAccessToken(
                        user
                );


        // Step 4: Generate refresh token
        String refreshToken =
                jwtService.generateRefreshToken(
                        user
                );


        // Step 5: Return both tokens
        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer"
        );
    }

    // REFRESH TOKEN
    public AuthResponse refreshToken(
            RefreshTokenRequest request) {

        // Step 1: Get refresh token
        String refreshToken =
                request.getRefreshToken();


        // Step 2: Extract email from token
        String username =
                jwtService.extractUsername(
                        refreshToken
                );


        // Step 3: Load actual User
        User user =
                userRepository.findByEmail(
                        username
                ).orElseThrow(() ->
                        new InvalidTokenException(
                                "User not found"
                        )
                );


        // Step 4: Check token version
        Long tokenVersion =
                jwtService.extractTokenVersion(
                        refreshToken
                );

        if (!tokenVersion.equals(
                user.getTokenVersion())) {

            throw new InvalidTokenException(
                    "Refresh token has been revoked"
            );
        }


        // Step 5: Validate refresh token
        if (!jwtService.isTokenValid(
                refreshToken,
                user)) {

            throw new InvalidTokenException(
                    "Invalid or expired refresh token"
            );
        }


        // Step 6: Generate new access token
        String newAccessToken =
                jwtService.generateAccessToken(
                        user
                );


        // Step 7: Return new access token
        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer"
        );
    }
}