/**
 * Created by Joachim on 05-05-2017.
 * http://www.worldbestlearningcenter.com/tips/Android-sqlite-table-layout-example.htm
 */

package com.example.jodaa.weatherdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weatherAppDb";
    private static final int DATABASE_VERSION = 2;
    static final String TABLE_WEATHER = "tableWeather";
    private static final String CREATE_TABLE_WEATHER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_WEATHER +
            "(weather_id INTEGER PRIMARY KEY AUTOINCREMENT, weather_temp TEXT NULL, weather_date TEXT NULL)";
    private static final String DELETE_TABLE_WEATHER = "DROP TABLE IF EXISTS " + TABLE_WEATHER;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_WEATHER);
        onCreate(db);
    }

    public void insertData(String temp, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put("weather_temp", temp);
            values.put("weather_date", date);
            // Insert Row
            long i = db.insert(TABLE_WEATHER, null, values);
            Log.i("Insert", i + "");
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

}