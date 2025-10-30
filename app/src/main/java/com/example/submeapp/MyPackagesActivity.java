package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MyPackagesActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnDetails1;
    private Button btnPause1;
    private Button btnDetails2;
    private Button btnPause2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_packages);

        btnBack = findViewById(R.id.btnBack);
        btnDetails1 = findViewById(R.id.btnDetails1);
        btnPause1 = findViewById(R.id.btnPause1);
        btnDetails2 = findViewById(R.id.btnDetails2);
        btnPause2 = findViewById(R.id.btnPause2);

        btnBack.setOnClickListener(v -> finish());

        btnDetails1.setOnClickListener(v -> {
            Intent intent = new Intent(MyPackagesActivity.this, PackageDetailActivity.class);
            intent.putExtra("packageName", "Cà phê sáng mỗi ngày");
            intent.putExtra("renewalDate", "29/11/2025");
            startActivity(intent);
        });

        btnPause1.setOnClickListener(v -> {
            Toast.makeText(this, "Tạm dừng gói Cà phê sáng mỗi ngày", Toast.LENGTH_SHORT).show();
        });

        btnDetails2.setOnClickListener(v -> {
            Intent intent = new Intent(MyPackagesActivity.this, PackageDetailActivity.class);
            intent.putExtra("packageName", "Cơm trưa mỗi ngày");
            intent.putExtra("renewalDate", "29/11/2025");
            startActivity(intent);
        });

        btnPause2.setOnClickListener(v -> {
            Toast.makeText(this, "Tạm dừng gói Cơm trưa mỗi ngày", Toast.LENGTH_SHORT).show();
        });
    }
}

