package com.example.norgaard.barty.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BartyDatabase";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BAR_TABLE =
                "CREATE TABLE " + BartyContract.BarEntry.TABLE_NAME_BARS + " (" +
                        BartyContract.BarEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        BartyContract.BarEntry.COLUMN_BAR_NAME + " TEXT NOT NULL," +
                        "UNIQUE (" + BartyContract.BarEntry.COLUMN_BAR_NAME + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_BASKET_TABLE =
                "CREATE TABLE " + BartyContract.BasketEntry.TABLE_NAME_BASKET + " (" +
                        BartyContract.BasketEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        BartyContract.BasketEntry.COLUMN_DRINK_NAME + " TEXT NOT NULL," +
                        BartyContract.BasketEntry.COLUMN_DRINK_PRICE + " REAL NOT NULL," +
                        BartyContract.BasketEntry.COLUMN_DRINK_QUANTITY + " INTEGER NOT NULL," +
                        BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID + " INTEGER NOT NULL," +
                        " FOREIGN KEY (" + BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID + ") REFERENCES " +
                        BartyContract.BarEntry.TABLE_NAME_BARS + "(" + BartyContract.BarEntry._ID + "));";

        db.execSQL(SQL_CREATE_BAR_TABLE);
        db.execSQL(SQL_CREATE_BASKET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BartyContract.BarEntry.TABLE_NAME_BARS);
        db.execSQL("DROP TABLE IF EXISTS " + BartyContract.BasketEntry.TABLE_NAME_BASKET);
        onCreate(db);
    }
}
