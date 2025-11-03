package com.example.submeapp.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserSubscriptionsResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<UserSubscription> data;

    @SerializedName("total")
    private int total;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<UserSubscription> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }
}

