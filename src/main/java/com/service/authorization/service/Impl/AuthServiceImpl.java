package com.service.authorization.service.Impl;

import com.service.authorization.constant.ERole;
import com.service.authorization.dto.request.RefreshTokenRequest;
import com.service.authorization.dto.response.*;
import com.service.authorization.entity.EntityCredential;
import com.service.authorization.entity.Student;
import com.service.authorization.dto.request.LoginRequest;
import com.service.authorization.dto.request.RegisterRequest;
import com.service.authorization.service.AuthService;
import com.service.authorization.service.JwtService;
import com.service.authorization.service.StudentDataCenterService;
import com.service.authorization.service.StudentService;
import com.service.authorization.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final StudentService studentService;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl credentialService;
    private final StudentDataCenterService dataCenterService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtils validationUtils;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("[AUTH] Registering student with NIM: {}", request.getStudentId());
        try {
            validationUtils.validate(request);

            var studentDetail = dataCenterService.studentDetail(request.getStudentId());

            var credential = new EntityCredential();
            credential.setStudentId(request.getStudentId());
            credential.setPassword(passwordEncoder.encode(request.getPassword()));
            credential.setRole(ERole.ROLE_STUDENT.name());

            credentialService.create(credential);

            var newStudent = Student.builder()
                    .fullName(studentDetail.getFullName())
                    .studentId(studentDetail.getStudentId())
                    .campus(studentDetail.getCampus())
                    .major(studentDetail.getMajor())
                    .status(studentDetail.getStatus())
                    .gender(studentDetail.getGender())
                    .level(studentDetail.getLevel())
                    .joinDate(studentDetail.getJoinDate())
                    .credential(credential)
                    .build();

            Student student = studentService.create(newStudent);

            log.info("[AUTH] Student with NIM: {} has been registered", request.getStudentId());
            return RegisterResponse.builder()
                    .fullName(student.getFullName())
                    .studentId(student.getStudentId())
                    .build();
        } catch (Exception e) {
            log.error("[AUTH] Error registering student with NIM: {}", request.getStudentId());
            if (e instanceof DuplicateKeyException) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NIM already exists!");
            }

            log.error("[AUTH] : Error occured while request {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occured");
        }
    }

    @Override
    public LoginResponse login(LoginRequest request, Boolean isAdmin) {
        log.info("[AUTH] incoming login request isAdmin: {}", isAdmin);

        try {
            validationUtils.validate(request);

            Authentication authenticated = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getStudentId(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authenticated);
            EntityCredential credential = (EntityCredential) authenticated.getPrincipal();

            String token = jwtService.generateToken(credential);
            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), credential);

            return LoginResponse.builder()
                    .studentId(credential.getStudentId())
                    .role(credential.getRole())
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            log.error("[AUTH] Error login request: {}", e.getMessage());

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect NIM or Password!");
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest token) {
        log.info("[AUTH] incoming refresh token request");

        try {
            validationUtils.validate(token);

            String studentId = jwtService.extractStudentId(token.getRefreshToken());
            EntityCredential credential = (EntityCredential) credentialService.loadUserByUsername(studentId);

            if (jwtService.validateToken(token.getRefreshToken(), credential)) {
                log.info("[AUTH] processing new token");

                String newToken = jwtService.generateToken(credential);
                String newRefreshToken = jwtService.generateRefreshToken(new HashMap<>(), credential);

                return RefreshTokenResponse.builder()
                        .studentId(credential.getStudentId())
                        .role(credential.getRole())
                        .token(newToken)
                        .refreshToken(newRefreshToken)
                        .build();
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        } catch (Exception e) {
            log.error("[AUTH] Error refresh token request: {}", e.getMessage());

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
    }
}
