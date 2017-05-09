package com.example.martin.forecastapp.utils;

import android.content.ContentValues;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.martin.forecastapp.data.ForecastContract;
import com.example.martin.forecastapp.models.WeatherInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mbc on 08-05-2017.
 */

public class Utilities {

    private final static String weatherBaseUrl =  "http://api.openweathermap.org/data/2.5/forecast";

    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";

    private static final String api_key = "9a5a49796eeb66ff23a2a826c6e9fb87";
    /* The number of days we want our API to return */
    private static final int numDays = 14;

    /* The query parameter allows us to provide a location string to the API */
    private static final String QUERY_PARAM = "q";

    private static final int c = '\u2202';
    private static final String city = "Aarhus.Dk";

    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    private static final String FORMAT_PARAM = "mode";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";
    /* The days parameter allows us to designate how many days of weather data we want */
    private static final String DAYS_PARAM = "cnt";

    private static final String API_KEY_PARAM = "APPID";

    public static URL createWeatherUrlForAarhus()
    {
        //Add the days parameter, if you want a specific amount of days
        Uri weatherQueryUri = Uri.parse(weatherBaseUrl).buildUpon()
                .appendQueryParameter(QUERY_PARAM, city)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                //.appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v("WeatherService", "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ContentValues[] createContentValuesForWeatherInfos(List<WeatherInfo> weatherInfo) {

        ContentValues[] values = new ContentValues[weatherInfo.size()];

        if (weatherInfo != null) {
            for (int i=0;i<weatherInfo.size();i++){

                ContentValues forecastValues = new ContentValues();

                Calendar cal = Calendar.getInstance();
                //cal.setTime(new Date(weatherInfo.get(i).getDtTxt()));

                long normalizedDate;
                if(!SunshineDateUtils.isDateNormalized(weatherInfo.get(i).getDt()))
                {
                    normalizedDate = ((long)weatherInfo.get(i).getDt())*1000;
                }
                else
                {
                    normalizedDate = (long)weatherInfo.get(i).getDt();
                }

                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_DATE, normalizedDate);
                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_HUMIDITY, weatherInfo.get(i).getMain().getHumidity());
                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_PRESSURE, weatherInfo.get(i).getMain().getPressure());
                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_WIND_SPEED, weatherInfo.get(i).getWind().getSpeed());
                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_DEGREES, weatherInfo.get(i).getWind().getDeg());
                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_TEMP_MAX, weatherInfo.get(i).getMain().getTempMax());
                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_TEMP_MIN, weatherInfo.get(i).getMain().getTempMin());
                forecastValues.put(ForecastContract.ForecastEntry.COLUMN_FORECAST_ID, weatherInfo.get(i).getWeather().get(0).getId());

                values[i] = forecastValues;
            }
        }

        return values;
    }


    public static boolean checkIfDay(long dateAsLong) {

        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(dateAsLong);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(hour > 22 || hour < 7)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
