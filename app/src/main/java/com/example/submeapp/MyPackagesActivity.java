package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MyPackagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_packages);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnRenew = findViewById(R.id.btnRenew);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnBack.setOnClickListener(v -> finish());

        btnRenew.setOnClickListener(v -> {
            Intent intent = new Intent(MyPackagesActivity.this, PaymentActivity.class);
            intent.putExtra("packageName", "Cà phê sáng mỗi ngày");
            intent.putExtra("price", "299.000đ");
            startActivity(intent);
        });

        btnCancel.setOnClickListener(v ->
            Toast.makeText(this, "Hủy gói Cà phê sáng mỗi ngày", Toast.LENGTH_SHORT).show()
        );
    }
}

