package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository
        extends JpaRepository<RevokedToken, Long> {

    boolean existsByTokenId(String tokenId);
}