package com.cinefast.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SnackDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "cinefast.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_SNACKS = "snacks";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "image";

    public SnackDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_SNACKS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT NOT NULL, " +
                        COL_PRICE + " REAL NOT NULL, " +
                        COL_IMAGE + " TEXT NOT NULL" +
                        ")"
        );

        // Seed initial snacks (only runs on first DB creation).
        db.execSQL("INSERT INTO " + TABLE_SNACKS + " (" + COL_NAME + "," + COL_PRICE + "," + COL_IMAGE + ") VALUES " +
                "('Popcorn', 8.99, 'snack_popcorn')," +
                "('Nachos', 7.99, 'snack_nachos')," +
                "('Soft Drink', 4.99, 'snack_drink')," +
                "('Candy Mix', 5.99, 'snack_candy')" +
                "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For assignment scope, simple reset is acceptable.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }
}

