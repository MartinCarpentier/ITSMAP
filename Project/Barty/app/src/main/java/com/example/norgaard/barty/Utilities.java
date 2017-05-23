package com.example.norgaard.barty;

import android.content.ContentValues;

import com.example.norgaard.barty.Data.BartyContract;
import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.DrinkBase;

import java.util.ArrayList;

public class Utilities {

    public static ContentValues[] createContentValuesForWeatherInfos(ArrayList<Bar> bars) {
        ContentValues[] contentValues = new ContentValues[bars.size()];

        for (int i = 0; i < bars.size(); i++) {
            ContentValues value = new ContentValues();
            value.put(BartyContract.BarEntry.COLUMN_BAR_NAME, bars.get(i).barName);
            contentValues[i] = value;
        }

        return contentValues;
    }

    public static ContentValues createContentValuesForDrink(DrinkBase drink, long barId) {
        ContentValues value = new ContentValues();
        value.put(BartyContract.BasketEntry.COLUMN_DRINK_NAME, drink.getName());
        value.put(BartyContract.BasketEntry.COLUMN_DRINK_PRICE, drink.getPrice());
        value.put(BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID, barId);

        return value;
    }
}
