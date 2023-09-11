package com.softawii.social.model;

public class Image {
    private Long   id;
    private String s3;
    private String local;
    private byte[] blob;
    private String extension;

    public Image() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", s3='" + s3 + '\'' +
                ", local='" + local + '\'' +
                ", blob(length)=" + (blob == null ? null : blob.length) +
                ", extension='" + extension + '\'' +
                '}';
    }
}
