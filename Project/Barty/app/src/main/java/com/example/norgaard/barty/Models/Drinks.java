package com.example.norgaard.barty.Models;

import java.util.ArrayList;
import java.util.List;

@org.parceler.Parcel
public class Drinks {

    public ArrayList<Beer> beer;
    public ArrayList<Cocktail> cocktails;
    public ArrayList<Shots> shots;

    public Drinks() {
    }

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
