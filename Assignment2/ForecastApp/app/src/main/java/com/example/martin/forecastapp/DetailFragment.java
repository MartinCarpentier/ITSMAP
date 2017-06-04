package com.example.martin.forecastapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.forecastapp.data.ForecastContract;
import com.example.martin.forecastapp.utils.SunshineDateUtils;
import com.example.martin.forecastapp.utils.Utilities;
import com.example.martin.forecastapp.utils.WeatherIdUtils;

public class DetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String DETAIL_URI = "URI";
    private Uri weatherContentProviderUri;

    private static final int ID_DETAIL_LOADER = 44;

    private static final String[] DETAIL_PROJECTS = {
            ForecastContract.ForecastEntry._ID,
            ForecastContract.ForecastEntry.COLUMN_DATE,
            ForecastContract.ForecastEntry.COLUMN_TEMP_MAX,
            ForecastContract.ForecastEntry.COLUMN_TEMP_MIN,
            ForecastContract.ForecastEntry.COLUMN_HUMIDITY,
            ForecastContract.ForecastEntry.COLUMN_PRESSURE,
            ForecastContract.ForecastEntry.COLUMN_WIND_SPEED,
            ForecastContract.ForecastEntry.COLUMN_DEGREES,
            ForecastContract.ForecastEntry.COLUMN_FORECAST_ID
    };

    public static final int INDEX_WEATHER_ID = 0;
    public static final int INDEX_WEATHER_DATE = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 2;
    public static final int INDEX_WEATHER_MIN_TEMP = 3;
    public static final int INDEX_WEATHER_HUMIDITY = 4;
    public static final int INDEX_WEATHER_PRESSURE = 5;
    public static final int INDEX_WEATHER_WINDSPEED = 6;
    public static final int INDEX_WEATHER_DEGREES = 7;
    public static final int INDEX_WEATHER_FORECAST_ID = 8;

    public DetailFragment() {
        // Required empty public constructor
    }

    private ImageView weatherIconView;
    private TextView shortDescriptionTextView;
    private TextView highTextView;
    private TextView minTextView;
    private TextView dateTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private TextView windSpeedTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            weatherContentProviderUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        highTextView = (TextView) rootView.findViewById(R.id.highTemperatureText);
        minTextView = (TextView) rootView.findViewById(R.id.lowTextView);
        dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        humidityTextView = (TextView) rootView.findViewById(R.id.currentHumidityText);
        pressureTextView = (TextView) rootView.findViewById(R.id.currentPressureTextView);
        windSpeedTextView = (TextView) rootView.findViewById(R.id.currentWindTextView);
        weatherIconView = (ImageView) rootView.findViewById(R.id.imageView2);
        shortDescriptionTextView = (TextView) rootView.findViewById(R.id.shortDescriptionText);

        getActivity().getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_DETAIL_LOADER:
                return new CursorLoader(getContext(),
                        weatherContentProviderUri,
                        DETAIL_PROJECTS,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        double max = data.getDouble(DetailFragment.INDEX_WEATHER_MAX_TEMP);
        double min = data.getDouble(DetailFragment.INDEX_WEATHER_MIN_TEMP);
        long dateAsLong = data.getLong(DetailFragment.INDEX_WEATHER_DATE);
        double humidity = data.getDouble(DetailFragment.INDEX_WEATHER_HUMIDITY);
        double pressure = data.getDouble(DetailFragment.INDEX_WEATHER_PRESSURE);
        double wind = data.getDouble(DetailFragment.INDEX_WEATHER_WINDSPEED);
        int weatherId = data.getInt(DetailFragment.INDEX_WEATHER_FORECAST_ID);

        String date = SunshineDateUtils.getFriendlyDateString(getActivity(), dateAsLong, true);

        highTextView.setText(String.valueOf(max));
        minTextView.setText(String.valueOf(min));
        dateTextView.setText(date);
        humidityTextView.setText(String.valueOf(humidity));
        pressureTextView.setText(String.valueOf(pressure));
        windSpeedTextView.setText(String.valueOf(wind));

        boolean isDay = Utilities.checkIfDay(dateAsLong);
        int imageId = WeatherIdUtils.getDrawableIdForWeatherCondition(weatherId, isDay);
        weatherIconView.setImageDrawable(ContextCompat.getDrawable(getActivity(), imageId));

        String description = WeatherIdUtils.getStringForWeatherCondition(getActivity(), weatherId);
        shortDescriptionTextView.setText(description);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
