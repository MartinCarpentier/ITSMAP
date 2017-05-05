/**
 * Created by Jodaa on 05-05-2017.
 * Source used:
 * https://developer.android.com/training/basics/data-storage/databases.html
 */

package com.example.jodaa.weatherdatabase;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDatabaseHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeatherDatabaseContract.FeedEntry.TABLE_NAME + " (" +
                    WeatherDatabaseContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    WeatherDatabaseContract.FeedEntry.COLUMN_NAME_TEMP + " TEXT," +
                    WeatherDatabaseContract.FeedEntry.COLUMN_NAME_TIME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WeatherDatabaseContract.FeedEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    public WeatherDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

}