package com.example.martin.forecastapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherAppDb";
    private static final int DATABASE_VERSION = 4;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitud

        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + ForecastContract.ForecastEntry.TABLE_NAME + " (" +
                        // Why AutoIncrement here, and not above?
                        // Unique keys will be auto-generated in either case.  But for weather
                        // forecasting, it's reasonable to assume the user will want information
                        // for a certain date and all dates *following*, so the forecast data
                        // should be sorted accordingly.
                        ForecastContract.ForecastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                        // the ID of the location entry associated with this weather data
                        ForecastContract.ForecastEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        ForecastContract.ForecastEntry.COLUMN_FORECAST_ID + " INTEGER NOT NULL," +

                        ForecastContract.ForecastEntry.COLUMN_TEMP_MIN + " REAL NOT NULL, " +
                        ForecastContract.ForecastEntry.COLUMN_TEMP_MAX + " REAL NOT NULL, " +

                        ForecastContract.ForecastEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                        ForecastContract.ForecastEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                        ForecastContract.ForecastEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                        ForecastContract.ForecastEntry.COLUMN_DEGREES + " REAL NOT NULL, " +

                        " UNIQUE (" + ForecastContract.ForecastEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ForecastContract.ForecastEntry.TABLE_NAME);
        onCreate(db);
    }
}
