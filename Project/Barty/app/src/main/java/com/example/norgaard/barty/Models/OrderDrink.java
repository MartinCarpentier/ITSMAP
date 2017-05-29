package com.example.norgaard.barty.Models;

import java.util.UUID;

public class OrderDrink {

    public String barName;
    public String state;
    public int quantity;
    public String name;
    public String id;

    public OrderDrink(UUID id, String name, int quantity, String barName, String state) {
        this.id = id.toString().substring(0,id.toString().length()/2);
        this.quantity = quantity;
        this.barName = barName;
        this.state = state;
        this.name = name;
    }
}
