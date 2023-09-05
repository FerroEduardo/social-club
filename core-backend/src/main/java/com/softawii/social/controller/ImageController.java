package com.softawii.social.controller;

import com.softawii.social.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public ResponseEntity<InputStreamResource> show(@PathVariable Long id) {
        try {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.IMAGE_PNG);
            responseHeaders.setCacheControl(CacheControl.maxAge(Duration.ofHours(1)));
            responseHeaders.setExpires(Instant.now().plus(1, ChronoUnit.HOURS));

            InputStream         stream              = service.getImageInputStreamById(id);
            InputStreamResource inputStreamResource = new InputStreamResource(stream);
            return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(inputStreamResource);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
