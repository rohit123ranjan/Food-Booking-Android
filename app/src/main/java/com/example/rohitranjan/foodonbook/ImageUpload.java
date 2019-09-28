package com.example.rohitranjan.foodonbook;

public class ImageUpload {

    public String name;
    public String url;
    public String userId;

    public ImageUpload(String name, String url, String userId) {
        this.name = name;
        this.url = url;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ImageUpload(){}
}
