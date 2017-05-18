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

        View rootView = inflater.inflate(R.layout.fragment_bar_distance, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerBarDistanceView);

        //progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerView.setVisibility(View.VISIBLE);

        barDistanceAdapter = new BarDistanceAdapter(rootView.getContext(), this);

        recyclerView.setAdapter(barDistanceAdapter);

        //showLoading();

        //TODO: retrieve data from database
        //getActivity().getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);

        startFirebaseDb();

        return inflater.inflate(R.layout.fragment_bar_distance, container, false);
    }

    private void startFirebaseDb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Bars");
        Query query = myRef.orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Bar> bars = new ArrayList<Bar>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    Bar currentBar = new Bar();

                    //TODO: FIX SHITTTY HACK -> Shit failed, so this is a hack to make it work
                    DataSnapshot drink = child.child("Drinks");
                    DataSnapshot beer = drink.child("Beer");
                    DataSnapshot cocktail = drink.child("Cocktails");
                    DataSnapshot shot = drink.child("Shots");
                    DataSnapshot location = child.child("Location");
                    DataSnapshot barLogo = child.child("Barlogo");

                    String barname = child.getKey().toString();
                    currentBar.setBarname(barname);

                    //Handle beers
                    ArrayList<Beer> beers = new ArrayList<Beer>();
                    for(DataSnapshot jsonBeer: beer.getChildren())
                    {
                        try
                        {
                            //JSONObject currentBeer = new JSONObject(jsonBeer.getValue().toString());
                            //Beer stuff = new Gson().fromJson(jsonBeer.getValue().toString(), Beer.class);
                            //Beer stuff = jsonBeer.getValue(Beer.class);

                            HashMap<String, String> beerMap = (HashMap<String, String>)jsonBeer.getValue();

                            Log.i("Stuff", "Stuff");

                            String imagewhat = beerMap.get("ImageURL");
                            String namewhat = beerMap.get("Name");
                            String pricewhat = String.valueOf(beerMap.get("Price"));

                            Beer currentBeer = new Beer(imagewhat, namewhat, Long.valueOf(pricewhat));
                            beers.add(currentBeer);
                        }
                        catch (Exception e)
                        {
                            Log.e("ErrorHappened", e.toString());
                        }
                    }

                    //Handle cocktails
                    ArrayList<Cocktail> cocktails = new ArrayList<Cocktail>();
                    for(DataSnapshot snapshot: cocktail.getChildren())
                    {
                        try
                        {
                            //JSONObject currentBeer = new JSONObject(jsonBeer.getValue().toString());
                            //Beer stuff = new Gson().fromJson(jsonBeer.getValue().toString(), Beer.class);
                            //Beer stuff = jsonBeer.getValue(Beer.class);

                            HashMap<String, String> cocktailMap = (HashMap<String, String>)snapshot.getValue();

                            Log.i("Stuff", "Stuff");

                            String imagewhat = cocktailMap.get("ImageURL");
                            String namewhat = cocktailMap.get("Name");
                            String pricewhat = String.valueOf(cocktailMap.get("Price"));

                            Cocktail currentCocktail = new Cocktail(imagewhat, namewhat, Long.valueOf(pricewhat));
                            cocktails.add(currentCocktail);
                        }
                        catch (Exception e)
                        {
                            Log.e("ErrorHappened", e.toString());
                        }
                    }

                    //Handle shots
                    ArrayList<Shots> shots = new ArrayList<Shots>();
                    for(DataSnapshot snapshot: shot.getChildren())
                    {
                        try
                        {
                            //JSONObject currentBeer = new JSONObject(jsonBeer.getValue().toString());
                            //Beer stuff = new Gson().fromJson(jsonBeer.getValue().toString(), Beer.class);
                            //Beer stuff = jsonBeer.getValue(Beer.class);

                            HashMap<String, String> shotMap = (HashMap<String, String>)snapshot.getValue();

                            Log.i("Stuff", "Stuff");

                            String imagewhat = shotMap.get("ImageURL");
                            String namewhat = shotMap.get("Name");
                            String pricewhat = String.valueOf(shotMap.get("Price"));

                            Shots currentCocktail = new Shots(imagewhat, namewhat, Long.valueOf(pricewhat));
                            shots.add(currentCocktail);
                        }
                        catch (Exception e)
                        {
                            Log.e("ErrorHappened", e.toString());
                        }
                    }


                    //Bar currentBar = new Gson().fromJson(child.getValue().toString(), Bar.class);
                    //Bar currentBar2 = child.getValue(Bar.class);

                    Log.i("OnDataChange", child.getValue().toString());
                    Drinks drinks = new Drinks();
                    drinks.setBeer(beers);
                    drinks.setCocktails(cocktails);
                    drinks.setShots(shots);

                    currentBar.setDrinks(drinks);

                    //Handle location
                    Location currentBarLocation = new Location();
                    try
                    {
                        HashMap<String, String> barLocation = (HashMap<String, String>)location.getValue();

                        Log.i("Stuff", "Stuff");

                        String longitude = String.valueOf(barLocation.get("Longitude"));
                        String latitude = String.valueOf(barLocation.get("Latitude"));

                        currentBarLocation.setLatitude(Double.valueOf(latitude));
                        currentBarLocation.setLongitude(Double.valueOf(longitude));
                    }
                    catch (Exception e)
                    {
                        Log.e("ErrorHappened", e.toString());
                    }

                    currentBar.setLocation(currentBarLocation);

                    String currentBarLogo = "";
                    try
                    {
                        currentBarLogo = String.valueOf(barLogo.getValue());

                        Log.i("Stuff", "Stuff");
                    }
                    catch (Exception e)
                    {
                        Log.e("ErrorHappened", e.toString());
                    }

                    currentBar.setBarlogo(currentBarLogo);

                    bars.add(currentBar);
                }

                barDistanceAdapter.swapData(bars);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(long weatherId, ImageView weatherIcon, TextView high, TextView low) {

    }


}
