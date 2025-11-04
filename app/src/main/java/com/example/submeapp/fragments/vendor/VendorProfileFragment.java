package com.example.submeapp.fragments.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.submeapp.LoginActivity;
import com.example.submeapp.R;
import com.example.submeapp.api.models.TokenPayload;
import com.example.submeapp.utils.TokenManager;

public class VendorProfileFragment extends Fragment {

    private TextView tvVendorName, tvVendorEmail;
    private LinearLayout btnEditProfile, btnSettings, btnLogout;
    private TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_profile, container, false);

        tokenManager = new TokenManager(requireContext());

        tvVendorName = view.findViewById(R.id.tvVendorName);
        tvVendorEmail = view.findViewById(R.id.tvVendorEmail);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnSettings = view.findViewById(R.id.btnSettings);
        btnLogout = view.findViewById(R.id.btnLogout);

        loadVendorInfo();

        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chỉnh sửa thông tin", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình chỉnh sửa thông tin
        });

        btnSettings.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Cài đặt", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> showLogoutDialog());

        return view;
    }

    private void loadVendorInfo() {
        TokenPayload userInfo = tokenManager.getUserInfo();
        if (userInfo != null) {
            tvVendorName.setText("Vendor Account");
            tvVendorEmail.setText(userInfo.getEmail());
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    tokenManager.clearToken();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

