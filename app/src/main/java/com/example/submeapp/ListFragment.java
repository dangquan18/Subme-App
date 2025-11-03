package com.example.submeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submeapp.api.RetrofitClient;
import com.example.submeapp.api.models.Plan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {

    private RecyclerView recyclerViewList;
    private LinearLayout emptyStateLayout;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private PlanAdapter adapter;
    private List<Plan> planList;
    private List<Plan> filteredList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Khởi tạo views
        recyclerViewList = view.findViewById(R.id.recyclerViewList);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        searchEditText = view.findViewById(R.id.searchEditText);
        progressBar = view.findViewById(R.id.progressBar);

        // Setup Grid Layout 2 cột
        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Khởi tạo danh sách
        planList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Khởi tạo adapter
        adapter = new PlanAdapter(filteredList);
        recyclerViewList.setAdapter(adapter);

        // Setup tìm kiếm
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Tải dữ liệu từ API
        loadPlansFromAPI();

        return view;
    }

    private void loadPlansFromAPI() {
        // Hiển thị loading
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        recyclerViewList.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);

        // Gọi API lấy danh sách plans
        RetrofitClient.getApiService().getPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(Call<List<Plan>> call, Response<List<Plan>> response) {
                // Ẩn loading
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null) {
                    List<Plan> allPlans = response.body();

                    // Lọc chỉ lấy các gói có status = "approved"
                    planList.clear();
                    for (Plan plan : allPlans) {
                        if (plan.isApproved()) {
                            planList.add(plan);
                        }
                    }

                    // Cập nhật filteredList và adapter
                    filteredList.clear();
                    filteredList.addAll(planList);
                    adapter.updateList(filteredList);

                    updateEmptyState();
                } else {
                    showError("Không thể tải danh sách gói");
                }
            }

            @Override
            public void onFailure(Call<List<Plan>> call, Throwable t) {
                // Ẩn loading
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                showError("Lỗi kết nối: " + t.getMessage());
                updateEmptyState();
            }
        });
    }

    private void filterList(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            // Nếu không tìm kiếm, hiển thị tất cả
            filteredList.addAll(planList);
        } else {
            // Tìm kiếm theo tên, mô tả, giá
            String lowerCaseQuery = query.toLowerCase();
            for (Plan plan : planList) {
                if (plan.getName().toLowerCase().contains(lowerCaseQuery) ||
                    plan.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                    plan.getPrice().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(plan);
                }
            }
        }

        adapter.updateList(filteredList);
        updateEmptyState();
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    private void updateEmptyState() {
        if (filteredList.isEmpty()) {
            recyclerViewList.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerViewList.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
}

