package com.service.authorization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String studentId;
    private String nickname;
    private StudentResponse student;
    private String role;
    private String token;
    private String refreshToken;
}
