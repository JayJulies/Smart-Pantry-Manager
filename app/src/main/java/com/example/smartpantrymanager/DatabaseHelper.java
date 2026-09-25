package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PANTRY = "pantry";
    public static final String COLUMN_PANTRY_ID = "id";
    public static final String COLUMN_PANTRY_NAME = "name";
    public static final String COLUMN_PANTRY_QTY = "quantity";
    public static final String COLUMN_PANTRY_UNIT = "unit";
    public static final String COLUMN_PANTRY_EXPIRY = "expiry_date";


    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";
    public static final String COLUMN_RECIPE_INSTRUCTIONS = "instructions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PANTRY_NAME + " TEXT NOT NULL, " +
                COLUMN_PANTRY_QTY + " REAL NOT NULL, " +
                COLUMN_PANTRY_UNIT + " TEXT NOT NULL, " +
                COLUMN_PANTRY_EXPIRY + " TEXT);";

        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                COLUMN_RECIPE_INGREDIENTS + " TEXT NOT NULL, " +
                COLUMN_RECIPE_INSTRUCTIONS + " TEXT NOT NULL);";

        db.execSQL(createPantryTable);
        db.execSQL(createRecipesTable);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipeSeed(db, "Scrambled Eggs", "Egg:2:pcs, Milk:0.1:l", "Whisk eggs with milk. Cook on medium heat for 3-5 minutes until set.");
        insertRecipeSeed(db, "Omelette", "Egg:3:pcs, Milk:0.05:l, Salt:1:g", "Beat eggs with milk and salt. Pour into heated skillet and fold when solid.");
        insertRecipeSeed(db, "Pancakes", "Flour:1:kg, Milk:0.5:l, Egg:2:pcs", "Mix flour, milk, and eggs into a smooth batter. Fry circular portions on a pan.");
        insertRecipeSeed(db, "French Toast", "Bread:2:pcs, Egg:1:pcs, Milk:0.1:l", "Whisk egg and milk. Dip bread slices and fry until golden brown on both sides.");
        insertRecipeSeed(db, "Grilled Cheese Sandwich", "Bread:2:pcs, Cheese:1:pcs", "Place cheese between bread slices and grill on pan until melted and golden.");
        insertRecipeSeed(db, "Pasta Tomato Sauce", "Pasta:0.25:kg, Tomato:2:pcs, Garlic:1:pcs", "Boil pasta. Chop tomatoes and garlic, simmer into sauce, and toss together.");
        insertRecipeSeed(db, "Garlic Butter Pasta", "Pasta:0.2:kg, Butter:0.05:kg, Garlic:2:pcs", "Boil pasta. Melt butter, sauté minced garlic, and toss with warm pasta.");
        insertRecipeSeed(db, "Fried Rice", "Rice:0.2:kg, Egg:2:pcs, Soy Sauce:0.02:l", "Cook rice. Stir fry with beaten eggs and soy sauce over high heat.");
        insertRecipeSeed(db, "Chicken Stir Fry", "Chicken:0.3:kg, Rice:0.2:kg, Soy Sauce:0.03:l", "Sauté chopped chicken, add soy sauce, and serve hot over steamed rice.");
        insertRecipeSeed(db, "Chicken Salad", "Chicken:0.2:kg, Lettuce:1:pcs, Tomato:1:pcs", "Cook chicken strips. Toss with chopped fresh lettuce and tomatoes.");
        insertRecipeSeed(db, "Tomato Soup", "Tomato:4:pcs, Milk:0.2:l, Butter:0.02:kg", "Simmer tomatoes in butter, blend until smooth, stir in milk, and heat through.");
        insertRecipeSeed(db, "Boiled Eggs", "Egg:2:pcs", "Place eggs in boiling water for 7-10 minutes. Cool in cold water and peel.");
        insertRecipeSeed(db, "Mashed Potatoes", "Potato:0.5:kg, Milk:0.1:l, Butter:0.03:kg", "Boil potatoes until soft. Mash thoroughly with warm milk and butter.");
        insertRecipeSeed(db, "Baked Potato", "Potato:2:pcs, Butter:0.02:kg", "Bake potatoes at 200°C for 45 minutes. Cut open and top with butter.");
        insertRecipeSeed(db, "Egg Rice Bowl", "Rice:0.25:kg, Egg:1:pcs, Soy Sauce:0.01:l", "Place warm cooked rice in bowl, top with a fried egg and drizzle soy sauce.");
        insertRecipeSeed(db, "Guacamole Salad", "Avocado:2:pcs, Tomato:1:pcs, Lemon:1:pcs", "Mash avocados, mix with diced tomato, and squeeze fresh lemon juice over.");
    }

    private void insertRecipeSeed(SQLiteDatabase db, String name, String ingredients, String instructions) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_INGREDIENTS, ingredients);
        values.put(COLUMN_RECIPE_INSTRUCTIONS, instructions);
        db.insert(TABLE_RECIPES, null, values);
    }


    public boolean addPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name);
        values.put(COLUMN_PANTRY_QTY, quantity);
        values.put(COLUMN_PANTRY_UNIT, unit);
        values.put(COLUMN_PANTRY_EXPIRY, expiryDate);

        long id = db.insert(TABLE_PANTRY, null, values);
        return id != -1;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_NAME));
                double qty = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_QTY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_UNIT));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PANTRY_EXPIRY));

                list.add(new PantryItem(id, name, qty, unit, expiry));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name);
        values.put(COLUMN_PANTRY_QTY, quantity);
        values.put(COLUMN_PANTRY_UNIT, unit);
        values.put(COLUMN_PANTRY_EXPIRY, expiryDate);

        int rows = db.update(TABLE_PANTRY, values, COLUMN_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PANTRY, COLUMN_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INGREDIENTS));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS));

                list.add(new Recipe(id, name, ingredients, instructions));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Recipe> getSuggestedRecipes() {
        List<Recipe> allRecipes = getAllRecipes();
        List<PantryItem> pantryItems = getAllPantryItems();
        List<Recipe> matchingRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            String ingredientsStr = recipe.getIngredients();
            if (ingredientsStr == null || ingredientsStr.isEmpty()) {
                continue;
            }

            String[] requiredList = ingredientsStr.split(",");
            boolean recipeCanBeMade = true;

            for (String reqItem : requiredList) {
                String[] parts = reqItem.trim().split(":");
                if (parts.length < 2) {
                    recipeCanBeMade = false;
                    break;
                }

                String reqName = parts[0].trim();
                double reqQty = 0;
                try {
                    reqQty = Double.parseDouble(parts[1].trim());
                } catch (NumberFormatException e) {
                    recipeCanBeMade = false;
                    break;
                }


                boolean ingredientFound = false;
                for (PantryItem pantry : pantryItems) {
                    if (pantry.getName().equalsIgnoreCase(reqName)) {
                        if (pantry.getQuantity() >= reqQty) {
                            ingredientFound = true;
                        }
                        break;
                    }
                }

                if (!ingredientFound) {
                    recipeCanBeMade = false;
                    break;
                }
            }

            if (recipeCanBeMade) {
                matchingRecipes.add(recipe);
            }
        }

        return matchingRecipes;
    }
}