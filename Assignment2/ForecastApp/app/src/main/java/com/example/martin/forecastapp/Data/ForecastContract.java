package com.example.martin.forecastapp.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Created by mbc on 06-05-2017.
 */

public class ForecastContract {
    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static final String CONTENT_AUTHORITY = "com.example.martin.forecastapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER = "weather";

    public static class ForecastEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon().appendPath(PATH_WEATHER).build();

        public static final String TABLE_NAME = "forecast";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_FORECAST_ID = "forecast_id";

        public static final String COLUMN_SHORT_DESCRIPTION = "short_desc";

        public static final String COLUMN_TEMP_MAX = "max";
        public static final String COLUMN_TEMP_MIN = "min";

        public static final String COLUMN_HUMIDITY = "humidity";

        public static final String COLUMN_PRESSURE = "pressure";

        public static final String COLUMN_WIND_SPEED = "wind";

        public static final String COLUMN_DEGREES = "degrees";

        public static Uri buildSpecificForecastUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = normalizeDate(System.currentTimeMillis());
            return ForecastContract.ForecastEntry.COLUMN_DATE + " >= " + normalizedUtcNow;
        }

        public static long normalizeDate(long date) {
            long daysSinceEpoch = elapsedDaysSinceEpoch(date);
            long millisFromEpochToTodayAtMidnightUtc = daysSinceEpoch * DAY_IN_MILLIS;
            return millisFromEpochToTodayAtMidnightUtc;
        }

        private static long elapsedDaysSinceEpoch(long utcDate) {
            return TimeUnit.MILLISECONDS.toDays(utcDate);
        }
    }
}
