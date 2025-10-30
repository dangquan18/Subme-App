package com.example.submeapp;

public class ListItem {
    private String title;
    private String price;
    private boolean isChecked;

    public ListItem(String title, String price) {
        this.title = title;
        this.price = price;
        this.isChecked = false;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

