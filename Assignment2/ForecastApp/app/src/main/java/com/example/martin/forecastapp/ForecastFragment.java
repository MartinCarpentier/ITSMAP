package com.example.martin.forecastapp;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Created by mbc on 05-05-2017.
 */

public class ForecastFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ForecastAdapter.ForecastAdapterOnClickHandler{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ForecastAdapter forecastAdapter;

    private int mPosition = RecyclerView.NO_POSITION;

    private static final int ID_FORECAST_LOADER = 44;

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

        LinearLayoutManager layoutManager =
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
    public void onClick(long date) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri myUri = Uri.parse("http://www.google.com");

        return new CursorLoader(getActivity(),
                myUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        String[] dataString = {"Hello1", "Hello2", "Hello3", "Hello4"};

        //forecastAdapter.swapCursor(data);
        forecastAdapter.mockCursor(dataString);

        stopLoading();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        forecastAdapter.swapCursor(null);
    }
}
