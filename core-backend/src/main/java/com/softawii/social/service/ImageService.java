package com.softawii.social.service;

import com.softawii.social.config.AppConfig;
import com.softawii.social.exception.FailedToCreateImageException;
import com.softawii.social.model.Image;
import com.softawii.social.model.dto.request.image.ImageDto;
import com.softawii.social.repository.ImageRepository;
import com.softawii.social.util.FileUploadUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Component
public class ImageService {

    private final AppConfig       appConfig;
    private final ImageRepository repository;
    private final FileUploadUtil  fileUploadUtil;

    public ImageService(AppConfig appConfig, ImageRepository repository, FileUploadUtil fileUploadUtil) {
        this.appConfig = appConfig;
        this.repository = repository;
        this.fileUploadUtil = fileUploadUtil;
    }

    public Optional<Image> findById(Long id) {
        return repository.findById(id);
    }

    public ImageDto getImageInputStreamById(Long id) throws IOException {
        Image       image = repository.findById(id).orElseThrow();
        InputStream inputStream;
        if (image.getBlob() != null) {
            inputStream = new ByteArrayInputStream(image.getBlob());
        } else if (image.getLocal() != null) {
            inputStream = readImageFile(image);
        } else {
            // TODO: FIND BETTER EXCEPTION
            throw new RuntimeException();
        }

        return new ImageDto(inputStream, image.getExtension());
    }

    public Image create(byte[] blob) throws FailedToCreateImageException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ByteArrayResource contentsAsResource = new ByteArrayResource(blob) {
            @Override
            public String getFilename() {
                return "image";
            }
        };
        map.add("image", contentsAsResource);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(appConfig.getUploadServiceUrl(), request, Map.class);
            Map<String, Object> body           = responseEntity.getBody();

            if (!body.containsKey("id")) {
                throw new FailedToCreateImageException("Response does not have he image id");
            }

            Image image = new Image();
            image.setId(Long.parseLong(body.get("id").toString()));
            return image;
        } catch (ResourceAccessException e) {
            // critical/fatal
            throw new FailedToCreateImageException(e);
        } catch (RestClientException e) {
            throw new FailedToCreateImageException(e);
        }
    }

    public InputStream readImageFile(Image image) throws IOException {
        return fileUploadUtil.readFile(image.getLocal());
    }

    public String getImageUrlFromImageId(Long imageId) {
        return this.appConfig.getImageUrlPrefix() + imageId.toString();
    }
}
