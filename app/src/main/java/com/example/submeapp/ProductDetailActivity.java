package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtProductName;
    private TextView txtDescription;
    private TextView txtRating;
    private TextView txtReviews;
    private TextView txtSubscribers;
    private TextView txtPrice;
    private Button btnSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        btnBack = findViewById(R.id.btnBack);
        txtProductName = findViewById(R.id.txtProductName);
        txtDescription = findViewById(R.id.txtDescription);
        txtRating = findViewById(R.id.txtRating);
        txtReviews = findViewById(R.id.txtReviews);
        txtSubscribers = findViewById(R.id.txtSubscribers);
        txtPrice = findViewById(R.id.txtPrice);
        btnSubscribe = findViewById(R.id.btnSubscribe);

        // Get data from intent
        String productName = getIntent().getStringExtra("productName");
        String productPrice = getIntent().getStringExtra("productPrice");

        if (productName != null) {
            txtProductName.setText(productName);
        }
        if (productPrice != null) {
            txtPrice.setText(productPrice);
        }

        btnBack.setOnClickListener(v -> finish());

        btnSubscribe.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, PaymentActivity.class);
            intent.putExtra("packageName", txtProductName.getText().toString());
            intent.putExtra("price", txtPrice.getText().toString());
            startActivity(intent);
        });
    }
}

