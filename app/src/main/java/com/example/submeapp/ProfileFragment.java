package com.example.submeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private ImageView btnSettings;
    private CardView btnUpgradePremium;
    private CardView btnMyPackages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnSettings = view.findViewById(R.id.btnSettings);
        btnUpgradePremium = view.findViewById(R.id.btnUpgradePremium);
        btnMyPackages = view.findViewById(R.id.btnMyPackages);

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        btnUpgradePremium.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpgradePremiumActivity.class);
            startActivity(intent);
        });

        btnMyPackages.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyPackagesActivity.class);
            startActivity(intent);
        });

        return view;
    }
}

