package com.example.norgaard.barty.Models;

/**
 * Created by mbc on 16-05-2017.
 */

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class Drinks
{
    public ArrayList<Beer> beer;

    public ArrayList<Cocktail> cocktails;

    public ArrayList<Shots> shots;

    public Drinks(){}

    public ArrayList<Beer> getBeer() {
        return beer;
    }

    public void setBeer(ArrayList<Beer> beer) {
        this.beer = beer;
    }

    public ArrayList<Cocktail> getCocktails() {
        return cocktails;
    }

    public void setCocktails(ArrayList<Cocktail> cocktails) {
        this.cocktails = cocktails;
    }

    public List<Shots> getShots() {
        return shots;
    }

    public void setShots(ArrayList<Shots> shots) {
        this.shots = shots;
    }

}
