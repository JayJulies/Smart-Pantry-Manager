package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Pantry");
            }
        }

        dbHelper = new DatabaseHelper(this);

        RecyclerView rvPantry = findViewById(R.id.rvPantry);
        Button btnAddItem = findViewById(R.id.btnAddItem);
        Button btnSuggestRecipes = findViewById(R.id.btnSuggestRecipes);

        if (rvPantry != null) {
            rvPantry.setLayoutManager(new LinearLayoutManager(this));
        }

        pantryList = dbHelper.getAllPantryItems();

        adapter = new PantryAdapter(
                pantryList,
                item -> {
                    Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                    intent.putExtra("ITEM_ID", item.getId());
                    intent.putExtra("ITEM_NAME", item.getName());
                    intent.putExtra("ITEM_QTY", item.getQuantity());
                    intent.putExtra("ITEM_UNIT", item.getUnit());
                    intent.putExtra("ITEM_EXPIRY", item.getExpiryDate());
                    startActivity(intent);
                },
                item -> {
                    boolean deleted = dbHelper.deletePantryItem(item.getId());
                    if (deleted) {
                        Toast.makeText(MainActivity.this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
                        loadPantryData();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        if (rvPantry != null) {
            rvPantry.setAdapter(adapter);
        }

        if (btnAddItem != null) {
            btnAddItem.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                startActivity(intent);
            });
        }

        if (btnSuggestRecipes != null) {
            btnSuggestRecipes.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryData();
    }

    private void loadPantryData() {
        if (dbHelper != null && adapter != null) {
            pantryList = dbHelper.getAllPantryItems();
            adapter.updateList(pantryList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_recipes) {
            startActivity(new Intent(this, RecipeActivity.class));
            return true;
        } else if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.menu_pantry) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}