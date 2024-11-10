package com.service.authorization.controller;

import com.service.authorization.dto.common.CommonResponse;
import com.service.authorization.dto.request.LoginRequest;
import com.service.authorization.dto.request.RefreshTokenRequest;
import com.service.authorization.dto.request.RegisterRequest;
import com.service.authorization.dto.response.LoginResponse;
import com.service.authorization.dto.response.RefreshTokenResponse;
import com.service.authorization.dto.response.RegisterResponse;
import com.service.authorization.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse login = authService.login(request, false);

        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<RefreshTokenResponse>> refresh(@RequestHeader(name = "X-Request-ID") RefreshTokenRequest token) {
        RefreshTokenResponse login = authService.refreshToken(token);

        CommonResponse<RefreshTokenResponse> response = CommonResponse.<RefreshTokenResponse>builder()
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse register = authService.register(request);

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logout";
    }
}
