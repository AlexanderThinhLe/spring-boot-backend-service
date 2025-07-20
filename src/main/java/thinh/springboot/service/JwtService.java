package thinh.springboot.service;

import thinh.springboot.common.TokenType;

import java.util.List;

public interface JwtService {
    String generateAccessToken(String username, List<String> authorities);
    String generateRefreshToken(String username, List<String> authorities);
    String extractUsername(String token, TokenType tokenType);

}
