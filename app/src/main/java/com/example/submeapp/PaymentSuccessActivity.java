package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentSuccessActivity extends AppCompatActivity {

    private TextView txtSuccessMessage;
    private TextView txtPackageName;
    private TextView txtAmount;
    private TextView txtTransactionId;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private Button btnGoToMyPackages;
    private Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // Initialize views
        txtSuccessMessage = findViewById(R.id.txtSuccessMessage);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtAmount = findViewById(R.id.txtAmount);
        txtTransactionId = findViewById(R.id.txtTransactionId);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        btnGoToMyPackages = findViewById(R.id.btnGoToMyPackages);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        // Get data from Intent
        String packageName = getIntent().getStringExtra("packageName");
        String amount = getIntent().getStringExtra("amount");
        String transactionId = getIntent().getStringExtra("transactionId");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        String message = getIntent().getStringExtra("message");

        // Display data
        if (message != null) {
            txtSuccessMessage.setText(message);
        }

        if (packageName != null) {
            txtPackageName.setText(packageName);
        }

        if (amount != null) {
            txtAmount.setText(formatCurrency(amount) + " VNĐ");
        }

        if (transactionId != null) {
            txtTransactionId.setText(transactionId);
        }

        if (startDate != null) {
            txtStartDate.setText(formatDateTime(startDate));
        }

        if (endDate != null) {
            txtEndDate.setText(formatDateTime(endDate));
        }

        // Button listeners
        btnGoToMyPackages.setOnClickListener(v -> {
            // Navigate to MyPackagesActivity with MainActivity in back stack
            Intent mainIntent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);

            // Then immediately navigate to MyPackagesActivity
            Intent packagesIntent = new Intent(PaymentSuccessActivity.this, MyPackagesActivity.class);
            startActivity(packagesIntent);

            finish();
        });

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private String formatCurrency(String amount) {
        try {
            double value = Double.parseDouble(amount);
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            return formatter.format(value);
        } catch (NumberFormatException e) {
            return amount;
        }
    }

    private String formatDateTime(String dateTimeStr) {
        try {
            // Parse ISO 8601 date format
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = isoFormat.parse(dateTimeStr);

            if (date != null) {
                // Format to Vietnamese style
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                return displayFormat.format(date);
            }
            return dateTimeStr;
        } catch (Exception e) {
            // If parsing fails, try another format
            try {
                SimpleDateFormat isoFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                Date date = isoFormat2.parse(dateTimeStr);

                if (date != null) {
                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    return displayFormat.format(date);
                }
                return dateTimeStr;
            } catch (Exception e2) {
                return dateTimeStr;
            }
        }
    }
}

