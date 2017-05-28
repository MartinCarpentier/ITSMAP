package com.example.norgaard.barty.Models;

import java.util.ArrayList;

public class Order {

    public int id;
    public ArrayList<Drink> Drinks;

    public double totalPrice() {
        double totalPrice = 0;

        for (int i = 0; i < Drinks.size(); i++) {
            totalPrice += Drinks.get(i).totalPrice();
        }

        return totalPrice;
    }
}
