package com.spring.course.management.system.security;

import com.spring.course.management.system.rate.RateLimitService;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(
            RateLimitService rateLimitService) {

        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String uri =
                request.getRequestURI();

        String clientIp =
                request.getRemoteAddr();

        /*
         * Authentication APIs
         */
        if (uri.equals("/api/auth/login")
                || uri.equals("/api/auth/refresh")) {

            String key =
                    "rate_limit:auth:" + clientIp;

            ConsumptionProbe probe =
                    rateLimitService.tryConsumeAuth(key);

            if (!probe.isConsumed()) {

                System.out.println(
                        "[RATE-LIMIT] Request blocked | IP: "
                                + clientIp
                                + " | URI: "
                                + uri
                );

                sendRateLimitResponse(
                        response,
                        probe
                );

                return;
            }
        }

        /*
         * General APIs
         */
        else if (uri.startsWith("/api/courses")
                || uri.startsWith("/api/students")
                || uri.startsWith("/api/trainers")) {

            String key =
                    "rate_limit:api:" + clientIp;

            ConsumptionProbe probe =
                    rateLimitService.tryConsumeApi(key);

            if (!probe.isConsumed()) {

                sendRateLimitResponse(
                        response,
                        probe
                );

                return;
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }

    private void sendRateLimitResponse(
            HttpServletResponse response,
            ConsumptionProbe probe)
            throws IOException {

        long waitSeconds =
                (long) Math.ceil(
                        probe.getNanosToWaitForRefill()
                                / 1_000_000_000.0
                );

        response.setStatus(429);

        response.setHeader(
                "Retry-After",
                String.valueOf(waitSeconds)
        );

        response.setContentType(
                "application/json"
        );

        response.getWriter().write(
                "{\"status\":429,"
                        + "\"errorCode\":\"RATE_LIMIT_EXCEEDED\","
                        + "\"message\":\"Too many requests. Please try again later.\"}"
        );
    }
}