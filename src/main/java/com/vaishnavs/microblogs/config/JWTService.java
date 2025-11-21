package com.vaishnavs.microblogs.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
  @Value("${jwt.secretkey}")
  private String secretkey;
  private final long jwtExpirationInMillis = 60 * 60 * 24 * 7 * 1000L;

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(secretkey.getBytes());
  }

  public String generateToken(String userId) {
    Map<String, Object> claims = new HashMap<>();
    String token = Jwts.builder()
        .claims()
        .add(claims)
        .subject(userId)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
        .and()
        .signWith(getKey())
        .compact();
    return token;
  }

  public String validatedJWTToken(String token) {
    try {
      Claims claims = Jwts.parser()
          .verifyWith(getKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
      return claims.getSubject();
    } catch (Exception e) {
      throw new RuntimeException("Invalid JWT token: " + e.getMessage());
    }
  }
}