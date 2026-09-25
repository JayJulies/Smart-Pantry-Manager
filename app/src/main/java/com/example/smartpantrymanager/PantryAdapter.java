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

    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(PantryItem item);
    }

    private List<PantryItem> pantryList;
    private final OnItemClickListener itemClickListener;
    private final OnDeleteClickListener deleteClickListener;

    public PantryAdapter(List<PantryItem> pantryList, OnItemClickListener itemClickListener, OnDeleteClickListener deleteClickListener) {
        this.pantryList = pantryList;
        this.itemClickListener = itemClickListener;
        this.deleteClickListener = deleteClickListener;
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
        PantryItem item = pantryList.get(position);

        holder.tvName.setText(item.getName());

        String qtyText = holder.itemView.getContext().getString(
                R.string.format_quantity, item.getQuantity(), item.getUnit());
        holder.tvQuantity.setText(qtyText);

        String expiryText = holder.itemView.getContext().getString(
                R.string.format_expiry, item.getExpiryDate());
        holder.tvExpiry.setText(expiryText);

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(item));

        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return pantryList != null ? pantryList.size() : 0;
    }

    public void updateList(List<PantryItem> newList) {
        this.pantryList = newList;
        notifyDataSetChanged();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiry;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQty);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}