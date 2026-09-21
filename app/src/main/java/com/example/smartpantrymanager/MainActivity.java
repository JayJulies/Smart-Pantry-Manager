package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private List<PantryItem> pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        RecyclerView rvPantry = findViewById(R.id.rvPantry);
        Button btnAddItem = findViewById(R.id.btnAddItem);

        rvPantry.setLayoutManager(new LinearLayoutManager(this));

        pantryList = dbHelper.getAllPantryItems();

        adapter = new PantryAdapter(pantryList, item -> {
            boolean deleted = dbHelper.deletePantryItem(item.getId());
            if (deleted) {
                Toast.makeText(MainActivity.this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
                loadPantryData(); // Refresh list display
            } else {
                Toast.makeText(MainActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
            }
        });

        rvPantry.setAdapter(adapter);

        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            startActivity(intent);
        });
        Button btnSuggestRecipes = findViewById(R.id.btnSuggestRecipes);

        btnSuggestRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryData();
    }

    private void loadPantryData() {
        pantryList = dbHelper.getAllPantryItems();
        adapter.updateList(pantryList);
    }
}