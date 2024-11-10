package com.service.authorization.service;

import com.service.authorization.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    File create(MultipartFile multipartFile);
    Resource get(String path);
    void delete(String path);
}
