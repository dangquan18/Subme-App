package com.example.submeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerViewNotifications;
    private LinearLayout emptyStateLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerViewNotifications = view.findViewById(R.id.recyclerViewNotifications);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);

        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show empty state by default
        recyclerViewNotifications.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);

        return view;
    }
}

