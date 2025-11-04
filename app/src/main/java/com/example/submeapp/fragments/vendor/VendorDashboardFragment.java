package com.example.submeapp.fragments.vendor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.submeapp.R;

public class VendorDashboardFragment extends Fragment {

    private TextView tvTotalRevenue, tvTotalSubscriptions, tvActivePlans, tvPendingOrders;
    private CardView cardRevenue, cardSubscriptions, cardPlans, cardOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_dashboard, container, false);

        // Khởi tạo views
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        tvTotalSubscriptions = view.findViewById(R.id.tvTotalSubscriptions);
        tvActivePlans = view.findViewById(R.id.tvActivePlans);
        tvPendingOrders = view.findViewById(R.id.tvPendingOrders);

        cardRevenue = view.findViewById(R.id.cardRevenue);
        cardSubscriptions = view.findViewById(R.id.cardSubscriptions);
        cardPlans = view.findViewById(R.id.cardPlans);
        cardOrders = view.findViewById(R.id.cardOrders);

        // Load dữ liệu dashboard
        loadDashboardData();

        return view;
    }

    private void loadDashboardData() {
        // TODO: Gọi API để lấy thống kê
        // Tạm thời dùng dữ liệu mẫu
        tvTotalRevenue.setText("12,500,000đ");
        tvTotalSubscriptions.setText("45");
        tvActivePlans.setText("8");
        tvPendingOrders.setText("3");
    }
}

