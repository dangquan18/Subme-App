package com.example.submeapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submeapp.R;
import com.example.submeapp.models.Banner;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<Banner> bannerList;

    public BannerAdapter(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = bannerList.get(position);

        holder.tvBannerTitle.setText(banner.getTitle());
        holder.tvBannerDescription.setText(banner.getDescription());
        holder.ivBannerImage.setImageResource(banner.getImageRes());

        try {
            holder.bannerContainer.setBackgroundColor(Color.parseColor(banner.getBackgroundColor()));
        } catch (Exception e) {
            // Use default gradient if color parsing fails
        }
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout bannerContainer;
        ImageView ivBannerImage;
        TextView tvBannerTitle, tvBannerDescription;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerContainer = itemView.findViewById(R.id.bannerContainer);
            ivBannerImage = itemView.findViewById(R.id.ivBannerImage);
            tvBannerTitle = itemView.findViewById(R.id.tvBannerTitle);
            tvBannerDescription = itemView.findViewById(R.id.tvBannerDescription);
        }
    }
}

