package com.service.authorization.service.Impl;

import com.service.authorization.service.JwtService;
import com.service.authorization.utils.KeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public JwtServiceImpl() {
        try {
            this.privateKey = KeyLoader.loadPrivateKey("src/main/resources/private_key.pem");
            this.publicKey = KeyLoader.getPublicPath("src/main/resources/public_key.pem");
        } catch (Exception e) {
            log.error("[JWT Service] Error loading key: {}", e.getMessage());
        }
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        var expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 24);
        log.info("Generating token with expired time: {}", expiration);

        try {
            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("role", userDetails.getAuthorities())
                    .claim("id", userDetails.getUsername())
                    .setIssuer("com.service.authorization")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expiration)
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String extractStudentId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateRefreshToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        try {

            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("role", userDetails.getAuthorities())
                    .claim("id", userDetails.getUsername())
                    .setIssuer("com.service.authorization")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String studentId = extractStudentId(token);
        return studentId.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        try {
            log.info("Extracting all claims from token");

            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("Error extracting all claims: {}", e.getMessage());

            throw e;
        }
    }
}
