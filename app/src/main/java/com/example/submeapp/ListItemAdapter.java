package com.example.submeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private List<ListItem> items;
    private OnItemDeleteListener deleteListener;

    public interface OnItemDeleteListener {
        void onDelete(int position);
    }

    public ListItemAdapter(List<ListItem> items, OnItemDeleteListener deleteListener) {
        this.items = items;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        ListItem item = items.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemPrice.setText(item.getPrice());
        holder.checkboxItem.setChecked(item.isChecked());

        holder.checkboxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
        });

        holder.btnDeleteItem.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemPrice;
        CheckBox checkboxItem;
        ImageView btnDeleteItem;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            checkboxItem = itemView.findViewById(R.id.checkboxItem);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}

