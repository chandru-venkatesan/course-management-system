package com.spring.course.management.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.management.system.dto.AuthResponse;
import com.spring.course.management.system.dto.LoginRequest;
import com.spring.course.management.system.dto.RefreshTokenRequest;
import com.spring.course.management.system.security.JwtAuthenticationFilter;
import com.spring.course.management.system.security.JwtService;
import com.spring.course.management.system.security.RateLimitFilter;
import com.spring.course.management.system.service.AuthService;
import com.spring.course.management.system.service.TokenService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Create ObjectMapper manually.
     */
    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    /*
     * Mock AuthService.
     */
    @MockitoBean
    private AuthService authService;

    /*
     * Mock JwtService.
     */
    @MockitoBean
    private JwtService jwtService;

    /*
     * Mock TokenService.
     */
    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private RateLimitFilter rateLimitFilter;


    // =========================================================
    // 1. LOGIN
    // =========================================================

    @Test
    void testLogin() throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail("student@gmail.com");
        request.setPassword("password123");


        AuthResponse response =
                new AuthResponse(
                        "access-token-123",
                        "refresh-token-123",
                        "Bearer"
                );


        when(
                authService.login(
                        any(LoginRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.accessToken")
                                .value("access-token-123")
                )
                .andExpect(
                        jsonPath("$.refreshToken")
                                .value("refresh-token-123")
                )
                .andExpect(
                        jsonPath("$.tokenType")
                                .value("Bearer")
                );
    }


    // =========================================================
    // 2. REFRESH TOKEN
    // =========================================================

    @Test
    void testRefreshToken() throws Exception {

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken(
                "refresh-token-123"
        );


        AuthResponse response =
                new AuthResponse(
                        "new-access-token-123",
                        "refresh-token-123",
                        "Bearer"
                );


        when(
                authService.refreshToken(
                        any(RefreshTokenRequest.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        post("/api/auth/refresh")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.accessToken")
                                .value("new-access-token-123")
                )
                .andExpect(
                        jsonPath("$.refreshToken")
                                .value("refresh-token-123")
                )
                .andExpect(
                        jsonPath("$.tokenType")
                                .value("Bearer")
                );
    }


    // =========================================================
    // 3. SUCCESSFUL LOGOUT
    // =========================================================

    @Test
    void testLogout() throws Exception {

        String token =
                "access-token-123";

        String tokenId =
                "token-id-123";

        Date expiration =
                new Date(
                        System.currentTimeMillis()
                                + 60000
                );


        when(
                jwtService.extractExpiration(token)
        ).thenReturn(expiration);


        when(
                jwtService.extractTokenId(token)
        ).thenReturn(tokenId);


        mockMvc.perform(
                        post("/api/auth/logout")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "Logout successful"
                        )
                );


        verify(jwtService)
                .extractExpiration(token);

        verify(jwtService)
                .extractTokenId(token);

        verify(tokenService)
                .revokeToken(
                        eq(tokenId),
                        eq(expiration)
                );
    }


    // =========================================================
    // 4. LOGOUT WITH INVALID AUTHORIZATION FORMAT
    // =========================================================

    @Test
    void testLogoutWithInvalidAuthorizationHeader()
            throws Exception {

        mockMvc.perform(
                        post("/api/auth/logout")
                                .header(
                                        "Authorization",
                                        "InvalidToken"
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        content().string(
                                "Authorization token is required"
                        )
                );


        /*
         * JWT should never be processed when the
         * Authorization header is invalid.
         */
        verify(
                jwtService,
                never()
        ).extractExpiration(
                any(String.class)
        );

        verify(
                jwtService,
                never()
        ).extractTokenId(
                any(String.class)
        );

        verify(
                tokenService,
                never()
        ).revokeToken(
                any(String.class),
                any(Date.class)
        );
    }


    // =========================================================
    // 5. LOGOUT WITH BASIC AUTHORIZATION
    // =========================================================

    @Test
    void testLogoutWithBasicAuthorization()
            throws Exception {

        mockMvc.perform(
                        post("/api/auth/logout")
                                .header(
                                        "Authorization",
                                        "Basic abc123"
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        content().string(
                                "Authorization token is required"
                        )
                );


        verify(
                jwtService,
                never()
        ).extractExpiration(
                any(String.class)
        );

        verify(
                jwtService,
                never()
        ).extractTokenId(
                any(String.class)
        );

        verify(
                tokenService,
                never()
        ).revokeToken(
                any(String.class),
                any(Date.class)
        );
    }
}