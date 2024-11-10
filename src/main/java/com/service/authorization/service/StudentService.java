package com.service.authorization.service;

import com.service.authorization.dto.response.StudentResponse;
import com.service.authorization.entity.Student;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    Student create(Student student);

    Student findByStudentId(String studentId);

    StudentResponse profile(Authentication authentication);

    StudentResponse updateProfilePicture(Authentication authentication, MultipartFile multipartFile);

    Resource loadProfilePicture(String id);
}
