package com.service.authorization.service.Impl;
import com.service.authorization.entity.File;
import com.service.authorization.entity.ProfilePicture;
import com.service.authorization.entity.Student;
import com.service.authorization.repository.ProfilePictureRepository;
import com.service.authorization.service.FileService;
import com.service.authorization.service.ProfilePictureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ProfilePictureServiceImpl implements ProfilePictureService {

    private final ProfilePictureRepository profilePictureRepository;
    private final FileService fileService;

    @Override
    public ProfilePicture update(Student student, MultipartFile multipartFile) {
        try {
            ProfilePicture picture = ProfilePicture.builder()
                    .student(student)
                    .build();

            File baseFile = fileService.create(multipartFile);
            picture.setName(baseFile.getName());
            picture.setSize(baseFile.getSize());
            picture.setContentType(baseFile.getContentType());
            picture.setPath(baseFile.getPath());


            profilePictureRepository.save(picture);

            return picture;
        }catch (Exception e){
            log.warn("ERROR: CREATING PROFILE PICTURE : {}" , e.getMessage(), e.getCause());

            throw e;
        }
    }

    @Override
    public Resource get(String id) {
        ProfilePicture profilePicture = profilePictureRepository
                .findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image Not Found!"));
        return fileService.get(profilePicture.getPath());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteById(String id) {
        ProfilePicture profilePicture = profilePictureRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!Objects.equals(authentication.getName(), profilePicture.getStudent().getStudentId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this image!");
        }

        String path = profilePicture.getPath();
        fileService.delete(path);
        profilePictureRepository.delete(profilePicture);
    }
}
