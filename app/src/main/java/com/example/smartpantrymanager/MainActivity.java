package com.example.smartpantrymanager;

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

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Initialize Views
        RecyclerView rvPantry = findViewById(R.id.rvPantry);
        Button btnAddItem = findViewById(R.id.btnAddItem);

        // Configure RecyclerView
        rvPantry.setLayoutManager(new LinearLayoutManager(this));

        // Load data from database
        pantryList = dbHelper.getAllPantryItems();

        // Initialize Adapter with item click/delete listener
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

        // Placeholder click listener for Add button
        btnAddItem.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Add Item feature coming next!", Toast.LENGTH_SHORT).show()
        );
    }

    // Helper method to reload data from SQLite database
    private void loadPantryData() {
        pantryList = dbHelper.getAllPantryItems();
        adapter.updateList(pantryList);
    }
}