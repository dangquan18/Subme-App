package com.example.submeapp.api.models;

import com.google.gson.annotations.SerializedName;

public class UserSubscription {
    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("plan_id")
    private int planId;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;

    @SerializedName("status")
    private String status;

    @SerializedName("plan")
    private PlanDetail plan;

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getPlanId() {
        return planId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public PlanDetail getPlan() {
        return plan;
    }

    // Nested classes
    public static class PlanDetail {
        @SerializedName("id")
        private int id;

        @SerializedName("vendor_id")
        private int vendorId;

        @SerializedName("category_id")
        private int categoryId;

        @SerializedName("name")
        private String name;

        @SerializedName("description")
        private String description;

        @SerializedName("price")
        private String price;

        @SerializedName("duration_unit")
        private String durationUnit;

        @SerializedName("duration_value")
        private int durationValue;

        @SerializedName("status")
        private String status;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("vendor")
        private Vendor vendor;

        @SerializedName("category")
        private Category category;

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getPrice() {
            return price;
        }

        public String getDurationUnit() {
            return durationUnit;
        }

        public int getDurationValue() {
            return durationValue;
        }

        public String getStatus() {
            return status;
        }

        public Vendor getVendor() {
            return vendor;
        }

        public Category getCategory() {
            return category;
        }

        // Format price for display
        public String getFormattedPrice() {
            try {
                double priceValue = Double.parseDouble(price);
                return String.format("%,.0f", priceValue);
            } catch (NumberFormatException e) {
                return price;
            }
        }
    }

    public static class Vendor {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;

        @SerializedName("phone")
        private String phone;

        @SerializedName("address")
        private String address;

        @SerializedName("description")
        private String description;

        @SerializedName("status")
        private String status;

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class Category {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("description")
        private String description;

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}

