package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submeapp.adapters.NotificationAdapter;
import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.models.Notification;
import com.example.submeapp.api.models.NotificationResponse;
import com.example.submeapp.api.models.TokenPayload;
import com.example.submeapp.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerViewNotifications;
    private LinearLayout emptyStateLayout;
    private ProgressBar progressBar;
    private TextView txtUnreadCount;
    private NotificationAdapter adapter;
    private TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize views
        recyclerViewNotifications = view.findViewById(R.id.recyclerViewNotifications);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        progressBar = view.findViewById(R.id.progressBar);
        txtUnreadCount = view.findViewById(R.id.txtUnreadCount);

        // Initialize TokenManager
        tokenManager = new TokenManager(requireContext());

        // Setup RecyclerView
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(requireContext(), new ArrayList<>());
        recyclerViewNotifications.setAdapter(adapter);

        // Load notifications
        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        // Get userId from token
        TokenPayload userInfo = tokenManager.getUserInfo();
        if (userInfo == null) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }

        int userId = userInfo.getSub();
        String bearerToken = tokenManager.getBearerToken();

        // Show loading
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        recyclerViewNotifications.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);

        // Call API
        RetrofitClient.getApiService().getUserNotifications(bearerToken, userId)
            .enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        NotificationResponse result = response.body();

                        if (result.isSuccess() && result.getData() != null) {
                            List<Notification> notifications = result.getData();

                            android.util.Log.d("Notifications", "Loaded " + result.getTotal() + " notifications, " + result.getUnreadCount() + " unread");

                            // Update unread count
                            if (txtUnreadCount != null && result.getUnreadCount() > 0) {
                                txtUnreadCount.setText(String.valueOf(result.getUnreadCount()));
                                txtUnreadCount.setVisibility(View.VISIBLE);
                            } else if (txtUnreadCount != null) {
                                txtUnreadCount.setVisibility(View.GONE);
                            }

                            if (notifications.isEmpty()) {
                                // Show empty state
                                emptyStateLayout.setVisibility(View.VISIBLE);
                                recyclerViewNotifications.setVisibility(View.GONE);
                            } else {
                                // Show notifications
                                adapter.updateData(notifications);
                                recyclerViewNotifications.setVisibility(View.VISIBLE);
                                emptyStateLayout.setVisibility(View.GONE);
                            }
                        } else {
                            showEmptyState();
                            if (result.getMessage() != null) {
                                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        showEmptyState();
                        Toast.makeText(getContext(),
                            "Không thể tải thông báo: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    showEmptyState();

                    android.util.Log.e("Notifications", "API error: " + t.getMessage(), t);
                    Toast.makeText(getContext(),
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void showEmptyState() {
        emptyStateLayout.setVisibility(View.VISIBLE);
        recyclerViewNotifications.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload notifications when fragment becomes visible
        loadNotifications();
    }
}

