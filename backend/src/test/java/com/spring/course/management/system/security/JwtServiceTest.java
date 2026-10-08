package com.spring.course.management.system.security;

import com.spring.course.management.system.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;


    // =====================================================
    // TEST SETUP
    // =====================================================

    @BeforeEach
    void setUp() throws Exception {

        setField(
                jwtService,
                "secret",
                "ThisIsMyCourseManagementSystemSecretKeyForJWTAuthentication123456"
        );

        setField(
                jwtService,
                "expiration",
                900000L
        );

        setField(
                jwtService,
                "refreshExpiration",
                604800000L
        );
    }


    // =====================================================
    // TEST 1 — GENERATE ACCESS TOKEN
    // =====================================================

    @Test
    void testGenerateAccessToken() {

        User user = createUser();

        String token =
                jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }


    // =====================================================
    // TEST 2 — GENERATE REFRESH TOKEN
    // =====================================================

    @Test
    void testGenerateRefreshToken() {

        User user = createUser();

        String token =
                jwtService.generateRefreshToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }


    // =====================================================
    // TEST 3 — EXTRACT USERNAME
    // =====================================================

    @Test
    void testExtractUsername() {

        User user = createUser();

        String token =
                jwtService.generateAccessToken(user);

        String username =
                jwtService.extractUsername(token);

        assertEquals(
                "student@gmail.com",
                username
        );
    }


    // =====================================================
    // TEST 4 — EXTRACT TOKEN VERSION
    // =====================================================

    @Test
    void testExtractTokenVersion() {

        User user = createUser();

        user.setTokenVersion(5L);

        String token =
                jwtService.generateAccessToken(user);

        Long tokenVersion =
                jwtService.extractTokenVersion(token);

        assertEquals(
                5L,
                tokenVersion
        );
    }


    // =====================================================
    // TEST 5 — EXTRACT JWT ID
    // =====================================================

    @Test
    void testExtractTokenId() {

        User user = createUser();

        String token =
                jwtService.generateAccessToken(user);

        String tokenId =
                jwtService.extractTokenId(token);

        assertNotNull(tokenId);
        assertFalse(tokenId.isBlank());
    }


    // =====================================================
    // TEST 6 — EXTRACT EXPIRATION
    // =====================================================

    @Test
    void testExtractExpiration() {

        User user = createUser();

        long beforeGeneration =
                System.currentTimeMillis();

        String token =
                jwtService.generateAccessToken(user);

        long afterGeneration =
                System.currentTimeMillis();

        Date expiration =
                jwtService.extractExpiration(token);

        assertNotNull(expiration);

        assertTrue(
                expiration.getTime() > beforeGeneration
        );

        assertTrue(
                expiration.getTime() > afterGeneration
        );
    }


    // =====================================================
    // TEST 7 — VALID TOKEN
    // =====================================================

    @Test
    void testIsTokenValid() {

        User user = createUser();

        String token =
                jwtService.generateAccessToken(user);

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        user
                );

        assertTrue(valid);
    }


    // =====================================================
    // TEST 8 — TOKEN BELONGS TO DIFFERENT USER
    // =====================================================

    @Test
    void testIsTokenInvalidForDifferentUser() {

        User tokenUser = createUser();

        String token =
                jwtService.generateAccessToken(
                        tokenUser
                );


        User differentUser = new User();

        differentUser.setEmail(
                "trainer@gmail.com"
        );

        differentUser.setPassword(
                "password123"
        );

        differentUser.setTokenVersion(1L);

        differentUser.setActive(true);


        boolean valid =
                jwtService.isTokenValid(
                        token,
                        differentUser
                );

        assertFalse(valid);
    }


    // =====================================================
    // TEST 9 — EXPIRED TOKEN
    // =====================================================

    @Test
    void testIsTokenInvalidWhenExpired()
            throws Exception {

        User user = createUser();


        // Set expiration to the past
        setField(
                jwtService,
                "expiration",
                -1000L
        );


        String token =
                jwtService.generateAccessToken(user);


        // JJWT throws ExpiredJwtException
        // when an expired token is parsed.
        assertThrows(
                io.jsonwebtoken.ExpiredJwtException.class,
                () -> jwtService.isTokenValid(
                        token,
                        user
                )
        );
    }


    // =====================================================
    // TEST 10 — ACCESS AND REFRESH TOKENS
    // HAVE DIFFERENT JWT IDs
    // =====================================================

    @Test
    void testAccessAndRefreshTokensHaveDifferentIds() {

        User user = createUser();

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);


        String accessTokenId =
                jwtService.extractTokenId(
                        accessToken
                );

        String refreshTokenId =
                jwtService.extractTokenId(
                        refreshToken
                );


        assertNotNull(accessTokenId);
        assertNotNull(refreshTokenId);

        assertNotEquals(
                accessTokenId,
                refreshTokenId
        );
    }


    // =====================================================
    // HELPER — CREATE USER
    // =====================================================

    private User createUser() {

        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPassword("password123");
        user.setTokenVersion(1L);
        user.setActive(true);

        return user;
    }


    // =====================================================
    // HELPER — SET PRIVATE FIELD
    // =====================================================

    private void setField(
            Object target,
            String fieldName,
            Object value)
            throws Exception {

        Field field =
                target.getClass()
                        .getDeclaredField(fieldName);

        field.setAccessible(true);

        field.set(
                target,
                value
        );
    }
}