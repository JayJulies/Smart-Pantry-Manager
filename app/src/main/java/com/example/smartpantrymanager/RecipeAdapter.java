package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvName.setText(recipe.getName());
        holder.tvIngredients.setText(context.getString(R.string.format_ingredients, recipe.getIngredients()));
        holder.tvInstructions.setText(context.getString(R.string.format_instructions, recipe.getInstructions()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_NAME", recipe.getName());
            intent.putExtra("RECIPE_INGREDIENTS", recipe.getIngredients());
            intent.putExtra("RECIPE_INSTRUCTIONS", recipe.getInstructions());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIngredients, tvInstructions;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvIngredients = itemView.findViewById(R.id.tvRecipeIngredients);
            tvInstructions = itemView.findViewById(R.id.tvRecipeInstructions);
        }
    }
}