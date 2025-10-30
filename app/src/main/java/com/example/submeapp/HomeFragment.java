package com.example.submeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Create sample products
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", "$10.00"));
        productList.add(new Product("Product 2", "$15.00"));
        productList.add(new Product("Product 3", "$20.00"));
        productList.add(new Product("Product 4", "$25.00"));
        productList.add(new Product("Product 5", "$30.00"));
        productList.add(new Product("Product 6", "$35.00"));

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);


        return view;
    }
}

