package com.example.userservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private final long expirationMs = 1000 * 60 * 60; // 1시간

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Long id,String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)  // ✅ userId 포함!
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ 추가된 메서드
    public String extractUsername(String token) {
        return parseToken(token).getSubject(); // username은 subject로 저장됨
    }

    // (선택) 사용자 역할도 필요하면 이렇게 추가 가능
    public String extractRole(String token) {
        return parseToken(token).get("role", String.class);
    }
}
