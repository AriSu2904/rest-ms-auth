package com.service.authorization.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String extractStudentId(String token);
    boolean validateToken(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    Claims extractAllClaims(String token);
    String generateRefreshToken(Map<String, Object> extractClaims, UserDetails userDetails);
}
