package com.example.norgaard.barty.Models;

/**
 * Created by mbc on 16-05-2017.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class Beer extends DrinkBase
{
    public String imageURL;
    public String name;
    public long price;

    public Beer()
    {

    }

    public Beer(String ImageURL, String Name, long Price)
    {
        imageURL = ImageURL;
        name = Name;
        price = Price;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

}
