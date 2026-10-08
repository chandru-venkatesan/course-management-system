package com.spring.course.management.system.service;

import com.spring.course.management.system.model.RevokedToken;
import com.spring.course.management.system.repository.RevokedTokenRepository;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private final RevokedTokenRepository revokedTokenRepository =
            mock(RevokedTokenRepository.class);

    private final TokenService tokenService =
            new TokenService(revokedTokenRepository);


    // =====================================================
    // TEST 1 — REVOKE NEW TOKEN
    // =====================================================

    @Test
    void testRevokeNewToken() {

        // Arrange
        String tokenId = "token-123";

        Date expiry =
                new Date(
                        System.currentTimeMillis()
                                + 60000
                );


        when(revokedTokenRepository
                .existsByTokenId(tokenId))
                .thenReturn(false);


        // Act
        tokenService.revokeToken(
                tokenId,
                expiry
        );


        // Assert
        ArgumentCaptor<RevokedToken> captor =
                ArgumentCaptor.forClass(
                        RevokedToken.class
                );


        verify(revokedTokenRepository)
                .save(captor.capture());


        RevokedToken savedToken =
                captor.getValue();


        assertEquals(
                tokenId,
                savedToken.getTokenId()
        );


        assertEquals(
                expiry,
                savedToken.getExpiry()
        );
    }


    // =====================================================
    // TEST 2 — ALREADY REVOKED TOKEN
    // =====================================================

    @Test
    void testRevokeAlreadyRevokedToken() {

        // Arrange
        String tokenId = "token-123";

        Date expiry =
                new Date(
                        System.currentTimeMillis()
                                + 60000
                );


        when(revokedTokenRepository
                .existsByTokenId(tokenId))
                .thenReturn(true);


        // Act
        tokenService.revokeToken(
                tokenId,
                expiry
        );


        // Assert
        verify(revokedTokenRepository)
                .existsByTokenId(tokenId);


        verify(revokedTokenRepository, never())
                .save(any(RevokedToken.class));
    }


    // =====================================================
    // TEST 3 — TOKEN IS REVOKED
    // =====================================================

    @Test
    void testIsTokenRevokedTrue() {

        // Arrange
        String tokenId = "token-123";


        when(revokedTokenRepository
                .existsByTokenId(tokenId))
                .thenReturn(true);


        // Act
        boolean result =
                tokenService.isTokenRevoked(
                        tokenId
                );


        // Assert
        assertTrue(result);


        verify(revokedTokenRepository)
                .existsByTokenId(tokenId);
    }


    // =====================================================
    // TEST 4 — TOKEN IS NOT REVOKED
    // =====================================================

    @Test
    void testIsTokenRevokedFalse() {

        // Arrange
        String tokenId = "token-456";


        when(revokedTokenRepository
                .existsByTokenId(tokenId))
                .thenReturn(false);


        // Act
        boolean result =
                tokenService.isTokenRevoked(
                        tokenId
                );


        // Assert
        assertFalse(result);


        verify(revokedTokenRepository)
                .existsByTokenId(tokenId);
    }
}