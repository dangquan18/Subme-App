package com.example.submeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.submeapp.api.models.TokenPayload;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public class TokenManager {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Lưu token
    public void saveToken(String token) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    // Lấy token
    public String getToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    // Xóa token (logout)
    public void clearToken() {
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply();
    }

    // Kiểm tra đã đăng nhập chưa
    public boolean isLoggedIn() {
        String token = getToken();
        if (token == null) {
            return false;
        }

        TokenPayload payload = decodeToken(token);
        return payload != null && !payload.isExpired();
    }

    // Decode JWT token để lấy payload
    public TokenPayload decodeToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }

            String payload = parts[1];
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE | Base64.NO_WRAP);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            return gson.fromJson(decodedString, TokenPayload.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy thông tin user từ token
    public TokenPayload getUserInfo() {
        String token = getToken();
        if (token != null) {
            return decodeToken(token);
        }
        return null;
    }

    // Lấy token với prefix Bearer
    public String getBearerToken() {
        String token = getToken();
        if (token != null) {
            return "Bearer " + token;
        }
        return null;
    }
}

