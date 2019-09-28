package com.example.rohitranjan.foodonbook;

public class ItemUpload {
    public String name;
    public String url;
    public String userId;
    public String infoItem;
    public String priceItem;

    public ItemUpload(String name, String url, String userId, String infoItem, String priceItem) {
        this.name = name;
        this.url = url;
        this.userId = userId;
        this.infoItem = infoItem;
        this.priceItem = priceItem;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getUserId() {
        return userId;
    }

    public String getInfoItem() {
        return infoItem;
    }

    public String getPriceItem() {
        return priceItem;
    }

    public ItemUpload() {

    }
}
