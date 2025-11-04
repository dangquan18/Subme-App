package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.submeapp.fragments.vendor.VendorDashboardFragment;
import com.example.submeapp.fragments.vendor.VendorPlansFragment;
import com.example.submeapp.fragments.vendor.VendorProfileFragment;
import com.example.submeapp.fragments.vendor.VendorSubscriptionsFragment;
import com.example.submeapp.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VendorMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        tokenManager = new TokenManager(this);

        // Kiểm tra đăng nhập
        if (!tokenManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_vendor_dashboard) {
                    selectedFragment = new VendorDashboardFragment();
                } else if (itemId == R.id.nav_vendor_plans) {
                    selectedFragment = new VendorPlansFragment();
                } else if (itemId == R.id.nav_vendor_subscriptions) {
                    selectedFragment = new VendorSubscriptionsFragment();
                } else if (itemId == R.id.nav_vendor_profile) {
                    selectedFragment = new VendorProfileFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

        // Load fragment mặc định
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_vendor_dashboard);
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

