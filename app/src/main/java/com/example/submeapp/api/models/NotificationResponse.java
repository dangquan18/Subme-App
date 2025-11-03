package com.example.submeapp.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotificationResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Notification> data;

    @SerializedName("total")
    private int total;

    @SerializedName("unread_count")
    private int unreadCount;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Notification> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}

