package com.service.authorization.controller;

import com.service.authorization.dto.common.CommonResponse;
import com.service.authorization.dto.request.LoginRequest;
import com.service.authorization.dto.request.RefreshTokenRequest;
import com.service.authorization.dto.request.RegisterRequest;
import com.service.authorization.dto.response.LoginResponse;
import com.service.authorization.dto.response.RefreshTokenResponse;
import com.service.authorization.dto.response.RegisterResponse;
import com.service.authorization.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "APIs for authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse login = authService.login(request, false);

        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/refresh", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommonResponse<RefreshTokenResponse>> refresh(@RequestBody RefreshTokenRequest refreshToken) {

        RefreshTokenResponse login = authService.refreshToken(refreshToken);

        CommonResponse<RefreshTokenResponse> response = CommonResponse.<RefreshTokenResponse>builder()
                .data(login)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse register = authService.register(request);

        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .data(register)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
