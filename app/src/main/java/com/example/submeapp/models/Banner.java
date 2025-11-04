package com.example.submeapp.models;

public class Banner {
    private int imageRes;
    private String title;
    private String description;
    private String backgroundColor;

    public Banner(int imageRes, String title, String description, String backgroundColor) {
        this.imageRes = imageRes;
        this.title = title;
        this.description = description;
        this.backgroundColor = backgroundColor;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}

