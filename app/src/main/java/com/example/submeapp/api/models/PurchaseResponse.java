package com.example.submeapp.api.models;

import com.google.gson.annotations.SerializedName;

public class PurchaseResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private String error;

    @SerializedName("data")
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public Data getData() {
        return data;
    }

    // Backward compatibility methods
    public Subscription getSubscription() {
        return data != null ? data.subscription : null;
    }

    public Payment getPayment() {
        return data != null ? data.payment : null;
    }

    public PlanInfo getPlan() {
        return data != null ? data.plan : null;
    }

    public static class Data {
        @SerializedName("subscription")
        private Subscription subscription;

        @SerializedName("payment")
        private Payment payment;

        @SerializedName("plan")
        private PlanInfo plan;

        // For error response (ALREADY_SUBSCRIBED)
        @SerializedName("subscription_id")
        private Integer subscriptionId;

        @SerializedName("plan_name")
        private String planName;

        @SerializedName("start_date")
        private String startDate;

        @SerializedName("end_date")
        private String endDate;

        @SerializedName("expires_in_days")
        private Integer expiresInDays;

        public Subscription getSubscription() {
            return subscription;
        }

        public Payment getPayment() {
            return payment;
        }

        public PlanInfo getPlan() {
            return plan;
        }

        public Integer getSubscriptionId() {
            return subscriptionId;
        }

        public String getPlanName() {
            return planName;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public Integer getExpiresInDays() {
            return expiresInDays;
        }
    }

    public static class Subscription {
        @SerializedName("id")
        private int id;

        @SerializedName("status")
        private String status;

        @SerializedName("start_date")
        private String startDate;

        @SerializedName("end_date")
        private String endDate;

        public int getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }
    }

    public static class Payment {
        @SerializedName("id")
        private int id;

        @SerializedName("amount")
        private String amount;

        @SerializedName("method")
        private String method;

        @SerializedName("status")
        private String status;

        @SerializedName("transaction_id")
        private String transactionId;

        public int getId() {
            return id;
        }

        public String getAmount() {
            return amount;
        }

        public String getMethod() {
            return method;
        }

        public String getStatus() {
            return status;
        }

        public String getTransactionId() {
            return transactionId;
        }
    }

    public static class PlanInfo {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("price")
        private String price;

        @SerializedName("vendor")
        private String vendor;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getVendor() {
            return vendor;
        }
    }
}

