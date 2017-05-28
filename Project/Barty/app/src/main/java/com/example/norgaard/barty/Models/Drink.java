package com.example.norgaard.barty.Models;

class Drink {

    public double price;
    public int quantity;

    public double totalPrice() {
        return price * quantity;
    }
}
