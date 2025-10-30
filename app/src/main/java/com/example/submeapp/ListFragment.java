package com.example.submeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerViewList;
    private LinearLayout emptyStateLayout;
    private EditText searchEditText;
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerViewList = view.findViewById(R.id.recyclerViewList);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        searchEditText = view.findViewById(R.id.searchEditText);

        // Use Grid Layout like Home (2 columns)
        recyclerViewList.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize subscription packages (same as Home)
        productList = new ArrayList<>();
        productList.add(new Product("Gói Netflix Premium", "180.000đ/tháng"));
        productList.add(new Product("Gói Spotify Family", "120.000đ/tháng"));
        productList.add(new Product("Gói YouTube Premium", "89.000đ/tháng"));
        productList.add(new Product("Gói Disney+ Standard", "95.000đ/tháng"));
        productList.add(new Product("Gói Office 365 Personal", "150.000đ/tháng"));
        productList.add(new Product("Gói Adobe Creative Cloud", "450.000đ/tháng"));
        productList.add(new Product("Gói Amazon Prime", "99.000đ/tháng"));
        productList.add(new Product("Gói Apple Music", "59.000đ/tháng"));

        filteredList = new ArrayList<>(productList);

        adapter = new ProductAdapter(filteredList);
        recyclerViewList.setAdapter(adapter);

        // Setup search functionality
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

        updateEmptyState();

        return view;
    }

    private void filterList(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Product product : productList) {
                if (product.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    product.getPrice().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(product);
                }
            }
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
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

