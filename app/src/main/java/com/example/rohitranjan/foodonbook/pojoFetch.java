package com.example.rohitranjan.foodonbook;

import java.util.List;

public class pojoFetch {
    private String uploadId,username,number,totalAmount,dataTable,rating;
    List<String> arrayItemName,arrayPrice,arrayQuantity;

    public pojoFetch(String uploadId, String username, String number, String totalAmount, String dataTable, String rating, List<String> arrayItemName, List<String> arrayPrice, List<String> arrayQuantity) {
        this.uploadId = uploadId;
        this.username = username;
        this.number = number;
        this.totalAmount = totalAmount;
        this.dataTable = dataTable;
        this.rating = rating;
        this.arrayItemName = arrayItemName;
        this.arrayPrice = arrayPrice;
        this.arrayQuantity = arrayQuantity;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMyid() {
        return uploadId;
    }

    public void setMyid(String myid) {
        this.uploadId = myid;
    }

    public pojoFetch() {
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<String> getArrayItemName() {
        return arrayItemName;
    }

    public void setArrayItemName(List<String> arrayItemName) {
        this.arrayItemName = arrayItemName;
    }

    public List<String> getArrayPrice() {
        return arrayPrice;
    }

    public void setArrayPrice(List<String> arrayPrice) {
        this.arrayPrice = arrayPrice;
    }

    public List<String> getArrayQuantity() {
        return arrayQuantity;
    }

    public void setArrayQuantity(List<String> arrayQuantity) {
        this.arrayQuantity = arrayQuantity;
    }
}
