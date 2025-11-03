package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.models.Plan;
import com.example.submeapp.api.models.ProfileResponse;
import com.example.submeapp.utils.TokenManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private TextView tvUserName, tvTotalPackages, tvMonthlySaving, tvDaysLeft;
    private ImageView btnNotifications;
    private LinearLayout btnQuickMyPackages, btnQuickPremium, btnQuickHistory, btnQuickSupport;
    private TextView btnSearch, btnViewAllCategories, btnViewAllFeatured, btnViewAllPopular;

    private ViewPager2 bannerViewPager;
    private RecyclerView recyclerCategories, recyclerFeaturedPlans, recyclerPopularPlans;

    private PlanAdapter featuredAdapter, popularAdapter;
    private List<Plan> featuredList, popularList;

    private TokenManager tokenManager;
    private String token;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize TokenManager
        tokenManager = new TokenManager(requireContext());
        token = tokenManager.getToken();
        if (token != null) {
            userId = getUserIdFromToken(token);
        }

        // Initialize views
        initViews(view);

        // Setup listeners
        setupListeners();

        // Load data
        loadUserProfile();
        loadPlans();
        setupBanner();
        setupStatistics();

        return view;
    }

    private void initViews(View view) {
        // Header views
        tvUserName = view.findViewById(R.id.tvUserName);
        btnNotifications = view.findViewById(R.id.btnNotifications);
        btnSearch = view.findViewById(R.id.btnSearch);

        // Quick actions
        btnQuickMyPackages = view.findViewById(R.id.btnQuickMyPackages);
        btnQuickPremium = view.findViewById(R.id.btnQuickPremium);
        btnQuickHistory = view.findViewById(R.id.btnQuickHistory);
        btnQuickSupport = view.findViewById(R.id.btnQuickSupport);

        // Statistics
        tvTotalPackages = view.findViewById(R.id.tvTotalPackages);
        tvMonthlySaving = view.findViewById(R.id.tvMonthlySaving);
        tvDaysLeft = view.findViewById(R.id.tvDaysLeft);

        // View all buttons
        btnViewAllCategories = view.findViewById(R.id.btnViewAllCategories);
        btnViewAllFeatured = view.findViewById(R.id.btnViewAllFeatured);
        btnViewAllPopular = view.findViewById(R.id.btnViewAllPopular);

        // Banner
        bannerViewPager = view.findViewById(R.id.bannerViewPager);

        // RecyclerViews
        recyclerCategories = view.findViewById(R.id.recyclerCategories);
        recyclerFeaturedPlans = view.findViewById(R.id.recyclerFeaturedPlans);
        recyclerPopularPlans = view.findViewById(R.id.recyclerPopularPlans);

        // Setup RecyclerViews
        recyclerCategories.setLayoutManager(new GridLayoutManager(getContext(), 4));

        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerFeaturedPlans.setLayoutManager(featuredLayoutManager);

        recyclerPopularPlans.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void setupListeners() {
        btnNotifications.setOnClickListener(v -> {
            // Navigate to notifications
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navigateToNotifications();
            }
        });

        btnSearch.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tính năng tìm kiếm đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        btnQuickMyPackages.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MyPackagesActivity.class));
        });

        btnQuickPremium.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpgradePremiumActivity.class));
        });

        btnQuickHistory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Lịch sử đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        btnQuickSupport.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Hỗ trợ: support@subme.vn", Toast.LENGTH_SHORT).show();
        });

        btnViewAllCategories.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navigateToList();
            }
        });

        btnViewAllFeatured.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navigateToList();
            }
        });

        btnViewAllPopular.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navigateToList();
            }
        });
    }

    private int getUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
                JSONObject json = new JSONObject(payload);
                return json.getInt("sub");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error decoding token: " + e.getMessage());
        }
        return -1;
    }

    private void loadUserProfile() {
        if (userId == -1 || token == null) {
            tvUserName.setText("Người dùng");
            return;
        }

        RetrofitClient.getApiService().getUserProfile("Bearer " + token, userId)
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ProfileResponse profileResponse = response.body();
                            if (profileResponse.isSuccess() && profileResponse.getData() != null) {
                                String name = profileResponse.getData().getName();
                                tvUserName.setText(name != null && !name.isEmpty() ? name : "Người dùng");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        Log.e(TAG, "Error loading profile: " + t.getMessage());
                    }
                });
    }

    private void loadPlans() {
        RetrofitClient.getApiService().getPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Plan> allPlans = response.body();

                    // Split into featured and popular
                    featuredList = new ArrayList<>();
                    popularList = new ArrayList<>();

                    for (int i = 0; i < allPlans.size(); i++) {
                        if (i < 5) {
                            featuredList.add(allPlans.get(i));
                        }
                        popularList.add(allPlans.get(i));
                    }

                    // Setup adapters
                    featuredAdapter = new PlanAdapter(featuredList);
                    recyclerFeaturedPlans.setAdapter(featuredAdapter);

                    popularAdapter = new PlanAdapter(popularList);
                    recyclerPopularPlans.setAdapter(popularAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                Log.e(TAG, "Error loading plans: " + t.getMessage());
                Toast.makeText(getContext(), "Không thể tải danh sách gói", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBanner() {
        // TODO: Implement banner slider with ViewPager2
        // For now, just hide it or show a placeholder
    }

    private void setupStatistics() {
        // TODO: Load real statistics from API
        tvTotalPackages.setText("3");
        tvMonthlySaving.setText("500K");
        tvDaysLeft.setText("15");
    }
}


