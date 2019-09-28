package com.example.rohitranjan.foodonbook;

public class PojoCart {
    private String itemId;
    private String newId;
    private String itemName;
    private String imageUrl;
    private String itemInfo;
    private String itemPrice;
    private String storeQuantity;
    private String storeTotal;

    public PojoCart(String itemId,String newId, String itemName, String imageUrl, String itemInfo, String itemPrice, String storeQuantity, String storeTotal) {
        this.itemId = itemId;
        this.newId = newId;
        this.itemName = itemName;
        this.imageUrl = imageUrl;
        this.itemInfo = itemInfo;
        this.itemPrice = itemPrice;
        this.storeQuantity = storeQuantity;
        this.storeTotal = storeTotal;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public PojoCart() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getStoreQuantity() {
        return storeQuantity;
    }

    public void setStoreQuantity(String storeQuantity) {
        this.storeQuantity = storeQuantity;
    }

    public String getStoreTotal() {
        return storeTotal;
    }

    public void setStoreTotal(String storeTotal) {
        this.storeTotal = storeTotal;
    }
}
