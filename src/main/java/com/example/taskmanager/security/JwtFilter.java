package com.example.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RateLimiterService rateLimiterService;

    public JwtFilter(
            JwtService jwtService,
            RateLimiterService rateLimiterService
    ) {
        this.jwtService = jwtService;
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ SKIP SWAGGER + H2 + AUTH ENDPOINTS
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/h2-console")
                || path.startsWith("/api/auth")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 STEP 1: Identify user for rate limiting
        String clientKey;

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                clientKey = jwtService.extractUsername(token);

            } catch (Exception e) {
                clientKey = request.getRemoteAddr();
            }

        } else {
            clientKey = request.getRemoteAddr();
        }

        // 🔥 STEP 2: Apply rate limiting
        if (!rateLimiterService.isAllowed(clientKey)) {

            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return;
        }

        // 🔥 STEP 3: JWT Authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + role);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(authority)
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authToken);

        } catch (Exception e) {

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}