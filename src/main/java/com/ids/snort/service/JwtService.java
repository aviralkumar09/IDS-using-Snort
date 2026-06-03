package com.ids.snort.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final Key signingKey;
  private final long expirationMillis;

  public JwtService(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.expiration-ms:86400000}") long expirationMillis) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMillis = expirationMillis;
  }

  public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMillis);
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    Date expiry = extractClaim(token, Claims::getExpiration);
    return expiry.before(new Date());
  }

  private <T> T extractClaim(String token, Function<Claims, T> resolver) {
    Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token)
        .getBody();
    return resolver.apply(claims);
  }
}
