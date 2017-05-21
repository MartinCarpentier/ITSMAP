package com.example.norgaard.barty.Models;

/**
 * Created by marti on 19-05-2017.
 */

public class DrinkBase {
    public String imageURL;

    public String name;

    public long price;

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
