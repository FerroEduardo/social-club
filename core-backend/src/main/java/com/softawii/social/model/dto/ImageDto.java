package com.softawii.social.model.dto;

import java.io.InputStream;

public class ImageDto {
    private InputStream stream;
    private String      extension;

    public ImageDto() {
    }

    public ImageDto(InputStream stream, String extension) {
        this.stream = stream;
        this.extension = extension;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
