package com.service.authorization.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.service.authorization.entity.File;
import com.service.authorization.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    @Autowired
    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    //It will create new file without remove or delete the old file, always extending files (not replacing)
    @Override
    public File create(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File can not be empty!");

        if (isSupportedContentType(multipartFile.getContentType()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid Content Type!");

        try {
            String filename = String.format("%d_%s", System.currentTimeMillis(), multipartFile.getOriginalFilename());
            String folderPath = "profiles/" + filename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            amazonS3.putObject(bucket, folderPath, multipartFile.getInputStream(), metadata);
            String directoryPath = amazonS3.getUrl(bucket, folderPath).toString();

            return File.builder()
                    .name(filename)
                    .path(directoryPath)
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .build();
        } catch (IOException | RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "a failure occurred on the server");
        }
    }

    @Override
    public Resource get(String path) {
        Path filePath = Paths.get(path);
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "a failure occurred on the server");
        }
    }

    @Override
    public void delete(String path) {
        try {
            Path filePath = Paths.get(path);
            log.info("Deleting file: {}", filePath);

            boolean deleted = Files.deleteIfExists(filePath);

            if (deleted) {
                log.info("File deleted: {}", filePath);
            } else {
                log.warn("File not found: {}", filePath);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file");
        } catch (RuntimeException e) {
            log.error("Internal error: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private Boolean isSupportedContentType(String contentType) {
        return !List.of(
                        "application/pdf",
                        "image/jpg", "image/png", "image/jpeg", "image/gif", "video/mp4",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "text/csv", "text/plain", "audio/mp4")
                .contains(contentType);
    }

}
