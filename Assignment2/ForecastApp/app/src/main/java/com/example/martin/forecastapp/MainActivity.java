package com.example.martin.forecastapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.martin.forecastapp.data.ForecastContract;
import com.example.martin.forecastapp.services.WeatherService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //putFakeDataInDatabase();

        if (findViewById(R.id.fragment_detail_layout) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_layout, fragment)
                    .commit();



        } else {
            mTwoPane = false;
        }

        Intent intent = new Intent(MainActivity.this,WeatherService.class);
        MainActivity.this.startService(intent);
        Log.i("AutoStart", "started");

        //ForecastFragment forecastFragment =  ((ForecastFragment)getSupportFragmentManager()
                //.findFragmentById(R.id.fragment_forecast));
        //forecastFragment.setUseTodayLayout(!mTwoPane);
    }

    private void putFakeDataInDatabase() {
        ContentResolver ForecastContentResolver = getApplicationContext().getContentResolver();

        ContentValues[] values = new ContentValues[3];
        values[0] = createContentValue(Calendar.getInstance().getTimeInMillis(), 20.5, 14.3, 933, 412.3, 52.2, 142.3, 1);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        values[1] = createContentValue(tomorrow.getTimeInMillis(), 22.1, 14.3, 933, 412.3, 52.2, 142.3, 2);

        Calendar dayAfterTomorrow = Calendar.getInstance();
        dayAfterTomorrow.add(Calendar.DATE, 2);
        values[2] = createContentValue(dayAfterTomorrow.getTimeInMillis(), 24.3, 14.3, 933, 412.3, 52.2, 142.3, 3);


                /* Delete old weather data because we don't need to keep multiple days' data */
        ForecastContentResolver.bulkInsert(
                ForecastContract.ForecastEntry.CONTENT_URI,
                values);
    }

    private ContentValues createContentValue(long dateTimeMillis, double high, double low, int humidity, double pressure
            , double windSpeed, double windDirection, int forecast_id)
    {
        ContentValues forecastValues = new ContentValues();

        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_DATE, dateTimeMillis);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_HUMIDITY, humidity);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_PRESSURE, pressure);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED, windSpeed);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_DEGREES, windDirection);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_TEMP_MAX, high);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_TEMP_MIN, low);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_FORECAST_ID, forecast_id);

        return forecastValues;
    }
}
