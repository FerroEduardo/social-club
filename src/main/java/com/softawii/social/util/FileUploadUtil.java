package com.softawii.social.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, byte[] blob) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

//         TODO BETTER DIRECTORY CHECKS
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }

        try (InputStream inputStream = new ByteArrayInputStream(blob)) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static byte[] readFile(String directory, String fileName) throws IOException {
        Path filePath = Path.of(directory).resolve(fileName);

        if (!Files.isRegularFile(filePath)) {
            throw new NoSuchFileException(filePath.toString());
        }

        return Files.readAllBytes(filePath);
    }
}