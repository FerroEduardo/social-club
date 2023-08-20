package com.softawii.social.util;

import com.softawii.social.config.AppConfig;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploadUtil {

    private final Path uploadFolder;

    public FileUploadUtil(AppConfig appConfig) {
        this.uploadFolder = appConfig.getUploadFolder();
    }

    public void saveFile(String fileName, byte[] blob) throws IOException {
        try (InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(blob))) {
            Path filePath = this.uploadFolder.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public byte[] readFile(String fileName) throws IOException {
        Path filePath = this.uploadFolder.resolve(fileName);

        if (!Files.isRegularFile(filePath)) {
            throw new NoSuchFileException(filePath.toString());
        }

        return Files.readAllBytes(filePath);
    }
}