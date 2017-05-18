package com.example.norgaard.barty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.Beer;
import com.example.norgaard.barty.Models.Cocktail;
import com.example.norgaard.barty.Models.Drinks;
import com.example.norgaard.barty.Models.Location;
import com.example.norgaard.barty.Models.Shots;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BarDistanceFragment extends Fragment implements BarDistanceAdapter.BarDistanceOnClickHandler {


    private RecyclerView recyclerView;
    //private ProgressBar progressBar;
    public BarDistanceAdapter barDistanceAdapter;
    private LinearLayoutManager layoutManager;
    private BarDistanceFragment thisFragment = this;
    private View mRootView;

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

        View rootView = inflater.inflate(R.layout.fragment_bar_distance, container, false);

        //final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //rootView.setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerBarDistanceView);

        //progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerView.setVisibility(View.INVISIBLE);

        barDistanceAdapter = new BarDistanceAdapter(rootView.getContext(), this);

        recyclerView.setAdapter(barDistanceAdapter);

        mRootView = rootView;
        //showLoading();

        //TODO: retrieve data from database


        return inflater.inflate(R.layout.fragment_bar_distance, container, false);
    }


    @Override
    public void onClick(long weatherId, ImageView weatherIcon, TextView high, TextView low) {

    }


}
