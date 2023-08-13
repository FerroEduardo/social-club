package com.softawii.social.controller;

import com.softawii.social.model.Image;
import com.softawii.social.model.dto.image.IndexImageDTO;
import com.softawii.social.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Image> index(@Valid IndexImageDTO dto) {
        if (dto.isPaginated()) {
            return this.service.findAll(dto.getPage().intValue(), dto.getSize().intValue());
        }

        return this.service.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            Image image = service.findById(id).orElseThrow();
            if (image.getS3() != null) {
                // redirect to S3 instead of return data
            } else {
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.IMAGE_PNG);

                byte[] blob;
                if (image.getBlob() != null) {
                    blob = image.getBlob();
                } else if (image.getLocal() != null) {
                    blob = service.readImageFile(image);
                } else {
                    // TODO: FIND BETTER EXCEPTION
                    throw new RuntimeException();
                }
                return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(blob);
            }

            return ResponseEntity.notFound().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (NoSuchFileException e) { // File not found
            return ResponseEntity.internalServerError().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
