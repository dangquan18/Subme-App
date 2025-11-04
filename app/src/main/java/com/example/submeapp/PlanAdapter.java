package com.example.submeapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.submeapp.api.models.Plan;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<Plan> planList;

    public PlanAdapter(List<Plan> planList) {
        this.planList = planList;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);

        holder.productTitle.setText(plan.getName());

        // Format price
        try {
            double price = Double.parseDouble(plan.getPrice());
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            holder.productPrice.setText(formatter.format(price) + "đ/" + plan.getDurationUnit());
        } catch (Exception e) {
            holder.productPrice.setText(plan.getPrice() + "đ");
        }

        // Load image if available
        if (plan.getImageUrl() != null && !plan.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(plan.getImageUrl())
                    .placeholder(R.drawable.ic_package)
                    .error(R.drawable.ic_package)
                    .into(holder.productImage);
        }

        // Click on card to view details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("planId", plan.getId());
            v.getContext().startActivity(intent);
        });

        // Click on "Xem chi tiết" button
        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("planId", plan.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return planList != null ? planList.size() : 0;
    }

    // Method to update list for filtering
    public void updateList(List<Plan> newList) {
        this.planList = newList;
        notifyDataSetChanged();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice;
        ImageView productImage;
        android.widget.Button btnViewDetail;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnViewDetail = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}

