package com.service.authorization.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "studentId is required")
    @NotEmpty(message = "studentId must not be empty")
    private String studentId;
    @NotEmpty(message = "password must not be empty")
    @NotNull(message = "password is required")
    private String password;
}
