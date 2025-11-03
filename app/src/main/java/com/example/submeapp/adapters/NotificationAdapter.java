package com.example.submeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submeapp.R;
import com.example.submeapp.api.models.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    public void updateData(List<Notification> newNotifications) {
        this.notifications = newNotifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        // Set title and message
        holder.txtTitle.setText(notification.getTitle());
        holder.txtMessage.setText(notification.getMessage());

        // Set time
        holder.txtTime.setText(formatTime(notification.getCreatedAt()));

        // Show unread indicator
        if (notification.isRead()) {
            holder.unreadIndicator.setVisibility(View.GONE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.txtTitle.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.unreadIndicator.setVisibility(View.VISIBLE);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            holder.txtTitle.setTextColor(context.getResources().getColor(android.R.color.black));
        }

        // Click listener
        holder.cardView.setOnClickListener(v -> {
            // TODO: Mark as read and show detail
            // For now, just show a toast
            android.widget.Toast.makeText(context,
                "Thông báo: " + notification.getTitle(),
                android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    private String formatTime(String createdAt) {
        try {
            // Parse ISO 8601 format
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = isoFormat.parse(createdAt);

            if (date != null) {
                // Calculate time difference
                long diff = System.currentTimeMillis() - date.getTime();
                long minutes = diff / (1000 * 60);
                long hours = diff / (1000 * 60 * 60);
                long days = diff / (1000 * 60 * 60 * 24);

                if (minutes < 60) {
                    return minutes + " phút trước";
                } else if (hours < 24) {
                    return hours + " giờ trước";
                } else if (days < 7) {
                    return days + " ngày trước";
                } else {
                    // Format to date
                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    return displayFormat.format(date);
                }
            }
        } catch (ParseException e) {
            android.util.Log.e("NotificationAdapter", "Error parsing date: " + e.getMessage());
        }
        return createdAt;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        View unreadIndicator;
        TextView txtTitle;
        TextView txtMessage;
        TextView txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}

