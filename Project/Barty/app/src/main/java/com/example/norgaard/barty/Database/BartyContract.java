package com.example.norgaard.barty.Database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class BartyContract {

    public static final String CONTENT_AUTHORITY = "com.example.norgaard.barty";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BARS = "bars";
    public static final String PATH_BASKET = "basket";

    public static Uri getUriForSpecificBar(long id) {
        return ContentUris.withAppendedId(BasketEntry.CONTENT_URI_BASKET, id);
    }

    public static String getSqlSelectForCurrentBarBasket(long barId) {
        return BasketEntry.COLUMN_FOREIGN_BAR_ID + " = " + barId;
    }

    public static class BarEntry implements BaseColumns {

        public static final Uri CONTENT_URI_BARS = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_BARS)
                .build();

        public static final String TABLE_NAME_BARS = "Bars";
        public static final String COLUMN_BAR_NAME = "BarName";
        public static final String COLUMN_BAR_ONLINE_ID = "BarOnlineId";
    }

    public static class BasketEntry implements BaseColumns {

        public static final Uri CONTENT_URI_BASKET = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_BASKET)
                .build();

        public static final String TABLE_NAME_BASKET = "Basket";
        public static final String COLUMN_FOREIGN_BAR_ID = "BarForeignId";
        public static final String COLUMN_DRINK_NAME = "DrinkName";
        public static final String COLUMN_DRINK_PRICE = "DrinksPrice";
        public static final String COLUMN_DRINK_QUANTITY = "DrinksQuantity";
    }
}
