package com.spring.course.management.system.security;

import com.spring.course.management.system.model.User;
import com.spring.course.management.system.repository.UserRepository;
import com.spring.course.management.system.service.TokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(
            UserRepository userRepository,
            UserDetailsService userDetailsService,
            JwtService jwtService,
            TokenService tokenService) {

        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    /*
     * These authentication endpoints do not need
     * JWT authentication processing.
     *
     * Login:
     *      No token exists yet.
     *
     * Refresh:
     *      Refresh token is handled by AuthService.
     *
     * Logout:
     *      The access token is read directly by
     *      AuthController and then revoked.
     */
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String uri =
                request.getRequestURI();

        return uri.equals("/api/auth/login")
                || uri.equals("/api/auth/refresh")
                || uri.equals("/api/auth/logout");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        /*
         * No Bearer token.
         *
         * Continue normally.
         */
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        final String token =
                authHeader.substring(7);

        try {


             // Extract username/email from JWT.

            String username =
                    jwtService.extractUsername(token);


             // Find the user in database.

            User user =
                    userRepository.findByEmail(username)
                            .orElse(null);

            if (user == null) {

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }


             // Extract JWT ID.

            String tokenId =
                    jwtService.extractTokenId(token);

            /*
             * Check whether this token
             * has been blacklisted/revoked.
             */
            if (tokenService.isTokenRevoked(tokenId)) {

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }

            /*
             * Check token version.
             */
            Long tokenVersion =
                    jwtService.extractTokenVersion(token);

            if (tokenVersion == null ||
                    !tokenVersion.equals(
                            user.getTokenVersion())) {

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }

            /*
             * Load Spring Security UserDetails.
             */
            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            /*
             * Validate the JWT.
             */
            if (jwtService.isTokenValid(
                    token,
                    user)) {

                UsernamePasswordAuthenticationToken
                        authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                /*
                 * Tell Spring Security that
                 * this request is authenticated.
                 */
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authentication
                        );
            }

        } catch (JwtException |
                 IllegalArgumentException exception) {

            /*
             * Invalid JWT.
             *
             * Do not authenticate the request.
             * Continue the filter chain.
             */
        }

        /*
         * Continue to the next filter/controller.
         */
        filterChain.doFilter(
                request,
                response
        );
    }
}