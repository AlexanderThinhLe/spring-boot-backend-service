package thinh.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import thinh.springboot.controller.request.LoginRequest;
import thinh.springboot.controller.response.TokenResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    @Operation(summary = "Get access token", description = "Get access token and refresh token by username and password")
    @PostMapping("/access-token")
    public TokenResponse getAccessToken(@RequestBody LoginRequest request) {
        log.info("Request access token");

        return TokenResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    @Operation(summary = "Get refresh token", description = "Get new access token by refresh token")
    @PostMapping("/refresh-token")
    public TokenResponse getRefreshToken(@RequestBody String refreshToken) {
        log.info("Request refresh token");

        return TokenResponse.builder()
                .accessToken("New accessToken")
                .refreshToken("refreshToken")
                .build();
    }
}
