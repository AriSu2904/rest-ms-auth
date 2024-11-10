package com.service.authorization.service;

import com.service.authorization.entity.ProfilePicture;
import com.service.authorization.entity.Student;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureService {
    ProfilePicture update(Student student, MultipartFile multipartFile);
    Resource get(String id);
    void deleteById(String id);
}
