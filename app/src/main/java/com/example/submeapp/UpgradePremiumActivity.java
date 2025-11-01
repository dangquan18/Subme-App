package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpgradePremiumActivity extends AppCompatActivity {

    private RadioButton radioMonthly;
    private RadioGroup radioGroupPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        ImageView btnBack = findViewById(R.id.btnBack);
        radioGroupPackage = findViewById(R.id.radioGroupPackage);
        radioMonthly = findViewById(R.id.radioMonthly);
        RadioButton radioYearly = findViewById(R.id.radioYearly);
        Button btnSubscribe = findViewById(R.id.btnSubscribe);

        btnBack.setOnClickListener(v -> finish());

        btnSubscribe.setOnClickListener(v -> {
            String packageType;
            String price;

            if (radioMonthly.isChecked()) {
                packageType = "Gói Premium 1 tháng";
                price = "99.000đ";
            } else if (radioYearly.isChecked()) {
                packageType = "Gói Premium 1 năm";
                price = "699.000đ";
            } else {
                Toast.makeText(this, "Vui lòng chọn gói đăng ký", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to payment screen
            Intent intent = new Intent(UpgradePremiumActivity.this, PaymentActivity.class);
            intent.putExtra("packageName", packageType);
            intent.putExtra("price", price);
            startActivity(intent);
        });
    }
}

