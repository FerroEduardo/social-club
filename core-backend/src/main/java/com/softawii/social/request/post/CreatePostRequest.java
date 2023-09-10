package com.softawii.social.request.post;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public class CreatePostRequest {
    @Size(min = 1, max = 100, message = "The length of \"title\" must be between 1 and 100")
    private String title;

    @Size(min = 1, max = 200, message = "The length of \"description\" must be between 1 and 200")
    private String description;

    @PositiveOrZero(message = "\"gameId\" must be at least 0")
    @NotNull(message = "\"gameId\" is required")
    private Long gameId;

    @NotNull(message = "\"image\" is required")
    private MultipartFile image;

    public CreatePostRequest() {
    }

    @AssertTrue(message = "Invalid image content type")
    private boolean invalidImageContentType() {
        return Stream.of("image/png", "image/jpg", "image/jpeg", "image/webp")
                .noneMatch(contentType -> contentType.equals(image.getContentType()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
