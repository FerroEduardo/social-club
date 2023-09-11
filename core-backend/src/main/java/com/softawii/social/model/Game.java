package com.softawii.social.model;

public class Game {
    private Long   id;
    private String name;
    private String studio;
    private String imageUrl;

    public Game() {
    }

    public Game(String name, String studio, String imageUrl) {
        this.name = name;
        this.studio = studio;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studio='" + studio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
