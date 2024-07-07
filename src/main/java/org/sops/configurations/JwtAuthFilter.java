package org.sops.configurations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sops.services.security.JwtTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(AUTH_HEADER);
        if (Objects.nonNull(authHeader) && !authHeader.isBlank() && authHeader.startsWith(TOKEN_PREFIX)) {
            var jwtToken = authHeader.substring(TOKEN_PREFIX.length());
            var claims = jwtTokenService.getClaimsFromToken(jwtToken);
            if (jwtTokenService.isValidToken(jwtToken, claims)) {
                var authToken = jwtTokenService.getAuthToken(claims.getSubject());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                response.setHeader(AUTH_HEADER, jwtTokenService.generateToken(claims));
            }
        }
        filterChain.doFilter(request, response);
    }
}
