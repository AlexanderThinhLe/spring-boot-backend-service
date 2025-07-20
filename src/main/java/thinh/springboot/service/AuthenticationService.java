package thinh.springboot.service;

import thinh.springboot.controller.request.LoginRequest;
import thinh.springboot.controller.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(LoginRequest request);
    TokenResponse getRefreshToken(String request);
}
