package com.example.submeapp.api.models;

public class UpdateProfileRequest {
    private String name;
    private String phone;
    private String address;
    private String date_of_birth;

    public UpdateProfileRequest(String name, String phone, String address, String date_of_birth) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.date_of_birth = date_of_birth;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}

