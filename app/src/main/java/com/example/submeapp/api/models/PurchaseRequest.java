package com.example.submeapp.api.models;

import com.google.gson.annotations.SerializedName;

public class PurchaseRequest {
    @SerializedName("user_id")
    private int userId;

    @SerializedName("plan_id")
    private int planId;

    @SerializedName("payment_method")
    private String paymentMethod;

    public PurchaseRequest(int userId, int planId, String paymentMethod) {
        this.userId = userId;
        this.planId = planId;
        this.paymentMethod = paymentMethod;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

