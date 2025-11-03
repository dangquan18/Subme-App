package com.example.submeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submeapp.PackageDetailActivity;
import com.example.submeapp.R;
import com.example.submeapp.api.models.UserSubscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private Context context;
    private List<UserSubscription> subscriptions;

    public SubscriptionAdapter(Context context, List<UserSubscription> subscriptions) {
        this.context = context;
        this.subscriptions = subscriptions;
    }

    public void updateData(List<UserSubscription> newSubscriptions) {
        this.subscriptions = newSubscriptions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserSubscription subscription = subscriptions.get(position);
        UserSubscription.PlanDetail plan = subscription.getPlan();

        // Set plan name
        holder.txtPackageName.setText(plan.getName());

        // Set vendor name
        if (plan.getVendor() != null) {
            holder.txtVendor.setText(plan.getVendor().getName());
        }

        // Set category
        if (plan.getCategory() != null) {
            holder.txtCategory.setText(plan.getCategory().getName());
        }

        // Set price
        holder.txtPrice.setText(plan.getFormattedPrice() + " VNĐ/" + plan.getDurationUnit());

        // Set dates
        holder.txtStartDate.setText("Bắt đầu: " + formatDate(subscription.getStartDate()));
        holder.txtEndDate.setText("Hết hạn: " + formatDate(subscription.getEndDate()));

        // Set status
        String status = subscription.getStatus();
        holder.txtStatus.setText(getStatusText(status));
        holder.txtStatus.setTextColor(getStatusColor(status));

        // Calculate remaining days
        int remainingDays = calculateRemainingDays(subscription.getEndDate());
        if (remainingDays >= 0) {
            holder.txtRemainingDays.setText("Còn " + remainingDays + " ngày");
            holder.txtRemainingDays.setVisibility(View.VISIBLE);
        } else {
            holder.txtRemainingDays.setVisibility(View.GONE);
        }

        // Set description
        holder.txtDescription.setText(plan.getDescription());

        // Click listeners
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PackageDetailActivity.class);
            intent.putExtra("subscriptionId", subscription.getId());
            intent.putExtra("planId", plan.getId());
            intent.putExtra("planName", plan.getName());
            intent.putExtra("price", plan.getPrice());
            intent.putExtra("startDate", subscription.getStartDate());
            intent.putExtra("endDate", subscription.getEndDate());
            context.startActivity(intent);
        });

        holder.btnRenew.setOnClickListener(v -> {
            Toast.makeText(context, "Gia hạn gói: " + plan.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Navigate to renewal flow
        });

        holder.btnCancel.setOnClickListener(v -> {
            Toast.makeText(context, "Hủy gói: " + plan.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Implement cancel subscription
        });
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr;
        }
    }

    private int calculateRemainingDays(String endDateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date endDate = format.parse(endDateStr);
            Date today = new Date();
            long diff = endDate.getTime() - today.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            return -1;
        }
    }

    private String getStatusText(String status) {
        switch (status.toLowerCase()) {
            case "active":
                return "Đang hoạt động";
            case "expired":
                return "Đã hết hạn";
            case "cancelled":
                return "Đã hủy";
            default:
                return status;
        }
    }

    private int getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "active":
                return context.getResources().getColor(android.R.color.holo_green_dark);
            case "expired":
                return context.getResources().getColor(android.R.color.holo_red_dark);
            case "cancelled":
                return context.getResources().getColor(android.R.color.darker_gray);
            default:
                return context.getResources().getColor(android.R.color.black);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtPackageName;
        TextView txtVendor;
        TextView txtCategory;
        TextView txtPrice;
        TextView txtStartDate;
        TextView txtEndDate;
        TextView txtStatus;
        TextView txtRemainingDays;
        TextView txtDescription;
        Button btnRenew;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            txtPackageName = itemView.findViewById(R.id.txtPackageName);
            txtVendor = itemView.findViewById(R.id.txtVendor);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtRemainingDays = itemView.findViewById(R.id.txtRemainingDays);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            btnRenew = itemView.findViewById(R.id.btnRenew);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}

