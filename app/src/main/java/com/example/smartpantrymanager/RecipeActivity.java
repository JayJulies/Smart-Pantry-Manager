package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        RecyclerView rvRecipes = findViewById(R.id.rvRecipes);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        List<Recipe> suggestedRecipes = dbHelper.getSuggestedRecipes();

        if (suggestedRecipes.isEmpty()) {
            Toast.makeText(this, "No recipes found matching your pantry ingredients.", Toast.LENGTH_LONG).show();
        }

        RecipeAdapter adapter = new RecipeAdapter(suggestedRecipes);
        rvRecipes.setAdapter(adapter);
    }
}