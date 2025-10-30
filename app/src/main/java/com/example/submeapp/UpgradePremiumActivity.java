package com.example.submeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpgradePremiumActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RadioGroup radioGroupPackage;
    private RadioGroup radioGroupPayment;
    private RadioButton radioMonthly;
    private RadioButton radioYearly;
    private TextView txtTotalAmount;
    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_premium);

        btnBack = findViewById(R.id.btnBack);
        radioGroupPackage = findViewById(R.id.radioGroupPackage);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioMonthly = findViewById(R.id.radioMonthly);
        radioYearly = findViewById(R.id.radioYearly);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        btnBack.setOnClickListener(v -> finish());

        // Update price when package selection changes
        radioGroupPackage.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioMonthly) {
                txtTotalAmount.setText("299.000 đ");
            } else if (checkedId == R.id.radioYearly) {
                txtTotalAmount.setText("2.590.000 đ");
            }
        });

        btnConfirmPayment.setOnClickListener(v -> {
            String packageType = radioMonthly.isChecked() ? "Gói tháng" : "Gói năm";
            int paymentMethodId = radioGroupPayment.getCheckedRadioButtonId();
            RadioButton selectedPayment = findViewById(paymentMethodId);
            String paymentMethod = selectedPayment.getText().toString();

            Toast.makeText(this,
                "Đang xử lý thanh toán " + packageType + " qua " + paymentMethod,
                Toast.LENGTH_LONG).show();
        });
    }
}

