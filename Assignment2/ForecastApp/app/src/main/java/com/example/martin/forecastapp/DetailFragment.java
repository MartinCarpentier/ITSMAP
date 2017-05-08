package com.example.martin.forecastapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.martin.forecastapp.Data.ForecastContract;

public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String DETAIL_URI = "URI";
    private Uri weatherContentProviderUri;

    private static final String[] DETAIL_PROJECTS = {
            ForecastContract.ForecastEntry.TABLE_NAME + "." + ForecastContract.ForecastEntry._ID,
            ForecastContract.ForecastEntry.COLUMN_DATE,
            ForecastContract.ForecastEntry.COLUMN_SHORT_DESCRIPTION,
            ForecastContract.ForecastEntry.COLUMN_TEMP_MAX,
            ForecastContract.ForecastEntry.COLUMN_TEMP_MIN,
            ForecastContract.ForecastEntry.COLUMN_HUMIDITY,
            ForecastContract.ForecastEntry.COLUMN_PRESSURE,
            ForecastContract.ForecastEntry.COLUMN_WIND_SPEED,
            ForecastContract.ForecastEntry.COLUMN_DEGREES,
            ForecastContract.ForecastEntry.COLUMN_FORECAST_ID,
    };

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            weatherContentProviderUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Inflate the layout for this fragment
        return rootView;
    }
}
