package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.toolbarRecipeDetail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvName = findViewById(R.id.tvDetailRecipeName);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvInstructions = findViewById(R.id.tvDetailInstructions);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("RECIPE_NAME");
            String ingredients = intent.getStringExtra("RECIPE_INGREDIENTS");
            String instructions = intent.getStringExtra("RECIPE_INSTRUCTIONS");

            if (name != null) tvName.setText(name);
            if (ingredients != null) tvIngredients.setText(ingredients);
            if (instructions != null) tvInstructions.setText(instructions);
        }
    }
    public String getFormattedIngredients(String rawIngredients) {
        if (rawIngredients == null) return "";
        StringBuilder builder = new StringBuilder();
        String[] items = rawIngredients.split(",");
        for (String item : items) {
            String[] parts = item.trim().split(":");
            if (parts.length >= 3) {
                builder.append("• ").append(parts[0]).append(" (").append(parts[1]).append(" ").append(parts[2]).append(")\n");
            } else if (parts.length == 2) {
                builder.append("• ").append(parts[0]).append(" (").append(parts[1]).append(")\n");
            } else {
                builder.append("• ").append(item.trim()).append("\n");
            }
        }
        return builder.toString().trim();
    }
}

