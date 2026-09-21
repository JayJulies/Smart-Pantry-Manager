package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> itemList;
    private final OnItemDeleteListener deleteListener;

    // Interface to handle delete button clicks in MainActivity
    public interface OnItemDeleteListener {
        void onDeleteClick(PantryItem item);
    }

    // Constructor
    public PantryAdapter(List<PantryItem> itemList, OnItemDeleteListener deleteListener) {
        this.itemList = itemList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = itemList.get(position);

        holder.tvName.setText(item.getName());

        // Use string resources with placeholders to satisfy linter and support localization
        String qtyText = holder.itemView.getContext().getString(
                R.string.format_quantity, item.getQuantity(), item.getUnit());
        holder.tvQty.setText(qtyText);

        String expiryText = holder.itemView.getContext().getString(
                R.string.format_expiry, item.getExpiryDate());
        holder.tvExpiry.setText(expiryText);

        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    // Update list dynamically when database changes
    @SuppressWarnings("NotifyDataSetChanged")
    public void updateList(List<PantryItem> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder class to hold references to item views
    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvExpiry;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvItemQty);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}