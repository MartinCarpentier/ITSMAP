package com.example.norgaard.barty.Activities.Maps;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
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

import com.example.norgaard.barty.Activities.Catalog.CatalogActivity;
import com.example.norgaard.barty.Database.BartyContract;
import com.example.norgaard.barty.Firebase.FirebaseHelperFunctions;
import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.Beer;
import com.example.norgaard.barty.Models.Cocktail;
import com.example.norgaard.barty.Models.Drinks;
import com.example.norgaard.barty.Models.Shots;
import com.example.norgaard.barty.R;
import com.example.norgaard.barty.Service.BartyService;
import com.example.norgaard.barty.Utilities.ContentValueCreator;
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
        MapsAdapter.BarDistanceOnClickHandler,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    public static final String LOG_MAPS = "LOG MAPS";
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private Location location;
    private float DEFAULT_ZOOM = 13;
    private RecyclerView recyclerView;
    private Boolean isLocationPermissionGranted = false;
    private CameraPosition cameraPosition;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public MapsAdapter mapsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseHelperFunctions firebaseHelperFunctions;
    public FirebaseDatabase firebaseDatabase;
    private MapsActivity mapsActivity = this;
    private LatLng latLng;
    ArrayList<Bar> bars;
    private boolean isBarsReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_MAPS, "onCreate()");

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            location = savedInstanceState.getParcelable(getString(R.string.key_location));
            cameraPosition = savedInstanceState.getParcelable(getString(R.string.key_camera_position));
        }

        firebaseHelperFunctions = new FirebaseHelperFunctions();
        setContentView(R.layout.activity_maps);
        MobilePay.getInstance().init(getString(R.string.key_mobilepay_test_merchant), Country.DENMARK);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        latLng = new LatLng(56.154662, 10.205894);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(getString(R.string.nearby_bars));
            int colorId = ContextCompat.getColor(getApplicationContext(), R.color.cardview_light_background);
            collapsingToolbar.setExpandedTitleColor(colorId);
            collapsingToolbar.setCollapsedTitleTextColor(colorId);

            // This part allows us to drag the google maps in the coordinator layout.
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
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
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        recyclerView = (RecyclerView) mapsActivity.findViewById(R.id.recyclerBarDistanceView);
        linearLayoutManager = new LinearLayoutManager(mapsActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mapsAdapter = new MapsAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(mapsAdapter);

        Intent intent = new Intent(MapsActivity.this, BartyService.class);
        startService(intent);
        //MapsActivity.this.startService(new Intent(".Service.BartyService"));
        Log.d(LOG_MAPS, "Started " + BartyService.class.toString());

        startFirebaseDb();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(LOG_MAPS, "onRequestPermissionsResult()");

        switch (requestCode) {
            case BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationPermissionGranted = true;
                    updateLocationUI();
                }
                else {
                    isLocationPermissionGranted = false;
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
        Log.d(LOG_MAPS, "onMapReady()");

        this.googleMap = googleMap;

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
                Log.e(LOG_MAPS, "Style parsing failed.");
            }
        }
        catch (Resources.NotFoundException e) {
            Log.e(LOG_MAPS, "Can't find style. Error: ", e);
        }
    }

    // Code taken from/inspired by:
    // https://developers.google.com/maps/documentation/android-api/current-place-tutorial
    private void getDeviceLocation() {
        Log.d(LOG_MAPS, "getDeviceLocation()");

        if (cameraPosition != null) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else if (location != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
        }
        else {
            Log.d(LOG_MAPS, "Current location is null. Using defaults.");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    // Code taken from/inspired by:
    // https://developer.android.com/training/location/retrieve-current.html
    @Override
    protected void onStart() {
        Log.d(LOG_MAPS, "onStart()");

        googleApiClient.connect();
        super.onStart();
    }

    // Code taken from/inspired by:
    // https://developer.android.com/training/location/retrieve-current.html
    @Override
    protected void onStop() {
        Log.d(LOG_MAPS, "onStop()");

        googleApiClient.disconnect();
        super.onStop();
    }

    // Code taken from/inspired by:
    // https://developer.android.com/training/location/retrieve-current.html
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_MAPS, "onConnected()");

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (location != null) {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                onMapReady(googleMap);
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_MAPS, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_MAPS, "onConnectionFailed()");
    }

    private void startFirebaseDb() {
        Log.d(LOG_MAPS, "startFirebaseDb()");

        firebaseDatabase = FirebaseDatabase.getInstance();

        isBarsReady = false;
        DatabaseReference myRef = firebaseDatabase.getReference().child("Bars");

        Query query = myRef.orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bars = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Bar currentBar = new Bar();

                    //TODO: Not the prettiest solution, but the automatic firebase conversion didn't work
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
                    ArrayList<Beer> beers = firebaseHelperFunctions.getBeers(beer);

                    //Handle cocktails
                    ArrayList<Cocktail> cocktails = firebaseHelperFunctions.getCocktails(cocktail);

                    //Handle shots
                    ArrayList<Shots> shots = firebaseHelperFunctions.getShots(shot);

                    Drinks drinks = new Drinks();
                    drinks.setBeer(beers);
                    drinks.setCocktails(cocktails);
                    drinks.setShots(shots);

                    currentBar.setDrinks(drinks);

                    //Handle location
                    com.example.norgaard.barty.Models.Location currentBarLocation = firebaseHelperFunctions.getLocation(location);

                    currentBar.setLocation(currentBarLocation);

                    String currentBarLogo = firebaseHelperFunctions.getBarLogo(barLogo);

                    currentBar.setBarlogo(currentBarLogo);

                    bars.add(currentBar);
                    isBarsReady = true;
                }
                setBarMarkers(bars);
                insertBarsIntoDatabase(bars);
                mapsAdapter.swapData(bars);
                isBarsReady = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(LOG_MAPS, "Couldn't retrieve firebase data");
            }
        });

    }



    private void setBarMarkers(ArrayList<Bar> bars) {
        Log.d(LOG_MAPS, "setBarMarkers()");

        while (!isBarsReady) {
        }
        LatLng latLng;
        for (Bar bar : bars) {
            if (bar.getLocation().getLongitude() == null || bar.getLocation().getLatitude() == null) {
                continue;
            }
            latLng = new LatLng(bar.getLocation().getLatitude(), bar.getLocation().getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(bar.barName));
        }
    }

    private void insertBarsIntoDatabase(ArrayList<Bar> bars) {
        Log.d(LOG_MAPS, "insertBarsIntoDatabase()");

        ContentValues[] values = ContentValueCreator.createContentValuesForBars(bars);

        //Insert values into db
        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        contentResolver.bulkInsert(
                BartyContract.BarEntry.CONTENT_URI_BARS,
                values);
    }

    public void onLocationChanged(Location location) {
        Log.d(LOG_MAPS, "onLocationChanged()");

        this.location = location;
        getDeviceLocation();
    }

    // Code taken from/inspired by:
    // https://developers.google.com/maps/documentation/android-api/current-place-tutorial
    private void updateLocationUI() {
        Log.d(LOG_MAPS, "updateLocationUI()");

        if (googleMap == null) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, BARTY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        else {
            isLocationPermissionGranted = true;
        }
        if (isLocationPermissionGranted) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            location = null;
        }
    }

    @Override
    public void onClick(Bar clickedBar) {
        Log.d(LOG_MAPS, "Bar clicked: " + clickedBar.barName);

        Intent intent = new Intent(this, CatalogActivity.class);

        intent.putExtra("barname_key", Parcels.wrap(clickedBar));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_MAPS, "onSaveInstanceState()");

        if (googleMap != null) {
            outState.putParcelable(getString(R.string.key_camera_position), googleMap.getCameraPosition());
            outState.putParcelable(getString(R.string.key_location), location);
            super.onSaveInstanceState(outState);
        }
    }
}
