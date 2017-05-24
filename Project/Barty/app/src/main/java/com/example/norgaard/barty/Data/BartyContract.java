package com.example.norgaard.barty.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BartyContract {

    public static final String CONTENT_AUTHORITY = "com.example.norgaard.barty";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BARS = "bars";
    public static final String PATH_BASKET = "basket";

    public static class BarEntry implements BaseColumns {

        public static final Uri CONTENT_URI_BARS = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_BARS)
                .build();

        public static final String TABLE_NAME_BARS = "Bars";
        public static final String COLUMN_BAR_NAME = "BarName";
    }

    public static class BasketEntry implements BaseColumns {

        public static final Uri CONTENT_URI_BASKET = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_BASKET)
                .build();

        public static final String TABLE_NAME_BASKET = "Basket";
        public static final String COLUMN_FOREIGN_BAR_ID = "BarForeignId";
        public static final String COLUMN_DRINK_NAME = "DrinkName";
        public static final String COLUMN_DRINK_QUANTITY = "DrinkQuantity";
        public static final String COLUMN_DRINK_PRICE = "DrinksPrice";
    }
}