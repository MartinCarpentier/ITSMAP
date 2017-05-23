package com.example.norgaard.barty;

import android.content.ContentValues;

import com.example.norgaard.barty.Data.BartyContract;
import com.example.norgaard.barty.Models.Bar;

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
}
