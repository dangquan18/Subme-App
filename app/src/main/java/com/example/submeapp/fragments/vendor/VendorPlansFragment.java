package com.example.submeapp.fragments.vendor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VendorPlansFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddPlan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_plans, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPlans);
        fabAddPlan = view.findViewById(R.id.fabAddPlan);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddPlan.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Thêm gói dịch vụ mới", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình thêm gói dịch vụ
        });

        loadPlans();

        return view;
    }

    private void loadPlans() {
        // TODO: Gọi API để lấy danh sách gói dịch vụ của vendor
    }
}

