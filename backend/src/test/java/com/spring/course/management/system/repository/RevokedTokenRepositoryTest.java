package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.RevokedToken;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class RevokedTokenRepositoryTest {

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;


    // =====================================================
    // TEST 1 — SAVE REVOKED TOKEN
    // =====================================================

    @Test
    void testSaveRevokedToken() {

        RevokedToken token =
                createToken("save-token-123");

        RevokedToken savedToken =
                revokedTokenRepository.save(token);


        assertNotNull(savedToken.getId());

        assertEquals(
                "save-token-123",
                savedToken.getTokenId()
        );

        assertNotNull(savedToken.getExpiry());
    }


    // =====================================================
    // TEST 2 — FIND ALL REVOKED TOKENS
    // =====================================================

    @Test
    void testFindAllRevokedTokens() {

        RevokedToken token1 =
                revokedTokenRepository.save(
                        createToken("find-all-token-1")
                );

        RevokedToken token2 =
                revokedTokenRepository.save(
                        createToken("find-all-token-2")
                );


        List<RevokedToken> tokens =
                revokedTokenRepository.findAll();


        assertTrue(tokens.size() >= 2);


        assertTrue(
                tokens.stream()
                        .anyMatch(token ->
                                token.getId()
                                        .equals(token1.getId()))
        );


        assertTrue(
                tokens.stream()
                        .anyMatch(token ->
                                token.getId()
                                        .equals(token2.getId()))
        );
    }


    // =====================================================
    // TEST 3 — FIND BY ID
    // =====================================================

    @Test
    void testFindRevokedTokenById() {

        RevokedToken token =
                revokedTokenRepository.save(
                        createToken("find-by-id-token")
                );


        Optional<RevokedToken> result =
                revokedTokenRepository.findById(
                        token.getId()
                );


        assertTrue(result.isPresent());

        assertEquals(
                "find-by-id-token",
                result.get().getTokenId()
        );
    }


    // =====================================================
    // TEST 4 — FIND BY ID NOT FOUND
    // =====================================================

    @Test
    void testFindRevokedTokenByIdNotFound() {

        Optional<RevokedToken> result =
                revokedTokenRepository.findById(99999L);


        assertFalse(result.isPresent());
    }


    // =====================================================
    // TEST 5 — UPDATE REVOKED TOKEN
    // =====================================================

    @Test
    void testUpdateRevokedToken() {

        RevokedToken token =
                revokedTokenRepository.save(
                        createToken("original-token")
                );


        token.setTokenId("updated-token");


        RevokedToken updatedToken =
                revokedTokenRepository.save(token);


        assertEquals(
                "updated-token",
                updatedToken.getTokenId()
        );


        Optional<RevokedToken> result =
                revokedTokenRepository.findById(
                        token.getId()
                );


        assertTrue(result.isPresent());

        assertEquals(
                "updated-token",
                result.get().getTokenId()
        );
    }


    // =====================================================
    // TEST 6 — DELETE REVOKED TOKEN
    // =====================================================

    @Test
    void testDeleteRevokedToken() {

        RevokedToken token =
                revokedTokenRepository.save(
                        createToken("delete-token")
                );


        Long tokenId =
                token.getId();


        revokedTokenRepository.delete(token);


        Optional<RevokedToken> result =
                revokedTokenRepository.findById(tokenId);


        assertFalse(result.isPresent());
    }


    // =====================================================
    // TEST 7 — EXISTS BY TOKEN ID
    // =====================================================

    @Test
    void testExistsByTokenId() {

        RevokedToken token =
                createToken("exists-token");


        revokedTokenRepository.save(token);


        boolean exists =
                revokedTokenRepository
                        .existsByTokenId("exists-token");


        assertTrue(exists);


        boolean doesNotExist =
                revokedTokenRepository
                        .existsByTokenId("unknown-token");


        assertFalse(doesNotExist);
    }


    // =====================================================
    // HELPER METHOD
    // =====================================================

    private RevokedToken createToken(
            String tokenId) {

        RevokedToken token =
                new RevokedToken();


        token.setTokenId(tokenId);


        token.setExpiry(
                new Date(
                        System.currentTimeMillis()
                                + 3600000
                )
        );


        return token;
    }
}