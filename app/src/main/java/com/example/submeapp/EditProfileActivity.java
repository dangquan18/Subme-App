package com.example.submeapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.ApiService;
import com.example.submeapp.api.models.ProfileResponse;
import com.example.submeapp.api.models.UpdateProfileRequest;
import com.example.submeapp.api.models.UserProfile;
import com.example.submeapp.utils.TokenManager;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private ImageView btnBack;
    private Button btnChangePhoto;
    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editPhone;
    private TextInputEditText editAddress;
    private TextInputEditText editDateOfBirth;
    private Button btnSave;
    private Button btnCancel;
    private ProgressDialog progressDialog;

    private ApiService apiService;
    private TokenManager tokenManager;
    private String token;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize API service
        apiService = RetrofitClient.getApiService();

        // Initialize TokenManager and get token
        try {
            tokenManager = new TokenManager(this);
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

        // Initialize progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);

        btnBack = findViewById(R.id.btnBack);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editDateOfBirth = findViewById(R.id.editDateOfBirth);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Email is read-only
        editEmail.setEnabled(false);
        editEmail.setFocusable(false);

        btnBack.setOnClickListener(v -> finish());

        btnChangePhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            // TODO: Implement image picker
        });

        btnSave.setOnClickListener(v -> updateProfile());

        btnCancel.setOnClickListener(v -> finish());

        // Load profile data
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
        if (userId == -1) {
            Log.e(TAG, "Cannot load profile - userId is -1");
            Toast.makeText(this, "Không thể lấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Loading profile for userId: " + userId);
        Log.d(TAG, "Token: " + token.substring(0, Math.min(20, token.length())) + "...");

        progressDialog.show();

        Call<ProfileResponse> call = apiService.getUserProfile("Bearer " + token, userId);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                progressDialog.dismiss();

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
                        Toast.makeText(EditProfileActivity.this,
                            profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Response not successful. Code: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(EditProfileActivity.this,
                        "Không thể tải thông tin profile. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Error loading profile: " + t.getMessage(), t);
                Toast.makeText(EditProfileActivity.this,
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayProfile(UserProfile profile) {
        editName.setText(profile.getName() != null ? profile.getName() : "");
        editEmail.setText(profile.getEmail() != null ? profile.getEmail() : "");
        editPhone.setText(profile.getPhone() != null ? profile.getPhone() : "");
        editAddress.setText(profile.getAddress() != null ? profile.getAddress() : "");

        // Format date_of_birth if exists (from "1995-05-20T00:00:00.000Z" to "1995-05-20")
        String dob = profile.getDateOfBirth();
        if (dob != null && !dob.isEmpty()) {
            if (dob.contains("T")) {
                dob = dob.split("T")[0];
            }
            editDateOfBirth.setText(dob);
        }
    }

    private void updateProfile() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String dob = editDateOfBirth.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(name)) {
            editName.setError("Vui lòng nhập tên");
            editName.requestFocus();
            return;
        }

        Log.d(TAG, "Updating profile - Name: " + name + ", Phone: " + phone);
        Log.d(TAG, "Address: " + address + ", DOB: " + dob);

        progressDialog.setMessage("Đang cập nhật...");
        progressDialog.show();

        UpdateProfileRequest request = new UpdateProfileRequest(name, phone, address, dob);

        Call<ProfileResponse> call = apiService.updateUserProfile("Bearer " + token, userId, request);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                progressDialog.dismiss();

                Log.d(TAG, "Update response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    ProfileResponse profileResponse = response.body();
                    Log.d(TAG, "Update success: " + profileResponse.isSuccess());

                    if (profileResponse.isSuccess()) {
                        Toast.makeText(EditProfileActivity.this,
                            profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Log.e(TAG, "Update failed: " + profileResponse.getMessage());
                        Toast.makeText(EditProfileActivity.this,
                            profileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Update response not successful. Code: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Update error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading update error body", e);
                    }
                    Toast.makeText(EditProfileActivity.this,
                        "Không thể cập nhật profile. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "Error updating profile: " + t.getMessage(), t);
                Toast.makeText(EditProfileActivity.this,
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

