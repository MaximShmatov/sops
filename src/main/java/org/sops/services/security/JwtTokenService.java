package org.sops.services.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.sops.database.entities.UserEntity;
import org.sops.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    @Value("${security.signing-key}")
    private String jwtSigningKey;
    private final UserService userService;

    public UsernamePasswordAuthenticationToken getAuthToken(String username) {
        var user = userService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public boolean isValidToken(String token, Claims claims) {
        final var username = claims.getSubject();
        var user = userService.loadUserByUsername(username);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    public String generateToken(UserEntity user) {
        long currentDate = OffsetDateTime.now().toInstant().toEpochMilli();
        long expirationDate = OffsetDateTime.now().plusHours(1).toInstant().toEpochMilli();
        return generateToken(Map.ofEntries(
                Map.entry("sub", user.getUsername()),
                Map.entry("exp", new Date(expirationDate)),
                Map.entry("iat", new Date(currentDate))
        ));
    }

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(getSigningKey(), SIG.HS256)
                .compact();
    }


    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token)
                .getExpiration()
                .before(new Date());
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}