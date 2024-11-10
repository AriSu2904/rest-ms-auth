package com.service.authorization.service.Impl;

import com.service.authorization.dto.response.FileResponse;
import com.service.authorization.dto.response.StudentResponse;
import com.service.authorization.entity.EntityCredential;
import com.service.authorization.entity.ProfilePicture;
import com.service.authorization.entity.Student;
import com.service.authorization.repository.StudentRepository;
import com.service.authorization.service.ProfilePictureService;
import com.service.authorization.service.StudentService;
import com.service.authorization.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.Resource;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ProfilePictureService profilePictureService;
    private final UserDetailsServiceImpl credentialService;

    @Override
    public Student create(Student student) {
        Student existStudent = this.findByStudentId(student.getStudentId());
        if (existStudent != null) throw new DuplicateKeyException("Student already exist");

        log.info("[STUDENT] Try to create student with NIM: {}", student.getStudentId());
        return studentRepository.save(student);
    }

    @Override
    public Student findByStudentId(String studentId) {
        log.info("[STUDENT] Try to get student with NIM: {}", studentId);

        return studentRepository.findByStudentId(studentId)
                .orElse(null);
    }

    @Override
    public StudentResponse profile(Authentication authentication) {
        log.info("[STUDENT] Try to get student profile");

        try {
            EntityCredential currentUser = (EntityCredential) credentialService
                    .loadUserByUsername(authentication.getName());

            Student student = this.findByStudentId(currentUser.getUsername());
            if (student == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");

            FileResponse profilePicture = new FileResponse();

            if (student.getProfilePicture() == null) {
                profilePicture.setId(null);
                profilePicture.setFilename(null);
                profilePicture.setUrl(StringUtils.defaultImage(student.getGender(), student.getFullName()));
            } else {
                profilePicture.setId(student.getProfilePicture().getId());
                profilePicture.setFilename(student.getProfilePicture().getName());
                profilePicture.setUrl(student.getProfilePicture().getPath());
            }

            return StudentResponse.builder()
                    .fullName(student.getFullName())
                    .studentId(student.getStudentId())
                    .campus(student.getCampus())
                    .major(student.getMajor())
                    .status(student.getStatus())
                    .gender(student.getGender())
                    .level(student.getLevel())
                    .joinDate(student.getJoinDate())
                    .profilePicture(profilePicture)
                    .build();
        } catch (Exception e) {
            log.error("[STUDENT] Error while getting student profile");

            throw e;
        }
    }

    @Override
    public StudentResponse updateProfilePicture(Authentication authentication, MultipartFile multipartFile) {
        log.info("[STUDENT] Try to update student profile picture");

        String credentialName = authentication.getName();
        EntityCredential credential = (EntityCredential) credentialService.loadUserByUsername(credentialName);
        Student student = studentRepository.findByStudentId(credential.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        if(student.getProfilePicture() != null) {
            profilePictureService.deleteById(student.getProfilePicture().getId());

            student.setProfilePicture(null);

            studentRepository.save(student);
        }

        ProfilePicture newPicture = profilePictureService.update(student, multipartFile);

        FileResponse fileResponse = FileResponse.builder()
                .id(newPicture.getId())
                .filename(newPicture.getName())
                .url(newPicture.getPath())
                .build();

        student.setProfilePicture(newPicture);

        Student savedStudent = studentRepository.save(student);

        return StudentResponse.builder()
                .fullName(savedStudent.getFullName())
                .studentId(savedStudent.getStudentId())
                .campus(savedStudent.getCampus())
                .major(savedStudent.getMajor())
                .status(savedStudent.getStatus())
                .gender(savedStudent.getGender())
                .level(savedStudent.getLevel())
                .joinDate(savedStudent.getJoinDate())
                .profilePicture(fileResponse)
                .build();
    }

    @Override
    public Resource loadProfilePicture(String id) {
        log.info("[STUDENT] Try to load student profile picture");

        return profilePictureService.get(id);
    }
}
