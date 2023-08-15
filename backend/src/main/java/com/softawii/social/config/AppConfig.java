package com.softawii.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;

@Component
public class AppConfig {
    @Value("${softawii.production}")
    private boolean isProduction;

    private Path uploadFolder;

    private UploadStorageType uploadStorageType;

    public boolean isProduction() {
        return isProduction;
    }

    public Path getUploadFolder() {
        return uploadFolder;
    }

    @Autowired
    public void setUploadFolder(@Value("${softawii.upload.folder}") String uploadFolder) {
        Path path;
        if (uploadFolder == null) {
            path = Path.of(System.getProperty("java.io.tmpdir"));
        } else {
            path = Path.of(uploadFolder);
        }
        if (!Files.exists(path) || !Files.isDirectory(path) || !Files.isWritable(path)) {
            throw new InvalidParameterException(String.format("Upload folder \"%s\" does not exists, is not a directory or is not writable", uploadFolder));
        }
        this.uploadFolder = path;
    }

    public UploadStorageType getUploadStorageType() {
        return uploadStorageType;
    }

    @Autowired
    public void setUploadStorageType(@Value("${softawii.upload.storage}") String uploadStorageType) {
        this.uploadStorageType = UploadStorageType.valueOf(uploadStorageType);
    }
}
