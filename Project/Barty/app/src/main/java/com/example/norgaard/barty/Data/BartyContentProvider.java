package com.example.norgaard.barty.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class BartyContentProvider extends ContentProvider {

    public static final int CODE_BARS = 100;
    public static final int CODE_BASKET_FOR_BAR = 101;
    public static final int CODE_BASKET = 102;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final String[] MAIN_BAR_PROJECTION = {
            BartyContract.BarEntry.COLUMN_BAR_NAME
    };

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BartyContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BartyContract.PATH_BARS, CODE_BARS);
        matcher.addURI(authority, BartyContract.PATH_BASKET + "/*", CODE_BASKET_FOR_BAR);
        matcher.addURI(authority, BartyContract.PATH_BASKET, CODE_BASKET);

        return matcher;
    }

    private DatabaseHelper mOpenHelper;

    public BartyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int deletedId;
        switch (sUriMatcher.match(uri)) {

            case CODE_BASKET:
                try {
                    db.beginTransaction();
                    deletedId = db.delete(BartyContract.BasketEntry.TABLE_NAME_BASKET, selection, selectionArgs);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return deletedId;
            default:
                return 0;
        }

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_BARS:
                try {
                    db.beginTransaction();
                    long barid = db.insert(BartyContract.BarEntry.TABLE_NAME_BARS, null, values);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return null;
            case CODE_BASKET:
                try {
                    db.beginTransaction();
                    long basketId = db.insert(BartyContract.BasketEntry.TABLE_NAME_BASKET, null, values);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                return null;
            default:
                return null;
        }
    }

    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_BARS:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BartyContract.BarEntry.TABLE_NAME_BARS, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_BASKET_FOR_BAR:
                //Get bar foreign key
                long currentBarId = Long.valueOf(uri.getLastPathSegment());

                String drinkSelection = BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID + "=?";
                String[] drinkArgs = new String[] {String.valueOf(currentBarId)};

                cursor = mOpenHelper.getReadableDatabase().query(
                        BartyContract.BasketEntry.TABLE_NAME_BASKET,
                        projection,
                        drinkSelection,
                        drinkArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case CODE_BASKET:
                cursor = mOpenHelper.getReadableDatabase().query(
                        BartyContract.BasketEntry.TABLE_NAME_BASKET,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_BARS:
                cursor = mOpenHelper.getReadableDatabase().query(
                        BartyContract.BarEntry.TABLE_NAME_BARS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


}
