package com.example.submeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.models.PurchaseRequest;
import com.example.submeapp.api.models.PurchaseResponse;
import com.example.submeapp.api.models.TokenPayload;
import com.example.submeapp.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtPackageName;
    private TextView txtDuration;
    private RadioGroup radioGroupPayment;
    private RadioButton radioMomo;
    private RadioButton radioVNPay;
    private TextView txtTotalAmount;
    private Button btnConfirmPayment;

    private TokenManager tokenManager;
    private ProgressDialog progressDialog;
    private int planId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tokenManager = new TokenManager(this);

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
        planId = getIntent().getIntExtra("planId", -1);

        if (packageName != null) {
            txtPackageName.setText(packageName);
        }
        if (price != null) {
            txtTotalAmount.setText(price);
        }

        btnBack.setOnClickListener(v -> finish());

        btnConfirmPayment.setOnClickListener(v -> {
            processPayment();
        });
    }

    private void processPayment() {
        // Kiểm tra đã chọn phương thức thanh toán chưa
        int selectedId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra đăng nhập
        if (!tokenManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Kiểm tra planId
        if (planId == -1) {
            Toast.makeText(this, "Thông tin gói không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedPayment = findViewById(selectedId);
        String paymentMethod = selectedPayment.getText().toString();

        // Lấy userId từ token
        TokenPayload tokenPayload = tokenManager.getUserInfo();
        if (tokenPayload == null) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = tokenPayload.getSub();

        // Hiển thị loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý thanh toán...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Tạo request
        PurchaseRequest request = new PurchaseRequest(userId, planId, paymentMethod);

        // Gọi API
        String token = tokenManager.getBearerToken();
        RetrofitClient.getApiService().purchaseSubscription(token, request)
            .enqueue(new Callback<PurchaseResponse>() {
                @Override
                public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        PurchaseResponse purchaseResponse = response.body();

                        if (purchaseResponse.isSuccess()) {
                            // Thanh toán thành công
                            showPaymentSuccess(purchaseResponse);
                        } else {
                            Toast.makeText(PaymentActivity.this,
                                "Thanh toán thất bại: " + purchaseResponse.getMessage(),
                                Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(PaymentActivity.this,
                            "Thanh toán thất bại. Vui lòng thử lại!",
                            Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PurchaseResponse> call, Throwable t) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(PaymentActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                }
            });
    }

    private void showPaymentSuccess(PurchaseResponse response) {
        // Chuyển đến màn hình thanh toán thành công
        Intent intent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
        intent.putExtra("packageName", response.getPlan().getName());
        intent.putExtra("amount", response.getPayment().getAmount());
        intent.putExtra("transactionId", response.getPayment().getTransactionId());
        intent.putExtra("startDate", response.getSubscription().getStartDate());
        intent.putExtra("endDate", response.getSubscription().getEndDate());
        intent.putExtra("message", response.getMessage());
        startActivity(intent);
        finish();
    }
}

