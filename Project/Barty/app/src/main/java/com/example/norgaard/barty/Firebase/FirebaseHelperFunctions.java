package com.example.norgaard.barty.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.norgaard.barty.Models.Beer;
import com.example.norgaard.barty.Models.Cocktail;
import com.example.norgaard.barty.Models.Shots;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mbc on 31-05-2017.
 */

public class FirebaseHelperFunctions {
    public String getBarLogo(DataSnapshot barLogo) {
        String currentBarLogo = "";
        try {
            currentBarLogo = String.valueOf(barLogo.getValue());
        }
        catch (Exception e) {

            Log.e("ErrorHappened", e.toString());
        }
        return currentBarLogo;
    }

    @NonNull
    public com.example.norgaard.barty.Models.Location getLocation(DataSnapshot location) {
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
        return currentBarLocation;
    }

    @NonNull
    public ArrayList<Shots> getShots(DataSnapshot shot) {
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
        return shots;
    }

    @NonNull
    public ArrayList<Cocktail> getCocktails(DataSnapshot cocktail) {
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
        return cocktails;
    }

    @NonNull
    public ArrayList<Beer> getBeers(DataSnapshot beer) {
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
        return beers;
    }
}
