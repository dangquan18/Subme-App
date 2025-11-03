package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submeapp.adapters.SubscriptionAdapter;
import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.models.TokenPayload;
import com.example.submeapp.api.models.UserSubscription;
import com.example.submeapp.api.models.UserSubscriptionsResponse;
import com.example.submeapp.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPackagesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSubscriptions;
    private SubscriptionAdapter adapter;
    private ProgressBar progressBar;
    private View emptyView;
    private Button btnExplore;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_packages);

        // Initialize TokenManager
        tokenManager = new TokenManager(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        recyclerViewSubscriptions = findViewById(R.id.recyclerViewSubscriptions);
        progressBar = findViewById(R.id.progressBar);
        emptyView = findViewById(R.id.emptyView);
        btnExplore = findViewById(R.id.btnExplore);

        btnBack.setOnClickListener(v -> finish());

        // Explore button - navigate to main screen to browse packages
        btnExplore.setOnClickListener(v -> {
            Intent intent = new Intent(MyPackagesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Setup RecyclerView
        recyclerViewSubscriptions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubscriptionAdapter(this, new ArrayList<>());
        recyclerViewSubscriptions.setAdapter(adapter);

        // Load active subscriptions
        loadActiveSubscriptions();
    }

    private void loadActiveSubscriptions() {
        // Get userId from token
        TokenPayload userInfo = tokenManager.getUserInfo();
        if (userInfo == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        int userId = userInfo.getSub();
        String bearerToken = tokenManager.getBearerToken();

        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewSubscriptions.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);

        // Call API
        RetrofitClient.getApiService().getUserActiveSubscriptions(bearerToken, userId)
            .enqueue(new Callback<UserSubscriptionsResponse>() {
                @Override
                public void onResponse(Call<UserSubscriptionsResponse> call, Response<UserSubscriptionsResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        UserSubscriptionsResponse result = response.body();

                        if (result.isSuccess() && result.getData() != null) {
                            List<UserSubscription> subscriptions = result.getData();

                            android.util.Log.d("MyPackages", "Loaded " + result.getTotal() + " subscriptions");

                            if (subscriptions.isEmpty()) {
                                // Show empty view
                                emptyView.setVisibility(View.VISIBLE);
                                recyclerViewSubscriptions.setVisibility(View.GONE);
                            } else {
                                // Show data
                                adapter.updateData(subscriptions);
                                recyclerViewSubscriptions.setVisibility(View.VISIBLE);
                                emptyView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(MyPackagesActivity.this,
                                result.getMessage() != null ? result.getMessage() : "Không thể tải danh sách gói",
                                Toast.LENGTH_SHORT).show();
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(MyPackagesActivity.this,
                            "Không thể tải danh sách gói: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<UserSubscriptionsResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);

                    android.util.Log.e("MyPackages", "API error: " + t.getMessage(), t);
                    Toast.makeText(MyPackagesActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadActiveSubscriptions();
    }
}

