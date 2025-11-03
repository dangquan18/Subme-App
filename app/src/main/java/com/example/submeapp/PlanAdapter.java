package com.example.submeapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.submeapp.api.models.Plan;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<Plan> planList;

    public PlanAdapter(List<Plan> planList) {
        this.planList = planList;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);

        holder.tvTitle.setText(plan.getName());

        // Hiển thị giá với định dạng đẹp
        String priceText = plan.getFormattedPrice() + "/" + plan.getDurationUnit();
        holder.tvPrice.setText(priceText);

        // Load image from URL using Glide
        if (plan.getImageUrl() != null && !plan.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(plan.getImageUrl())
                .apply(new RequestOptions()
                    .transform(new RoundedCorners(16))
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery))
                .into(holder.ivProductImage);
        } else {
            holder.ivProductImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Click vào toàn bộ card để xem chi tiết
        holder.itemView.setOnClickListener(v -> openPlanDetail(v, plan));

        // Click vào nút "XEM CHI TIẾT"
        holder.btnViewDetail.setOnClickListener(v -> openPlanDetail(v, plan));
    }

    private void openPlanDetail(View view, Plan plan) {
        Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
        intent.putExtra("title", plan.getName());
        intent.putExtra("price", plan.getFormattedPrice() + "/" + plan.getDurationUnit());
        intent.putExtra("description", plan.getDescription());
        intent.putExtra("imageUrl", plan.getImageUrl());
        intent.putExtra("planId", plan.getId());
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public void updateList(List<Plan> newList) {
        this.planList = newList;
        notifyDataSetChanged();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvTitle;
        TextView tvPrice;
        Button btnViewDetail;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            // Sử dụng đúng ID từ item_product.xml
            ivProductImage = itemView.findViewById(R.id.productImage);
            tvTitle = itemView.findViewById(R.id.productTitle);
            tvPrice = itemView.findViewById(R.id.productPrice);
            btnViewDetail = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}

