package com.softawii.social.service;

import com.softawii.social.config.AppConfig;
import com.softawii.social.config.UploadStorageType;
import com.softawii.social.model.Image;
import com.softawii.social.repository.ImageRepository;
import com.softawii.social.util.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Optional;
import java.util.UUID;

@Component
public class ImageService {

    private final AppConfig       appConfig;
    private final ImageRepository repository;
    private final FileUploadUtil fileUploadUtil;

    public ImageService(AppConfig appConfig, ImageRepository repository, FileUploadUtil fileUploadUtil) {
        this.appConfig = appConfig;
        this.repository = repository;
        this.fileUploadUtil = fileUploadUtil;
    }

    public Optional<Image> findById(Long id) {
        return repository.findById(id);
    }

    public Page<Image> findAll() {
        return repository.findAll(Pageable.unpaged());
    }

    public Page<Image> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    // TODO: COMPRESS IMAGE WITH ImageMagick
    public Image create(byte[] blob) throws IOException {
        Image image = new Image();
        if (appConfig.getUploadStorageType().equals(UploadStorageType.DATABASE)) {
            image.setBlob(blob);
        } else if (appConfig.getUploadStorageType().equals(UploadStorageType.S3)) {
//            image.setS3(s3);
        } else if (appConfig.getUploadStorageType().equals(UploadStorageType.LOCAL)) {
            String filename = UUID.randomUUID().toString();
            fileUploadUtil.saveFile(filename, blob);
            image.setLocal(filename);
        }

        return repository.save(image);
    }

    public byte[] readImageFile(Image image) throws NoSuchFileException, IOException {
        if (image.getLocal() == null) {
            // TODO: FIND BETTER EXCEPTION
            throw new RuntimeException();
        }

        return fileUploadUtil.readFile(image.getLocal());
    }
}
