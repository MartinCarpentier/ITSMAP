package com.example.norgaard.barty.Models;

import java.util.UUID;

public class OrderDrink {

    public String barName;
    public String state;
    public int quantity;
    public String name;
    public UUID id;

    public OrderDrink(UUID id, String name, int quantity, String barName, String state) {
        this.quantity = quantity;
        this.barName = barName;
        this.state = state;
        this.name = name;
        this.id = id;
    }
}
