/**
 * Created by Jodaa on 05-05-2017.
 * Source used:
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
package com.example.jodaa.weatherdatabase;
import android.provider.BaseColumns;

public final class WeatherDatabaseContract {
    private WeatherDatabaseContract(){}

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TEMP + " TEXT," +
                    FeedEntry.COLUMN_NAME_TIME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


    public static class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME = "WeatherData";
        public static final String COLUMN_NAME_TEMP = "Temperature";
        public static final String COLUMN_NAME_TIME = "Time";
    }
}
