package thinh.springboot.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import thinh.springboot.common.TokenType;
import thinh.springboot.exception.InvalidDataException;
import thinh.springboot.service.JwtService;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j(topic = "JWT-SERVICE-IMPL")
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.expireMinutes}")
    private long expireMinutes;

    @Value("${jwt.expireDays}")
    private long expireDays;

    @Value("${jwt.accessKeySecret}")
    private String accessKeySecret;

    @Value("${jwt.refreshKeySecret}")
    private String refreshKeySecret;
    
    @Override
    public String generateAccessToken(String username, List<String> authorities) {
        log.info("Generating access token for user: {}", username);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);

        return generateAccessToken(claims, username);
    }

    @Override
    public String generateRefreshToken(String username, List<String> authorities) {
        log.info("Generating refresh token for user: {}", username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);

        return generateRefreshToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType tokenType) {
        log.info("Extracting username from token: {}", token);
        return extractClaim(token, tokenType, Claims::getSubject);
    }

    private String generateAccessToken(Map<String, Object> claims, String username) {
        log.info("----------[ generateAccessToken ]----------");

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expireMinutes))
            .signWith(getSignInKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
            .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        log.info("----------[ generateRefreshToken ]----------");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expireDays))
                .signWith(getSignInKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    // Access "https://www.devglan.com/online-tools/hmac-sha256-online" for generate accessTokenSecret and refreshTokenSecret
    // Enter plain text for compute hash: KeyForAccessToken
    // Enter the Secret Key: AlexanderThinhLe
    // Cryptographic hash function: SHA-256, base64
    private Key getSignInKey(TokenType tokenType) {
        String accessTokenSecret = accessKeySecret;
        String refreshTokenSecret = refreshKeySecret;

        switch (tokenType) {
            case ACCESS_TOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
            case REFRESH_TOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecret));
            default:
                throw new InvalidDataException("Invalid token type");
        }
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        log.info("----------[ extractClaim ]----------");
        final Claims claims = extraAllClaim(token, type);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type) {
        log.info("----------[ extraAllClaim ]----------");
        try {
            return Jwts.parserBuilder().setSigningKey(getSignInKey(type)).build().parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }
}
