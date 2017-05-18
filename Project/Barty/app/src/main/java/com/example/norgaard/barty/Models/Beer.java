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

@IgnoreExtraProperties
public class Beer implements Parcelable
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

    public final static Parcelable.Creator<Beer> CREATOR = new Creator<Beer>() {



        @SuppressWarnings({
                "unchecked"
        })
        public Beer createFromParcel(Parcel in) {
            Beer instance = new Beer();
            //instance.imageURL = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.price = ((long) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Beer[] newArray(int size) {
            return (new Beer[size]);
        }

    }
            ;

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(imageURL);
        dest.writeValue(name);
        dest.writeValue(price);
    }

    public int describeContents() {
        return 0;
    }

}