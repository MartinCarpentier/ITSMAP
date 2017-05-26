package com.example.norgaard.barty.Models;

/**
 * Created by mbc on 16-05-2017.
 */

@org.parceler.Parcel
public class Bar
{
    public String barName;

    public String barlogo;

    public Drinks drinks;

    public long id;

    public Location location;


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

    public long getId() {return id;}

    public void setId(long id){this.id = id;}

}
