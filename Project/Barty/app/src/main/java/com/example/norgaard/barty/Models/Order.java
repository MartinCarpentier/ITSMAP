package com.example.norgaard.barty.Models;

/**
 * Created by marti on 30-05-2017.
 */

public class Order {
    public int barForeignId;
    public String firebaseOrderTag;
    public String orderStatus;
    public double totalPrice;

    public Order(String firebaseOrderTag, int barForeignId, double totalPrice, String orderStatus)
    {
        this.firebaseOrderTag = firebaseOrderTag;
        this.barForeignId = barForeignId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
    }
}
