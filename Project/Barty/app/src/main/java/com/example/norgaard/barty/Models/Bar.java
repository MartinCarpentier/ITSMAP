package com.example.norgaard.barty.Models;

/**
 * Created by mbc on 16-05-2017.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bar implements Parcelable
{
    private String barName;

    private String barlogo;

    private Drinks drinks;

    private Location location;
    public final static Parcelable.Creator<Bar> CREATOR = new Creator<Bar>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Bar createFromParcel(Parcel in) {
            Bar instance = new Bar();
            instance.barlogo = ((String) in.readValue((String.class.getClassLoader())));
            instance.drinks = ((Drinks) in.readValue((Drinks.class.getClassLoader())));
            instance.location = ((Location) in.readValue((Location.class.getClassLoader())));
            return instance;
        }

        public Bar[] newArray(int size) {
            return (new Bar[size]);
        }

    }
            ;

    public String getBarlogo() {
        return barlogo;
    }

    public void setBarlogo(String barlogo) {
        this.barlogo = barlogo;
    }

    public String getBarname() {
        return barName;
    }

    public void setBarname(String BarName) {
        this.barName = BarName;
    }

    public Drinks getDrinks() {
        return drinks;
    }

    public void setDrinks(Drinks drinks) {
        this.drinks = drinks;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(barlogo);
        dest.writeValue(drinks);
        dest.writeValue(location);
    }

    public int describeContents() {
        return 0;
    }

}
