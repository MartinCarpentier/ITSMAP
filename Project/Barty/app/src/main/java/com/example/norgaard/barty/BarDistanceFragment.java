package com.example.norgaard.barty;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.norgaard.barty.Adapters.BarDistanceAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BarDistanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BarDistanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarDistanceFragment extends Fragment implements BarDistanceAdapter.BarDistanceOnClickHandler {


    private RecyclerView recyclerView;
    //private ProgressBar progressBar;
    private BarDistanceAdapter barDistanceAdapter;
    private LinearLayoutManager layoutManager;

    public BarDistanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bar_distance, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerForecastView);

        //progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager =
                new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        barDistanceAdapter = new BarDistanceAdapter(rootView.getContext(), this);

        recyclerView.setAdapter(barDistanceAdapter);

        //showLoading();

        //TODO: retrieve data from database
        //getActivity().getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);


        return inflater.inflate(R.layout.fragment_bar_distance, container, false);
    }

    @Override
    public void onClick(long weatherId, ImageView weatherIcon, TextView high, TextView low) {

    }
}
