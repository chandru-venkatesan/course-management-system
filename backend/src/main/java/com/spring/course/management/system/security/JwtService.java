package com.spring.course.management.system.security;

import com.spring.course.management.system.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;



    // SIGNING KEY

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }



    // GENERATE ACCESS TOKEN

    public String generateAccessToken(User user) {

        return generateToken(
                user,
                expiration
        );
    }



    // GENERATE REFRESH TOKEN

    public String generateRefreshToken(User user) {

        return generateToken(
                user,
                refreshExpiration
        );
    }



    // GENERATE TOKEN

    private String generateToken(
            User user,
            long tokenExpiration) {

        Map<String, Object> claims =
                new HashMap<>();

        // Store user's current token version
        claims.put(
                "tokenVersion",
                user.getTokenVersion()
        );

        return Jwts.builder()

                // Custom claims
                .claims(claims)

                // User email
                .subject(user.getEmail())

                // Unique JWT ID
                .id(UUID.randomUUID().toString())

                // Created time
                .issuedAt(new Date())

                // Expiration time
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + tokenExpiration
                        )
                )

                // Sign token
                .signWith(getSigningKey())

                // Convert to String
                .compact();
    }



    // EXTRACT USERNAME

    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }



    // EXTRACT TOKEN VERSION

    public Long extractTokenVersion(String token) {

        return extractClaim(
                token,
                claims -> {
                    Number version =
                            claims.get("tokenVersion", Number.class);

                    return version != null
                            ? version.longValue()
                            : null;
                }
        );
    }



    // EXTRACT JWT ID

    public String extractTokenId(String token) {

        return extractClaim(
                token,
                Claims::getId
        );
    }



    // EXTRACT EXPIRATION

    public Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }



    // VALIDATE TOKEN

    public boolean isTokenValid(
            String token,
            User user) {

        String username =
                extractUsername(token);

        return username.equals(user.getEmail())
                && !isTokenExpired(token);
    }



    // CHECK TOKEN EXPIRATION

    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }



    // GENERIC CLAIM EXTRACTOR

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims =
                Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return claimsResolver.apply(claims);
    }
}