package com.example.norgaard.barty.Models;

@org.parceler.Parcel
public class Shots extends DrinkBase {

    public String imageURL;
    public String name;
    public long price;

    public Shots() {

    }

    public Shots(String ImageURL, String Name, long Price) {
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
