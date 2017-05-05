/**
 * Created by Jodaa on 05-05-2017.
 */
package com.example.jodaa.weatherdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DataAccess {
    public DatabaseHelper dbHelper;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;

    public DataAccess(Context context){
        dbHelper = new DatabaseHelper(context);
    }


    public void insertData(String temp, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put("weather_temp", temp);
            values.put("weather_date", date);
            // Insert Row
            long i = db.insert(dbHelper.TABLE_WEATHER, null, values);
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
