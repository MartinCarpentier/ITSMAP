package com.example.norgaard.barty.Utilities;

import android.content.ContentValues;

import com.example.norgaard.barty.Database.BartyContract;
import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.DrinkBase;

import java.util.ArrayList;

public class ContentValueCreator {

    public static ContentValues[] createContentValuesForBars(ArrayList<Bar> bars) {
        ContentValues[] contentValues = new ContentValues[bars.size()];

        for (int i = 0; i < bars.size(); i++) {
            ContentValues value = new ContentValues();
            value.put(BartyContract.BarEntry.COLUMN_BAR_NAME, bars.get(i).barName);
            contentValues[i] = value;
        }

        return contentValues;
    }

    public static ContentValues createContentValuesForDrinks(DrinkBase drink, long barId, int drinkQuantity) {
        ContentValues value = new ContentValues();
        value.put(BartyContract.BasketEntry.COLUMN_DRINK_NAME, drink.getName());
        value.put(BartyContract.BasketEntry.COLUMN_DRINK_PRICE, drink.getPrice());
        value.put(BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID, barId);
        value.put(BartyContract.BasketEntry.COLUMN_DRINK_QUANTITY, drinkQuantity);

        return value;
    }

    public static ContentValues createContentValuesForOrders(String firebaseTag, long barId, double totalPrice) {
        ContentValues value = new ContentValues();
        value.put(BartyContract.OrderEntry.COLUMN_ORDER_STATUS_TAG, firebaseTag);
        value.put(BartyContract.OrderEntry.COLUMN_ORDER_PRICE, barId);
        value.put(BartyContract.OrderEntry.COLUMN_FOREIGN_BAR_ID, barId);

        return value;
    }
}
