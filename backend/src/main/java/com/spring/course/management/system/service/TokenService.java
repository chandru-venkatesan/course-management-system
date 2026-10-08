package com.spring.course.management.system.service;

import com.spring.course.management.system.model.RevokedToken;
import com.spring.course.management.system.repository.RevokedTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private final RevokedTokenRepository revokedTokenRepository;

    public TokenService(
            RevokedTokenRepository revokedTokenRepository) {

        this.revokedTokenRepository =
                revokedTokenRepository;
    }

    public void revokeToken(
            String tokenId,
            Date expiry) {

        if (!revokedTokenRepository
                .existsByTokenId(tokenId)) {

            RevokedToken revokedToken =
                    new RevokedToken();

            revokedToken.setTokenId(tokenId);
            revokedToken.setExpiry(expiry);

            revokedTokenRepository.save(
                    revokedToken
            );
        }
    }

    public boolean isTokenRevoked(
            String tokenId) {

        return revokedTokenRepository
                .existsByTokenId(tokenId);
    }
}