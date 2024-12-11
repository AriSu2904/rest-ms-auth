package com.service.authorization.controller;

import com.service.authorization.dto.common.CommonResponse;
import com.service.authorization.dto.response.StudentResponse;
import com.service.authorization.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
@Tag(name = "Student", description = "APIs for student")
public class StudentController {

    private final StudentService studentService;

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<StudentResponse>> me(Authentication authentication) {
        StudentResponse profile = studentService.profile(authentication);

        CommonResponse<StudentResponse> response = CommonResponse.<StudentResponse>builder()
                .data(profile)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/profile-picture",
            consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
            )
    public ResponseEntity<CommonResponse<StudentResponse>> updateProfile(
            Authentication authentication,
            @RequestPart("image") MultipartFile multipartFile) {
        StudentResponse studentResponse = studentService.updateProfilePicture(authentication, multipartFile);

        CommonResponse<StudentResponse> response = CommonResponse.<StudentResponse>builder()
                .data(studentResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/profile-picture/{imageId}")
    public ResponseEntity<?> profilePicture(@PathVariable(name = "imageId") String id){
        Resource resource = studentService.loadProfilePicture(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
