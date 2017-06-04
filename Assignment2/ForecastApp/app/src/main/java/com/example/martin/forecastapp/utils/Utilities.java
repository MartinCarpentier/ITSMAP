package com.example.martin.forecastapp.utils;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.example.martin.forecastapp.data.ForecastContract;
import com.example.martin.forecastapp.models.CurrentWeather;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class Utilities {

    private final static String weatherBaseUrl = "http://api.openweathermap.org/data/2.5/weather";

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";

    private static final String api_key = "9a5a49796eeb66ff23a2a826c6e9fb87";
    /* The number of days we want our API to return */

    /* The query parameter allows us to provide a location string to the API */
    private static final String QUERY_PARAM = "q";

    private static final int c = '\u2202';
    private static final String city = "Aarhus.Dk";

    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    private static final String FORMAT_PARAM = "mode";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";

    private static final String API_KEY_PARAM = "APPID";

    public static URL createWeatherUrlForAarhus() {
        //Add the days parameter, if you want a specific amount of days
        Uri weatherQueryUri = Uri.parse(weatherBaseUrl).buildUpon()
                .appendQueryParameter(QUERY_PARAM, city)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v("WeatherService", "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ContentValues[] createContentValuesForCurrentWeather(CurrentWeather weather) {
        ContentValues[] values = new ContentValues[1];
        ContentValues forecastValues = new ContentValues();

        long normalizedDate;
        if (!SunshineDateUtils.isDateNormalized(weather.getDt())) {
            normalizedDate = ((long) weather.getDt()) * 1000;
        }
        else {
            normalizedDate = (long) weather.getDt();
        }

        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_DATE, normalizedDate);
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_HUMIDITY, weather.getMain().getHumidity());
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_PRESSURE, weather.getMain().getPressure());
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED, weather.getWind().getSpeed());
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_DEGREES, weather.getWind().getDeg());
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_TEMP_MAX, weather.getMain().getTempMax());
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_TEMP_MIN, weather.getMain().getTempMin());
        forecastValues.put(ForecastContract.ForecastEntry.COLUMN_FORECAST_ID, weather.getWeather().get(0).getId());

        values[0] = forecastValues;

        return values;
    }

    public static boolean checkIfDay(long dateAsLong) {

        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(dateAsLong);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        return !(hour > 22 || hour < 7);
    }
}
