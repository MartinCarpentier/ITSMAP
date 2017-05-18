package com.example.norgaard.barty.Models;

/**
 * Created by mbc on 16-05-2017.
 */

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Drinks implements Parcelable
{

    @SerializedName("Beer")
    @Expose
    public List<Beer> beer = null;
    @SerializedName("Cocktails")
    @Expose
    public List<Cocktail> cocktails = null;
    @SerializedName("Shots")
    @Expose
    public List<Shots> shots = null;
    public final static Parcelable.Creator<Drinks> CREATOR = new Creator<Drinks>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Drinks createFromParcel(Parcel in) {
            Drinks instance = new Drinks();
            in.readList(instance.beer, (com.example.norgaard.barty.Models.Beer.class.getClassLoader()));
            in.readList(instance.cocktails, (com.example.norgaard.barty.Models.Cocktail.class.getClassLoader()));
            in.readList(instance.shots, (com.example.norgaard.barty.Models.Shots.class.getClassLoader()));
            return instance;
        }

        public Drinks[] newArray(int size) {
            return (new Drinks[size]);
        }

    }
            ;

    public List<Beer> getBeer() {
        return beer;
    }

    public void setBeer(List<Beer> beer) {
        this.beer = beer;
    }

    public List<Cocktail> getCocktails() {
        return cocktails;
    }

    public void setCocktails(List<Cocktail> cocktails) {
        this.cocktails = cocktails;
    }

    public List<Shots> getShots() {
        return shots;
    }

    public void setShots(List<Shots> shots) {
        this.shots = shots;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(beer);
        dest.writeList(cocktails);
        dest.writeList(shots);
    }

    public int describeContents() {
        return 0;
    }

}
