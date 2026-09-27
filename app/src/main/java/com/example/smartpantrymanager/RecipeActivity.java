package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.TextView;
import android.view.View;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = findViewById(R.id.toolbarRecipe);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Suggested Recipes");
            }
        }

        RecyclerView rvRecipes = findViewById(R.id.rvRecipes);
        TextView tvEmptyRecipes = findViewById(R.id.tvEmptyRecipes);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        List<Recipe> suggestedRecipes;
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            suggestedRecipes = dbHelper.getSuggestedRecipes();
        }

        if (suggestedRecipes.isEmpty()) {
            if (tvEmptyRecipes != null) tvEmptyRecipes.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.GONE);
        } else {
            if (tvEmptyRecipes != null) tvEmptyRecipes.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }

        RecipeAdapter adapter = new RecipeAdapter(suggestedRecipes);
        rvRecipes.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_pantry) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.menu_recipes) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}