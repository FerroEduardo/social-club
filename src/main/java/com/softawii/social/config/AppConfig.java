package com.softawii.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${softawii.production}")
    private boolean isProduction;

    private String uploadFolder;

    private UploadStorageType uploadStorageType;

    public boolean isProduction() {
        return isProduction;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    @Autowired
    public void setUploadFolder(@Value("${softawii.upload.folder}") String uploadFolder) {
        if (uploadFolder == null) {
            this.uploadFolder = System.getProperty("java.io.tmpdir");
        } else {
            this.uploadFolder = uploadFolder;
        }
    }

    public UploadStorageType getUploadStorageType() {
        return uploadStorageType;
    }

    @Autowired
    public void setUploadStorageType(@Value("${softawii.upload.storage}") String uploadStorageType) {
        this.uploadStorageType = UploadStorageType.valueOf(uploadStorageType);
    }
}
