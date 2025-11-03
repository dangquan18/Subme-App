package com.example.submeapp.api.models;

import com.google.gson.annotations.SerializedName;

public class Plan {
    private int id;

    @SerializedName("vendor_id")
    private int vendorId;

    @SerializedName("category_id")
    private int categoryId;

    private String name;
    private String description;
    private String price;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("duration_unit")
    private String durationUnit;

    @SerializedName("duration_value")
    private int durationValue;

    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public int getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(int durationValue) {
        this.durationValue = durationValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to format price
    public String getFormattedPrice() {
        try {
            double priceValue = Double.parseDouble(price);
            return String.format("%,.0f₫", priceValue);
        } catch (NumberFormatException e) {
            return price + "₫";
        }
    }

    // Helper method to get duration text
    public String getDurationText() {
        return durationValue + " " + durationUnit;
    }

    // Helper method to check if approved
    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }
}

