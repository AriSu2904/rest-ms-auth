package com.service.authorization.service;

import com.service.authorization.dto.request.LoginRequest;
import com.service.authorization.dto.request.RefreshTokenRequest;
import com.service.authorization.dto.request.RegisterRequest;
import com.service.authorization.dto.response.LoginResponse;
import com.service.authorization.dto.response.RefreshTokenResponse;
import com.service.authorization.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request, boolean isAdmin);
    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest token);
}
