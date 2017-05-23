package com.example.martin.forecastapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martin.forecastapp.data.ForecastContract;

/**
 * Created by mbc on 05-05-2017.
 */

public class ForecastFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ForecastAdapter.ForecastAdapterOnClickHandler{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ForecastAdapter forecastAdapter;
    private LinearLayoutManager layoutManager;

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int COLUMN_FORECAST_ID = 3;

    private int mPosition = RecyclerView.NO_POSITION;

    private static final int ID_FORECAST_LOADER = 44;

    public static final String[] MAIN_FORECAST_PROJECTION = {
            ForecastContract.ForecastEntry.COLUMN_DATE,
            ForecastContract.ForecastEntry.COLUMN_TEMP_MAX,
            ForecastContract.ForecastEntry.COLUMN_TEMP_MIN,
            ForecastContract.ForecastEntry.COLUMN_FORECAST_ID
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerForecastView);

        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager =
                new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        forecastAdapter = new ForecastAdapter(rootView.getContext(), this);

        recyclerView.setAdapter(forecastAdapter);

        showLoading();

        //TODO: retrieve data from database
        getActivity().getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);

        return rootView;
    }

    private void stopLoading()
    {
        progressBar.setVisibility(View.INVISIBLE);

        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(long date, ImageView weatherIcon, TextView high, TextView low) {
        //Either start activity with date or show in this activity

        if(MainActivity.mTwoPane)
        {

        }
        else
        {
            Intent weatherDetailIntent = new Intent(getActivity(), DetailActivity.class);
            Uri uriForWeatherId = ForecastContract.ForecastEntry.buildSpecificForecastUri(date);
            weatherDetailIntent.setData(uriForWeatherId);

            Pair<View, String> p1 = Pair.create((View)weatherIcon, getString(R.string.weatherIconTransitionName));
            Pair<View, String> p2 = Pair.create((View)high, getString(R.string.highTemperatureTransitionName));
            Pair<View, String> p3 = Pair.create((View)low, getString(R.string.lowTemperatureTransitionName));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), p1, p2, p3);

            //android:transitionName="highTemperatureTransition"
            startActivity(weatherDetailIntent, options.toBundle());
        }
}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_FORECAST_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = ForecastContract.ForecastEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = ForecastContract.ForecastEntry.COLUMN_DATE + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                String selection = ForecastContract.ForecastEntry.getSqlSelectForNowOnwards();

                return new CursorLoader(getContext(),
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        selection,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //forecastAdapter.swapCursor(data);
        forecastAdapter.swapCursor(data);

        stopLoading();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        forecastAdapter.swapCursor(null);
    }
}
