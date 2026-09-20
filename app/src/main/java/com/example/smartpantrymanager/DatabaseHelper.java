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
    private static final int DATABASE_VERSION = 1;

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
        // Create Pantry Table
        String createPantry = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PANTRY_NAME + " TEXT, " +
                COLUMN_PANTRY_QTY + " REAL, " +
                COLUMN_PANTRY_UNIT + " TEXT, " +
                COLUMN_PANTRY_EXPIRY + " TEXT)";
        db.execSQL(createPantry);


        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT, " +
                COLUMN_RECIPE_INGREDIENTS + " TEXT, " +
                COLUMN_RECIPE_INSTRUCTIONS + " TEXT)";
        db.execSQL(createRecipes);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    public boolean addPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name.trim().toLowerCase());
        values.put(COLUMN_PANTRY_QTY, quantity);
        values.put(COLUMN_PANTRY_UNIT, unit.trim().toLowerCase());
        values.put(COLUMN_PANTRY_EXPIRY, expiryDate);

        long result = db.insert(TABLE_PANTRY, null, values);
        return result != -1;
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);
    }

    public boolean updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PANTRY_NAME, name.trim().toLowerCase());
        values.put(COLUMN_PANTRY_QTY, quantity);
        values.put(COLUMN_PANTRY_UNIT, unit.trim().toLowerCase());
        values.put(COLUMN_PANTRY_EXPIRY, expiryDate);

        int rows = db.update(TABLE_PANTRY, values, COLUMN_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_PANTRY, COLUMN_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }


    public Cursor getSuggestedRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> matchingRecipeIds = new ArrayList<>();

        Cursor recipeCursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        if (recipeCursor.moveToFirst()) {
            do {
                int recipeId = recipeCursor.getInt(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
                String ingredientsNeeded = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_INGREDIENTS));

                if (canMakeRecipe(ingredientsNeeded)) {
                    matchingRecipeIds.add(recipeId);
                }
            } while (recipeCursor.moveToNext());
        }
        recipeCursor.close();

        if (matchingRecipeIds.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_RECIPES + " WHERE " + COLUMN_RECIPE_ID + " IN (");
        for (int i = 0; i < matchingRecipeIds.size(); i++) {
            query.append(matchingRecipeIds.get(i));
            if (i < matchingRecipeIds.size() - 1) {
                query.append(",");
            }
        }
        query.append(")");

        return db.rawQuery(query.toString(), null);
    }

    private boolean canMakeRecipe(String ingredientsNeeded) {
        String[] requiredItems = ingredientsNeeded.split(",");

        for (String item : requiredItems) {
            String[] parts = item.trim().split(":");
            if (parts.length < 2) continue;

            String reqName = parts[0].trim().toLowerCase();
            double reqQty = Double.parseDouble(parts[1].trim());

            if (!hasSufficientIngredient(reqName, reqQty)) {
                return false;
            }
        }
        return true;
    }


    private boolean hasSufficientIngredient(String reqName, double reqQty) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PANTRY_QTY + " FROM " + TABLE_PANTRY +
                        " WHERE " + COLUMN_PANTRY_NAME + " = ? OR " + COLUMN_PANTRY_NAME + " = ?",
                new String[]{reqName, reqName + "s"});

        double totalQty = 0;
        if (cursor.moveToFirst()) {
            do {
                totalQty += cursor.getDouble(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return totalQty >= reqQty;
    }


    private void seedRecipes(SQLiteDatabase db) {
        insertSeed(db, "Scrambled Eggs", "egg:2, butter:1, salt:1", "1. Whisk eggs with salt.\n2. Melt butter in pan.\n3. Cook eggs gently.");
        insertSeed(db, "Omelette", "egg:3, cheese:1, butter:1", "1. Whisk eggs.\n2. Pour into heated buttered pan.\n3. Add cheese and fold.");
        insertSeed(db, "Pancakes", "flour:2, milk:1, egg:1, butter:2", "1. Mix flour, milk, egg, butter.\n2. Pour batter on skillet.\n3. Flip when bubbly.");
        insertSeed(db, "Grilled Cheese", "bread:2, cheese:2, butter:1", "1. Butter bread.\n2. Place cheese between bread.\n3. Grill until golden.");
        insertSeed(db, "Garlic Toast", "bread:2, butter:1, garlic:1", "1. Mix garlic into butter.\n2. Spread on bread.\n3. Toast until crisp.");
        insertSeed(db, "Boiled Eggs", "egg:2, water:2", "1. Place eggs in water.\n2. Boil for 8 minutes.\n3. Peel and serve.");
        insertSeed(db, "French Toast", "bread:2, egg:1, milk:1, butter:1", "1. Dip bread in egg and milk mix.\n2. Fry in butter until brown.");
        insertSeed(db, "Fried Rice", "rice:2, egg:1, soy sauce:1, oil:1", "1. Fry egg in oil.\n2. Stir in cooked rice and soy sauce.");
        insertSeed(db, "Simple Pasta", "pasta:2, butter:2, cheese:1", "1. Boil pasta in water.\n2. Drain.\n3. Toss with butter and cheese.");
        insertSeed(db, "Tomato Soup", "tomato:4, water:2, salt:1, butter:1", "1. Simmer tomatoes in water.\n2. Blend smooth.\n3. Stir in butter and salt.");
        insertSeed(db, "Mashed Potatoes", "potato:3, butter:2, milk:1, salt:1", "1. Boil potatoes until soft.\n2. Mash with butter and milk.");
        insertSeed(db, "Potato Chips", "potato:2, oil:2, salt:1", "1. Slice potatoes thinly.\n2. Deep fry in hot oil.\n3. Sprinkle salt.");
        insertSeed(db, "Basic Salad", "lettuce:2, tomato:1, olive oil:1", "1. Chop lettuce and tomato.\n2. Toss with olive oil.");
        insertSeed(db, "Steamed Rice", "rice:1, water:2", "1. Rinse rice.\n2. Simmer with water for 15 minutes covered.");
        insertSeed(db, "Lemon Tea", "tea bag:1, water:1, lemon:1", "1. Steep tea bag in boiling water.\n2. Squeeze lemon juice.");
    }

    private void insertSeed(SQLiteDatabase db, String name, String ingredients, String instructions) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_INGREDIENTS, ingredients);
        values.put(COLUMN_RECIPE_INSTRUCTIONS, instructions);
        db.insert(TABLE_RECIPES, null, values);
    }
}