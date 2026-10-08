package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.AuthResponse;
import com.spring.course.management.system.dto.LoginRequest;
import com.spring.course.management.system.model.Role;
import com.spring.course.management.system.model.User;
import com.spring.course.management.system.repository.UserRepository;
import com.spring.course.management.system.security.JwtService;
import com.spring.course.management.system.dto.RefreshTokenRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;


    // =====================================================
    // TEST 1 — SUCCESSFUL LOGIN
    // =====================================================

    @Test
    void testLoginSuccessfully() {

        // Arrange
        LoginRequest request = new LoginRequest();

        request.setEmail("student@gmail.com");
        request.setPassword("password123");


        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPassword("password123");
        user.setRole(Role.STUDENT);
        user.setActive(true);
        user.setTokenVersion(1L);


        when(userRepository.findByEmail("student@gmail.com"))
                .thenReturn(Optional.of(user));


        when(jwtService.generateAccessToken(user))
                .thenReturn("access-token");


        when(jwtService.generateRefreshToken(user))
                .thenReturn("refresh-token");


        // Act
        AuthResponse response =
                authService.login(request);


        // Assert
        assertNotNull(response);

        assertEquals(
                "access-token",
                response.getAccessToken()
        );

        assertEquals(
                "refresh-token",
                response.getRefreshToken()
        );

        assertEquals(
                "Bearer",
                response.getTokenType()
        );


        // Verify authentication was called
        verify(authenticationManager)
                .authenticate(any());


        // Verify user was searched
        verify(userRepository)
                .findByEmail("student@gmail.com");


        // Verify both tokens were generated
        verify(jwtService)
                .generateAccessToken(user);

        verify(jwtService)
                .generateRefreshToken(user);
    }

    @Test
    void testLoginUserNotFound() {

        // Arrange
        LoginRequest request = new LoginRequest();

        request.setEmail("missing@gmail.com");
        request.setPassword("password123");


        when(userRepository.findByEmail("missing@gmail.com"))
                .thenReturn(Optional.empty());


        // Act + Assert
        assertThrows(
                com.spring.course.management.system.exception.InvalidTokenException.class,
                () -> authService.login(request)
        );


        // Verify user was searched
        verify(userRepository)
                .findByEmail("missing@gmail.com");


        // Tokens should NOT be generated
        verify(jwtService, never())
                .generateAccessToken(any(User.class));

        verify(jwtService, never())
                .generateRefreshToken(any(User.class));
    }

    @Test
    void testLoginAuthenticationFailure() {

        // Arrange
        LoginRequest request = new LoginRequest();

        request.setEmail("student@gmail.com");
        request.setPassword("wrong-password");


        when(authenticationManager.authenticate(any()))
                .thenThrow(
                        new org.springframework.security.authentication.BadCredentialsException(
                                "Invalid username or password"
                        )
                );


        // Act + Assert
        assertThrows(
                org.springframework.security.authentication.BadCredentialsException.class,
                () -> authService.login(request)
        );


        // User repository should NOT be called
        verify(userRepository, never())
                .findByEmail(anyString());


        // JWT tokens should NOT be generated
        verify(jwtService, never())
                .generateAccessToken(any(User.class));

        verify(jwtService, never())
                .generateRefreshToken(any(User.class));
    }

    @Test
    void testRefreshTokenSuccessfully() {

        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();

        request.setRefreshToken("refresh-token");


        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPassword("password123");
        user.setRole(Role.STUDENT);
        user.setActive(true);
        user.setTokenVersion(1L);


        when(jwtService.extractUsername("refresh-token"))
                .thenReturn("student@gmail.com");


        when(userRepository.findByEmail("student@gmail.com"))
                .thenReturn(Optional.of(user));


        when(jwtService.extractTokenVersion("refresh-token"))
                .thenReturn(1L);


        when(jwtService.isTokenValid("refresh-token", user))
                .thenReturn(true);


        when(jwtService.generateAccessToken(user))
                .thenReturn("new-access-token");


        // Act
        AuthResponse response =
                authService.refreshToken(request);


        // Assert
        assertNotNull(response);

        assertEquals(
                "new-access-token",
                response.getAccessToken()
        );

        assertEquals(
                "refresh-token",
                response.getRefreshToken()
        );

        assertEquals(
                "Bearer",
                response.getTokenType()
        );


        // Verify important operations
        verify(jwtService)
                .extractUsername("refresh-token");

        verify(userRepository)
                .findByEmail("student@gmail.com");

        verify(jwtService)
                .extractTokenVersion("refresh-token");

        verify(jwtService)
                .isTokenValid("refresh-token", user);

        verify(jwtService)
                .generateAccessToken(user);


        // Refresh flow should NOT generate another refresh token
        verify(jwtService, never())
                .generateRefreshToken(any(User.class));
    }

    @Test
    void testRefreshTokenUserNotFound() {

        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        when(jwtService.extractUsername("refresh-token"))
                .thenReturn("missing@gmail.com");

        when(userRepository.findByEmail("missing@gmail.com"))
                .thenReturn(Optional.empty());


        // Act + Assert
        assertThrows(
                com.spring.course.management.system.exception.InvalidTokenException.class,
                () -> authService.refreshToken(request)
        );


        // Verify username was extracted
        verify(jwtService)
                .extractUsername("refresh-token");

        // Verify user lookup happened
        verify(userRepository)
                .findByEmail("missing@gmail.com");


        // These operations must NOT happen
        verify(jwtService, never())
                .extractTokenVersion(anyString());

        verify(jwtService, never())
                .isTokenValid(anyString(), any(User.class));

        verify(jwtService, never())
                .generateAccessToken(any(User.class));
    }

    @Test
    void testRefreshTokenVersionMismatch() {

        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("old-refresh-token");


        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPassword("password123");
        user.setRole(Role.STUDENT);
        user.setActive(true);

        // Current version in database
        user.setTokenVersion(2L);


        when(jwtService.extractUsername("old-refresh-token"))
                .thenReturn("student@gmail.com");

        when(userRepository.findByEmail("student@gmail.com"))
                .thenReturn(Optional.of(user));

        // Old token contains version 1
        when(jwtService.extractTokenVersion("old-refresh-token"))
                .thenReturn(1L);


        // Act + Assert
        assertThrows(
                com.spring.course.management.system.exception.InvalidTokenException.class,
                () -> authService.refreshToken(request)
        );


        // Verify required operations
        verify(jwtService)
                .extractUsername("old-refresh-token");

        verify(userRepository)
                .findByEmail("student@gmail.com");

        verify(jwtService)
                .extractTokenVersion("old-refresh-token");


        // Token validation must NOT happen
        verify(jwtService, never())
                .isTokenValid(anyString(), any(User.class));

        // New access token must NOT be generated
        verify(jwtService, never())
                .generateAccessToken(any(User.class));
    }

    @Test
    void testRefreshTokenInvalidOrExpired() {

        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("expired-refresh-token");


        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPassword("password123");
        user.setRole(Role.STUDENT);
        user.setActive(true);
        user.setTokenVersion(1L);


        when(jwtService.extractUsername("expired-refresh-token"))
                .thenReturn("student@gmail.com");

        when(userRepository.findByEmail("student@gmail.com"))
                .thenReturn(Optional.of(user));


        // Token version matches
        when(jwtService.extractTokenVersion("expired-refresh-token"))
                .thenReturn(1L);


        // But token itself is invalid/expired
        when(jwtService.isTokenValid(
                "expired-refresh-token",
                user
        )).thenReturn(false);


        // Act + Assert
        assertThrows(
                com.spring.course.management.system.exception.InvalidTokenException.class,
                () -> authService.refreshToken(request)
        );


        // Verify important operations
        verify(jwtService)
                .extractUsername("expired-refresh-token");

        verify(userRepository)
                .findByEmail("student@gmail.com");

        verify(jwtService)
                .extractTokenVersion("expired-refresh-token");

        verify(jwtService)
                .isTokenValid(
                        "expired-refresh-token",
                        user
                );


        // No new access token should be generated
        verify(jwtService, never())
                .generateAccessToken(any(User.class));
    }
}