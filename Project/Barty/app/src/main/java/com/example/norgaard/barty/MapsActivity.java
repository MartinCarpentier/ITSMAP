package com.example.norgaard.barty;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.norgaard.barty.BarSale.BarSale;
import com.example.norgaard.barty.Data.BartyContract;
import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.Beer;
import com.example.norgaard.barty.Models.Cocktail;
import com.example.norgaard.barty.Models.Drinks;
import com.example.norgaard.barty.Models.Shots;
import com.example.norgaard.barty.Service.BartyService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

import dk.danskebank.mobilepay.sdk.Country;
import dk.danskebank.mobilepay.sdk.MobilePay;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        BarDistanceAdapter.BarDistanceOnClickHandler,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private float DEFAULT_ZOOM = 13;
    private RecyclerView recyclerView;
    private Boolean mLocationPermissionGranted;
    private CameraPosition mCameraPosition;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public BarDistanceAdapter barDistanceAdapter;
    private LinearLayoutManager layoutManager;
    public FirebaseDatabase mFireDb;
    private MapsActivity mapsActivity = this;
    private LatLng mDefaultLocation;
    ArrayList<Bar> bars;
    private boolean barsReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(getString(R.string.key_location));
            mCameraPosition = savedInstanceState.getParcelable(getString(R.string.key_camera_position));
        }
        setContentView(R.layout.activity_maps);
        MobilePay.getInstance().init(getString(R.string.key_mobilepay_test_merchant), Country.DENMARK);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        mDefaultLocation = new LatLng(10, 10);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(getString(R.string.nearby_bars));
            int colorId = ContextCompat.getColor(getApplicationContext(), R.color.cardview_light_background);
            collapsingToolbar.setExpandedTitleColor(colorId);
            collapsingToolbar.setCollapsedTitleTextColor(colorId);

            // This part allows us to drag the google maps in the coordinator layout.
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
            AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(AppBarLayout appBarLayout) {
                    return false;
                }
            });
            params.setBehavior(behavior);
        }

        // Creating an instance of the Google API client
        // Code taken from/inspired by:
        // https://developers.google.com/maps/documentation/android-api/current-place-tutorial
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        recyclerView = (RecyclerView) mapsActivity.findViewById(R.id.recyclerBarDistanceView);
        layoutManager = new LinearLayoutManager(mapsActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        barDistanceAdapter = new BarDistanceAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(barDistanceAdapter);

        Intent intent = new Intent(MapsActivity.this, BartyService.class);
        MapsActivity.this.startService(intent);
        Log.d(MapsActivity.class.toString(), "Started " + BartyService.class.toString());

        startFirebaseDb();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                else {
                    mLocationPermissionGranted = false;
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        updateLocationUI();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        // https://developers.google.com/maps/documentation/android-api/styling
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("asd", "Style parsing failed.");
            }
        }
        catch (Resources.NotFoundException e) {
            Log.e("asd", "Can't find style. Error: ", e);
        }
    }

    // Code taken from/inspired by:
    // https://developers.google.com/maps/documentation/android-api/current-place-tutorial
    private void getDeviceLocation() {

        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }
        else if (mLastLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), DEFAULT_ZOOM));
        }
        else {
            Log.d(MapsActivity.class.toString(), "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    // Code taken from/inspired by:
    // https://developer.android.com/training/location/retrieve-current.html
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    // Code taken from/inspired by:
    // https://developer.android.com/training/location/retrieve-current.html
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // Code taken from/inspired by:
    // https://developer.android.com/training/location/retrieve-current.html
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startFirebaseDb() {

        mFireDb = FirebaseDatabase.getInstance();

        barsReady = false;
        DatabaseReference myRef = mFireDb.getReference().child("Bars");

        Query query = myRef.orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bars = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

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

                    //Setting id
                    long id = (long) child.child("id").getValue();
                    currentBar.setId(id);

                    //Handle beers
                    ArrayList<Beer> beers = new ArrayList<Beer>();
                    for (DataSnapshot jsonBeer : beer.getChildren()) {
                        try {
                            HashMap<String, String> beerMap = (HashMap<String, String>) jsonBeer.getValue();

                            String imagewhat = beerMap.get("ImageURL");
                            String namewhat = beerMap.get("Name");
                            String pricewhat = String.valueOf(beerMap.get("Price"));

                            Beer currentBeer = new Beer(imagewhat, namewhat, Long.valueOf(pricewhat));
                            beers.add(currentBeer);
                        }
                        catch (Exception e) {

                            Log.e("ErrorHappened", e.toString());
                        }
                    }

                    //Handle cocktails
                    ArrayList<Cocktail> cocktails = new ArrayList<Cocktail>();
                    for (DataSnapshot snapshot : cocktail.getChildren()) {
                        try {

                            HashMap<String, String> cocktailMap = (HashMap<String, String>) snapshot.getValue();

                            String imagewhat = cocktailMap.get("ImageURL");
                            String namewhat = cocktailMap.get("Name");
                            String pricewhat = String.valueOf(cocktailMap.get("Price"));

                            Cocktail currentCocktail = new Cocktail(imagewhat, namewhat, Long.valueOf(pricewhat));
                            cocktails.add(currentCocktail);
                        }
                        catch (Exception e) {

                            Log.e("ErrorHappened", e.toString());
                        }
                    }

                    //Handle shots
                    ArrayList<Shots> shots = new ArrayList<Shots>();
                    for (DataSnapshot snapshot : shot.getChildren()) {
                        try {
                            HashMap<String, String> shotMap = (HashMap<String, String>) snapshot.getValue();

                            String imagewhat = shotMap.get("ImageURL");
                            String namewhat = shotMap.get("Name");
                            String pricewhat = String.valueOf(shotMap.get("Price"));

                            Shots currentCocktail = new Shots(imagewhat, namewhat, Long.valueOf(pricewhat));
                            shots.add(currentCocktail);
                        }
                        catch (Exception e) {

                            Log.e("ErrorHappened", e.toString());
                        }
                    }

                    Log.i("OnDataChange", child.getValue().toString());
                    Drinks drinks = new Drinks();
                    drinks.setBeer(beers);
                    drinks.setCocktails(cocktails);
                    drinks.setShots(shots);

                    currentBar.setDrinks(drinks);

                    //Handle location
                    com.example.norgaard.barty.Models.Location currentBarLocation = new com.example.norgaard.barty.Models.Location();
                    try {
                        HashMap<String, String> barLocation = (HashMap<String, String>) location.getValue();

                        String longitude = String.valueOf(barLocation.get("Longitude"));
                        String latitude = String.valueOf(barLocation.get("Latitude"));

                        currentBarLocation.setLatitude(Double.valueOf(latitude));
                        currentBarLocation.setLongitude(Double.valueOf(longitude));
                    }
                    catch (Exception e) {

                        Log.e("ErrorHappened", e.toString());
                    }

                    currentBar.setLocation(currentBarLocation);

                    String currentBarLogo = "";
                    try {
                        currentBarLogo = String.valueOf(barLogo.getValue());
                    }
                    catch (Exception e) {

                        Log.e("ErrorHappened", e.toString());
                    }

                    currentBar.setBarlogo(currentBarLogo);

                    bars.add(currentBar);
                    barsReady = true;
                }
                setBarMarkers(bars);
                insertBarsIntoDatabase(bars);
                barDistanceAdapter.swapData(bars);
                barsReady = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        barsReady = true;
    }

    private void setBarMarkers(ArrayList<Bar> bars) {

        while (!barsReady) {
        }
        LatLng latLng;
        for (Bar bar : bars) {
            if (bar.getLocation().getLongitude() == null || bar.getLocation().getLatitude() == null) {
                continue;
            }
            latLng = new LatLng(bar.getLocation().getLatitude(), bar.getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(bar.barName));
        }
    }

    private void insertBarsIntoDatabase(ArrayList<Bar> bars) {
        ContentValues[] values = Utilities.createContentValuesForWeatherInfos(bars);

        //Insert values into db
        ContentResolver barCuntentResolver = getApplicationContext().getContentResolver();

        barCuntentResolver.bulkInsert(
                BartyContract.BarEntry.CONTENT_URI_BARS,
                values);

        Cursor cursor = barCuntentResolver.query(
                BartyContract.BarEntry.CONTENT_URI_BARS,
                null,
                null,
                null,
                null);

        Log.d("stuff", "asoid");
    }

    public void onLocationChanged(Location location) {
        mLastLocation = location;
        getDeviceLocation();
    }

    // Code taken from/inspired by:
    // https://developers.google.com/maps/documentation/android-api/current-place-tutorial
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastLocation = null;
        }
    }

    @Override
    public void onClick(Bar clickedBar) {
        Log.d("bar was clicked", "Clicked");

        Intent intent = new Intent(this, BarSale.class);

        intent.putExtra("barname_key", Parcels.wrap(clickedBar));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(getString(R.string.key_camera_position), mMap.getCameraPosition());
            outState.putParcelable(getString(R.string.key_location), mLastLocation);
            super.onSaveInstanceState(outState);
        }
    }
}
