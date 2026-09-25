package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etUnit, etExpiry;
    private DatabaseHelper dbHelper;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        dbHelper = new DatabaseHelper(this);

        TextView tvTitle = findViewById(R.id.tvHeader);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiry);
        Button btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        if (intent.hasExtra("ITEM_ID")) {
            itemId = intent.getIntExtra("ITEM_ID", -1);
            etName.setText(intent.getStringExtra("ITEM_NAME"));
            etQuantity.setText(String.valueOf(intent.getDoubleExtra("ITEM_QTY", 0)));
            etUnit.setText(intent.getStringExtra("ITEM_UNIT"));
            etExpiry.setText(intent.getStringExtra("ITEM_EXPIRY"));

            if (tvTitle != null) {
                tvTitle.setText("Edit Ingredient");
            }
        }

        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        if (name.isEmpty() || qtyStr.isEmpty() || unit.isEmpty() || expiry.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity format", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;
        if (itemId == -1) {
            success = dbHelper.addPantryItem(name, quantity, unit, expiry);
        } else {
            success = dbHelper.updatePantryItem(itemId, name, quantity, unit, expiry);
        }

        if (success) {
            Toast.makeText(this, itemId == -1 ? "Item added" : "Item updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Operation failed", Toast.LENGTH_SHORT).show();
        }
    }
}