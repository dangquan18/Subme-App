package com.example.submeapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtPackageName;
    private TextView txtDuration;
    private RadioGroup radioGroupPayment;
    private RadioButton radioMomo;
    private RadioButton radioVNPay;
    private TextView txtTotalAmount;
    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnBack = findViewById(R.id.btnBack);
        txtPackageName = findViewById(R.id.txtPackageName);
        txtDuration = findViewById(R.id.txtDuration);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioMomo = findViewById(R.id.radioMomo);
        radioVNPay = findViewById(R.id.radioVNPay);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Get data from intent
        String packageName = getIntent().getStringExtra("packageName");
        String price = getIntent().getStringExtra("price");

        if (packageName != null) {
            txtPackageName.setText(packageName);
        }
        if (price != null) {
            txtTotalAmount.setText(price);
        }

        btnBack.setOnClickListener(v -> finish());

        btnConfirmPayment.setOnClickListener(v -> {
            int selectedId = radioGroupPayment.getCheckedRadioButtonId();
            RadioButton selectedPayment = findViewById(selectedId);
            String paymentMethod = selectedPayment.getText().toString();

            Toast.makeText(this,
                "Đang xử lý thanh toán " + txtTotalAmount.getText() + " qua " + paymentMethod,
                Toast.LENGTH_LONG).show();

            // TODO: Process payment
            // After successful payment, navigate back or show success screen
        });
    }
}

