package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.models.Plan;
import com.example.submeapp.api.models.PurchaseRequest;
import com.example.submeapp.api.models.PurchaseResponse;
import com.example.submeapp.api.models.TokenPayload;
import com.example.submeapp.utils.TokenManager;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView productImage;
    private TextView txtProductName;
    private TextView txtDescription;
    private TextView txtRating;
    private TextView txtReviews;
    private TextView txtSubscribers;
    private TextView txtPrice;
    private Button btnSubscribe;
    private ProgressBar progressBar;
    private RadioGroup radioGroupPayment;
    private RadioButton radioVNPay;
    private RadioButton radioMoMo;

    private Plan currentPlan;
    private String selectedPaymentMethod = "VNPay"; // Default
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize TokenManager
        tokenManager = new TokenManager(this);

        btnBack = findViewById(R.id.btnBack);
        productImage = findViewById(R.id.productImage);
        txtProductName = findViewById(R.id.txtProductName);
        txtDescription = findViewById(R.id.txtDescription);
        txtRating = findViewById(R.id.txtRating);
        txtReviews = findViewById(R.id.txtReviews);
        txtSubscribers = findViewById(R.id.txtSubscribers);
        txtPrice = findViewById(R.id.txtPrice);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        progressBar = findViewById(R.id.progressBar);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioVNPay = findViewById(R.id.radioVNPay);
        radioMoMo = findViewById(R.id.radioMoMo);

        btnBack.setOnClickListener(v -> finish());

        // Xử lý chọn phương thức thanh toán
        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioVNPay) {
                selectedPaymentMethod = "VNPay";
            } else if (checkedId == R.id.radioMoMo) {
                selectedPaymentMethod = "MoMo";
            }
        });

        // Lấy planId từ Intent
        int planId = getIntent().getIntExtra("planId", -1);

        if (planId != -1) {
            // Gọi API lấy chi tiết plan
            loadPlanDetail(planId);
        } else {
            // Fallback: Hiển thị dữ liệu từ Intent (nếu không có planId)
            loadDataFromIntent();
        }

        btnSubscribe.setOnClickListener(v -> {
            if (currentPlan != null) {
                // Gọi API purchase
                processPurchase();
            } else {
                Toast.makeText(this, "Đang tải thông tin...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlanDetail(int planId) {
        // Hiển thị loading
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        btnSubscribe.setEnabled(false);

        // Gọi API - response là List<Plan>
        RetrofitClient.getApiService().getPlanDetail(planId).enqueue(new Callback<java.util.List<Plan>>() {
            @Override
            public void onResponse(Call<java.util.List<Plan>> call, Response<java.util.List<Plan>> response) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                btnSubscribe.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lấy phần tử đầu tiên trong array
                    currentPlan = response.body().get(0);
                    displayPlanDetail(currentPlan);
                } else {
                    Toast.makeText(ProductDetailActivity.this,
                        "Không thể tải thông tin gói",
                        Toast.LENGTH_SHORT).show();
                    // Fallback: Load từ Intent
                    loadDataFromIntent();
                }
            }

            @Override
            public void onFailure(Call<java.util.List<Plan>> call, Throwable t) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                btnSubscribe.setEnabled(true);

                Toast.makeText(ProductDetailActivity.this,
                    "Lỗi kết nối: " + t.getMessage(),
                    Toast.LENGTH_SHORT).show();

                // Fallback: Load từ Intent
                loadDataFromIntent();
            }
        });
    }

    private void displayPlanDetail(Plan plan) {
        txtProductName.setText(plan.getName());
        txtDescription.setText(plan.getDescription());

        // Hiển thị giá với thời hạn
        String priceText = plan.getFormattedPrice() + "/" + plan.getDurationUnit();
        txtPrice.setText(priceText);

        // Load image from API using Glide
        if (plan.getImageUrl() != null && !plan.getImageUrl().isEmpty()) {
            com.bumptech.glide.Glide.with(this)
                .load(plan.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .centerCrop()
                .into(productImage);
        }

        // Có thể thêm các thông tin khác nếu cần
        // txtRating, txtReviews, txtSubscribers...
    }

    private void loadDataFromIntent() {
        // Fallback: Lấy dữ liệu từ Intent (dùng khi không có API hoặc lỗi)
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        if (title != null) {
            txtProductName.setText(title);
        }
        if (price != null) {
            txtPrice.setText(price);
        }
        if (description != null) {
            txtDescription.setText(description);
        }

        // Load image using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            com.bumptech.glide.Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .centerCrop()
                .into(productImage);
        }
    }

    private void processPurchase() {
        // Lấy userId từ token
        int userId = getUserIdFromToken();

        // Debug log
        android.util.Log.d("ProductDetail", "UserId from token: " + userId);
        android.util.Log.d("ProductDetail", "Selected payment method: " + selectedPaymentMethod);

        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            // Navigate back to login
            Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Hiển thị loading
        progressBar.setVisibility(View.VISIBLE);
        btnSubscribe.setEnabled(false);

        // Tạo request
        PurchaseRequest request = new PurchaseRequest(userId, currentPlan.getId(), selectedPaymentMethod);

        // Lấy token từ TokenManager
        String bearerToken = tokenManager.getBearerToken();

        android.util.Log.d("ProductDetail", "Sending purchase request - PlanId: " + currentPlan.getId() + ", UserId: " + userId + ", Method: " + selectedPaymentMethod);

        // Gọi API purchase
        RetrofitClient.getApiService().purchaseSubscription(bearerToken, request)
            .enqueue(new Callback<PurchaseResponse>() {
                @Override
                public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    btnSubscribe.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        PurchaseResponse result = response.body();

                        if (result.isSuccess()) {
                            // Thanh toán thành công
                            if (result.getData() != null && result.getData().getSubscription() != null
                                && result.getData().getPayment() != null && result.getData().getPlan() != null) {

                                Intent intent = new Intent(ProductDetailActivity.this, PaymentSuccessActivity.class);
                                intent.putExtra("packageName", result.getData().getPlan().getName());
                                intent.putExtra("amount", result.getData().getPlan().getPrice());
                                intent.putExtra("transactionId", result.getData().getPayment().getTransactionId());
                                intent.putExtra("startDate", result.getData().getSubscription().getStartDate());
                                intent.putExtra("endDate", result.getData().getSubscription().getEndDate());
                                intent.putExtra("message", result.getMessage());
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ProductDetailActivity.this,
                                    result.getMessage() != null ? result.getMessage() : "Thanh toán thành công!",
                                    Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Xử lý lỗi từ API (success = false)
                            handleErrorResponse(result);
                        }
                    } else {
                        // HTTP error (4xx, 5xx) - Parse errorBody
                        handleHttpError(response);
                    }
                }

                @Override
                public void onFailure(Call<PurchaseResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    btnSubscribe.setEnabled(true);
                    android.util.Log.e("ProductDetail", "Purchase error: " + t.getMessage(), t);
                    Toast.makeText(ProductDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void handleErrorResponse(PurchaseResponse result) {
        String errorMessage = result.getMessage() != null ? result.getMessage() : "Thanh toán thất bại";

        // Nếu là lỗi ALREADY_SUBSCRIBED, hiển thị thông tin chi tiết
        if ("ALREADY_SUBSCRIBED".equals(result.getError()) && result.getData() != null) {
            PurchaseResponse.Data data = result.getData();
            if (data.getExpiresInDays() != null && data.getEndDate() != null) {
                errorMessage = errorMessage + "\n\n" +
                    "📅 Ngày hết hạn: " + data.getEndDate() + "\n" +
                    "⏰ Còn lại: " + data.getExpiresInDays() + " ngày";
            }
        }

        android.util.Log.w("ProductDetail", "Purchase failed - Error: " + result.getError() + ", Message: " + errorMessage);
        Toast.makeText(ProductDetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void handleHttpError(Response<PurchaseResponse> response) {
        String errorMessage = "Lỗi: " + response.message();

        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();
                android.util.Log.e("ProductDetail", "HTTP error body: " + errorJson);

                // Parse error response
                Gson gson = new Gson();
                PurchaseResponse errorResponse = gson.fromJson(errorJson, PurchaseResponse.class);

                if (errorResponse != null) {
                    // Sử dụng message từ API nếu có
                    if (errorResponse.getMessage() != null) {
                        errorMessage = errorResponse.getMessage();

                        // Nếu là lỗi ALREADY_SUBSCRIBED, hiển thị thông tin chi tiết
                        if ("ALREADY_SUBSCRIBED".equals(errorResponse.getError()) && errorResponse.getData() != null) {
                            PurchaseResponse.Data data = errorResponse.getData();
                            if (data.getExpiresInDays() != null && data.getEndDate() != null) {
                                errorMessage = errorMessage + "\n\n" +
                                    "📅 Ngày hết hạn: " + data.getEndDate() + "\n" +
                                    "⏰ Còn lại: " + data.getExpiresInDays() + " ngày";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            android.util.Log.e("ProductDetail", "Error parsing error response: " + e.getMessage(), e);
        }

        android.util.Log.e("ProductDetail", "HTTP error: " + response.code() + " - " + errorMessage);
        Toast.makeText(ProductDetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private int getUserIdFromToken() {
        try {
            // Sử dụng TokenManager để lấy thông tin user
            TokenPayload userInfo = tokenManager.getUserInfo();

            android.util.Log.d("ProductDetail", "Token exists: " + (tokenManager.getToken() != null));

            if (userInfo == null) {
                android.util.Log.e("ProductDetail", "Cannot decode token or token is null!");
                return -1;
            }

            android.util.Log.d("ProductDetail", "Token payload - Email: " + userInfo.getEmail() + ", Role: " + userInfo.getRole());
            android.util.Log.d("ProductDetail", "Extracted userId (sub): " + userInfo.getSub());

            return userInfo.getSub();
        } catch (Exception e) {
            android.util.Log.e("ProductDetail", "Error getting userId from token: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return -1;
    }
}
