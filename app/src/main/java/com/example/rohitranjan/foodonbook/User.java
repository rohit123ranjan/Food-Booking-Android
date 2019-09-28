package com.example.rohitranjan.foodonbook;

public class User {
    String username, email, phoneNumber, userId;

    public User(){

    }

    public User(String username, String email, String phoneNumber, String userId) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
