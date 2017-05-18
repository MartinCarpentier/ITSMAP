package com.example.norgaard.barty;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.Beer;
import com.example.norgaard.barty.Models.Cocktail;
import com.example.norgaard.barty.Models.Drinks;
import com.example.norgaard.barty.Models.Shots;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, BarDistanceAdapter.BarDistanceOnClickHandler {

    private static final int BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private RecyclerView recyclerView;
    //private ProgressBar progressBar;
    public BarDistanceAdapter barDistanceAdapter;
    private LinearLayoutManager layoutManager;
    private MapsActivity mapsActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Nearby bars");

        checkForPermissions();



        //Creating an instance of the Google API client
        //Code taken from:
        //https://developer.android.com/training/location/retrieve-current.html
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation == null) {
                return;
            }

            LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        }
    }


    //Code From
    //https://developer.android.com/training/permissions/requesting.html
    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // BARTY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }

    //Code from:
    //https://developer.android.com/training/location/retrieve-current.html
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    //Code from:
    //https://developer.android.com/training/location/retrieve-current.html
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //Code from:
    //https://developer.android.com/training/location/retrieve-current.html
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                onMapReady(mMap);
            }
        }

        recyclerView = (RecyclerView)mapsActivity.findViewById(R.id.recyclerBarDistanceView);

        //progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager = new LinearLayoutManager(mapsActivity);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        barDistanceAdapter = new BarDistanceAdapter(getApplicationContext(), this);

        recyclerView.setAdapter(barDistanceAdapter);

        startFirebaseDb();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(long weatherId, ImageView weatherIcon, TextView high, TextView low) {

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
                    com.example.norgaard.barty.Models.Location currentBarLocation = new com.example.norgaard.barty.Models.Location();
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


                barDistanceAdapter = new BarDistanceAdapter(getApplicationContext(), mapsActivity);
                barDistanceAdapter.swapData(bars);
                recyclerView.setAdapter(barDistanceAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
