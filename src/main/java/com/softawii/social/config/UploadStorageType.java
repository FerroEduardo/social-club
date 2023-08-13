package com.softawii.social.config;

public enum UploadStorageType {
    DATABASE("DATABASE"),
    S3("S3"),
    LOCAL("LOCAL");

    private final String name;

    UploadStorageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
