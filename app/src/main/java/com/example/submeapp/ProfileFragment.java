package com.example.submeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.models.ProfileResponse;
import com.example.submeapp.api.models.TokenPayload;
import com.example.submeapp.api.models.UserProfile;
import com.example.submeapp.utils.TokenManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ImageView btnSettings;
    private CardView btnUpgradePremium;
    private CardView btnMyPackages;
    private CardView btnLogout;
    private TextView tvUserName, tvUserEmail, tvUserRole;
    private TokenManager tokenManager;
    private String token;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Khởi tạo TokenManager
        try {
            tokenManager = new TokenManager(requireContext());
            // Get token and userId from TokenManager
            token = tokenManager.getToken();
            if (token == null) {
                token = "";
            }
            userId = getUserIdFromToken(token);
            Log.d(TAG, "Token retrieved: " + (!token.isEmpty() ? "Yes" : "No") + ", userId: " + userId);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing TokenManager", e);
            token = "";
            userId = -1;
        }

        // Khởi tạo views
        btnSettings = view.findViewById(R.id.btnSettings);
        btnUpgradePremium = view.findViewById(R.id.btnUpgradePremium);
        btnMyPackages = view.findViewById(R.id.btnMyPackages);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserRole = view.findViewById(R.id.tvUserRole);

        // Load profile from API
        loadUserProfile();

        // Settings button
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            });
        }

        // Upgrade Premium button
        if (btnUpgradePremium != null) {
            btnUpgradePremium.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), UpgradePremiumActivity.class);
                startActivity(intent);
            });
        }

        // My Packages button
        if (btnMyPackages != null) {
            btnMyPackages.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MyPackagesActivity.class);
                startActivity(intent);
            });
        }

        // Logout button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                try {
                    // Xóa token và quay về màn hình login
                    if (tokenManager != null) {
                        tokenManager.clearToken();
                    }
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload profile when returning to this fragment
        loadUserProfile();
    }

    private int getUserIdFromToken(String token) {
        try {
            Log.d(TAG, "Decoding token: " + token.substring(0, Math.min(20, token.length())) + "...");
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
                Log.d(TAG, "Token payload: " + payload);
                JSONObject json = new JSONObject(payload);
                int userId = json.getInt("sub");
                Log.d(TAG, "Decoded userId: " + userId);
                return userId;
            } else {
                Log.e(TAG, "Token does not have enough parts: " + parts.length);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error decoding token: " + e.getMessage(), e);
        }
        return -1;
    }

    private void loadUserProfile() {
        if (userId == -1 || token.isEmpty()) {
            Log.e(TAG, "Cannot load profile - userId: " + userId + ", token empty: " + token.isEmpty());
            Toast.makeText(getContext(), "Không thể lấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            displayDefaultInfo();
            return;
        }

        Log.d(TAG, "Loading profile for userId: " + userId);

        RetrofitClient.getApiService().getUserProfile("Bearer " + token, userId)
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        Log.d(TAG, "Response code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {
                            ProfileResponse profileResponse = response.body();
                            Log.d(TAG, "Response success: " + profileResponse.isSuccess());

                            if (profileResponse.isSuccess() && profileResponse.getData() != null) {
                                UserProfile profile = profileResponse.getData();
                                Log.d(TAG, "Profile loaded - Name: " + profile.getName() + ", Email: " + profile.getEmail());
                                displayProfile(profile);
                            } else {
                                Log.e(TAG, "Profile response not successful or data is null");
                                Toast.makeText(getContext(), "Không thể tải thông tin: " + profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                displayDefaultInfo();
                            }
                        } else {
                            Log.e(TAG, "Response not successful. Code: " + response.code());
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                                Log.e(TAG, "Error body: " + errorBody);
                                Toast.makeText(getContext(), "Lỗi tải thông tin: " + response.code(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body: " + e.getMessage());
                            }
                            displayDefaultInfo();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        Log.e(TAG, "Error loading profile: " + t.getMessage(), t);
                        Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        displayDefaultInfo();
                    }
                });
    }

    private void displayProfile(UserProfile profile) {
        if (tvUserName != null) {
            String name = profile.getName();
            tvUserName.setText(name != null && !name.isEmpty() ? name : "Chưa cập nhật tên");
        }

        if (tvUserEmail != null) {
            String email = profile.getEmail();
            tvUserEmail.setText(email != null ? email : "");
        }

        if (tvUserRole != null) {
            String role = profile.getRole();
            tvUserRole.setText(role != null ? role.toUpperCase() : "USER");
        }
    }

    private void displayDefaultInfo() {
        try {
            if (tokenManager == null) {
                setDefaultValues();
                return;
            }

            TokenPayload userInfo = tokenManager.getUserInfo();
            if (userInfo != null) {
                if (tvUserName != null) {
                    tvUserName.setText("Chưa cập nhật tên");
                }
                if (tvUserEmail != null) {
                    String email = userInfo.getEmail();
                    tvUserEmail.setText(email != null ? email : "guest@example.com");
                }
                if (tvUserRole != null) {
                    String role = userInfo.getRole();
                    tvUserRole.setText(role != null ? role.toUpperCase() : "USER");
                }
            } else {
                setDefaultValues();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setDefaultValues();
        }
    }

    private void setDefaultValues() {
        if (tvUserName != null) {
            tvUserName.setText("Guest User");
        }
        if (tvUserEmail != null) {
            tvUserEmail.setText("guest@example.com");
        }
        if (tvUserRole != null) {
            tvUserRole.setText("GUEST");
        }
    }

    private void displayUserInfo() {
        // Method không còn sử dụng, giữ lại để tránh lỗi nếu có gọi từ đâu đó
        loadUserProfile();
    }
}

