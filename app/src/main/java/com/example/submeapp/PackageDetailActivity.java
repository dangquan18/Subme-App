package com.example.submeapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PackageDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtPackageTitle;
    private TextView txtRenewalDate;
    private TextView txtPaymentMethod;
    private TextView txtPaymentCycle;
    private LinearLayout btnReportIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        btnBack = findViewById(R.id.btnBack);
        txtPackageTitle = findViewById(R.id.txtPackageTitle);
        txtRenewalDate = findViewById(R.id.txtRenewalDate);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);
        txtPaymentCycle = findViewById(R.id.txtPaymentCycle);
        btnReportIssue = findViewById(R.id.btnReportIssue);

        // Get data from intent
        String packageName = getIntent().getStringExtra("packageName");
        String renewalDate = getIntent().getStringExtra("renewalDate");

        if (packageName != null) {
            txtPackageTitle.setText(packageName);
        }
        if (renewalDate != null) {
            txtRenewalDate.setText(renewalDate);
        }

        btnBack.setOnClickListener(v -> finish());

        btnReportIssue.setOnClickListener(v -> {
            Toast.makeText(this, "Mở form báo vấn đề", Toast.LENGTH_SHORT).show();
            // TODO: Open report issue form
        });
    }
}

