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
public class RegisterRequest {
    @NotNull(message = "studentId is required")
    @NotEmpty(message = "studentId must not be empty")
    @Pattern(regexp = "^[0-9]+$", message = "studentId must contain only numbers")
    private String studentId;
    @NotNull(message = "password is required")
    @NotEmpty(message = "password must not be empty")
    private String password;

}
