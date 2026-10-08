package com.spring.course.management.system.controller;

import com.spring.course.management.system.dto.AuthResponse;
import com.spring.course.management.system.dto.LoginRequest;
import com.spring.course.management.system.dto.RefreshTokenRequest;
import com.spring.course.management.system.security.JwtService;
import com.spring.course.management.system.service.AuthService;
import com.spring.course.management.system.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            TokenService tokenService) {

        this.authService = authService;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }


    // Refresh Token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }


    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization")
            String authHeader) {

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .badRequest()
                    .body("Authorization token is required");
        }

        String token =
                authHeader.substring(7);

        Date expiration =
                jwtService.extractExpiration(token);

        String tokenId =
                jwtService.extractTokenId(token);

        tokenService.revokeToken(
                tokenId,
                expiration
        );

        return ResponseEntity.ok(
                "Logout successful"
        );
    }
}