package com.softawii.social.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;

@Component
public class AppConfig {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private boolean           isProduction;
    private Path              uploadFolder;
    private UploadStorageType uploadStorageType;
    private URI               uploadServiceUrl;
    private String            imageUrlPrefix;

    @Autowired
    public void setProduction(@Value("${softawii.production}") boolean production) {
        isProduction = production;
        logger.info("Is production? {}", production);
    }

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
            InvalidParameterException exception = new InvalidParameterException(String.format("Upload folder \"%s\" does not exists, is not a directory or is not writable", uploadFolder));
            logger.error("Failed to set upload folder: {}", uploadFolder, exception);
            throw exception;
        }

        logger.info("Upload folder: {}", path);
        this.uploadFolder = path;
    }

    public UploadStorageType getUploadStorageType() {
        return uploadStorageType;
    }

    @Autowired
    public void setUploadStorageType(@Value("${softawii.upload.storage}") String uploadStorageType) {
        this.uploadStorageType = UploadStorageType.valueOf(uploadStorageType);
        logger.info("Upload storage type: {}", this.uploadStorageType.getName());
    }

    public URI getUploadServiceUrl() {
        return uploadServiceUrl;
    }

    @Autowired
    public void setUploadServiceUrl(@Value("${softawii.upload-service.url}") String uploadServiceUrl) {
        try {
            this.uploadServiceUrl = new URL(uploadServiceUrl).toURI();
            logger.info("Upload service URL: {}", this.uploadServiceUrl);
        } catch (IllegalArgumentException | MalformedURLException | URISyntaxException e) {
            IllegalStateException exception = new IllegalStateException("Invalid upload service url: '%s'".formatted(uploadServiceUrl), e);

            logger.error("Failed to set upload service URL: {}", this.uploadServiceUrl, exception);
            throw exception;
        }
    }

    @Autowired
    public void setImageUrlPrefix(@Value("${softawii.image-url-prefix}") String imageUrlPrefix) {
        this.imageUrlPrefix = imageUrlPrefix;
        logger.info("Image URL prefix: {}", imageUrlPrefix);
    }

    public String getImageUrlPrefix() {
        return this.imageUrlPrefix;
    }
}
