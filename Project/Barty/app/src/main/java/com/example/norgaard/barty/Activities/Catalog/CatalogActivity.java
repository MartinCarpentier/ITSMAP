package com.example.norgaard.barty.Activities.Catalog;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.norgaard.barty.Activities.Checkout.CheckoutActivity;
import com.example.norgaard.barty.Database.BartyContract;
import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.DrinkBase;
import com.example.norgaard.barty.R;
import com.example.norgaard.barty.Utilities.ContentValueCreator;

import org.parceler.Parcels;

import java.util.ArrayList;

public class CatalogActivity extends AppCompatActivity implements
        CatalogAdapter.DrinksOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    public CatalogAdapter catalogAdapter;
    private LinearLayoutManager layoutManager;
    private Bar currentBar;
    private MenuView.ItemView menu;
    public ArrayList<DrinkBase> drinks;
    private Context mContext = this;
    private String logTag = CatalogActivity.class.getSimpleName();
    private LinearLayout priceLayout;
    private TextView barText;
    private Context context = this;

    static final int CHECKOUT_COUNTER_RESULT = 1;

    private static final int ID_BAR_LOADER = 44;

    public static final String[] BAR_DRINK_BASKET_PROJECTION = {
            BartyContract.BasketEntry.COLUMN_DRINK_NAME,
            BartyContract.BasketEntry.COLUMN_DRINK_PRICE,
            BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID,
            BartyContract.BasketEntry.COLUMN_DRINK_QUANTITY
    };

    public static final int COLUMN_DRINK_NAME = 0;
    public static final int COLUMN_DRINK_PRICE = 1;
    public static final int COLUMN_FOREIGN_BAR_ID = 2;
    public static final int COLUMN_DRINK_QUANTITY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        if (savedInstanceState != null) {
            currentBar = Parcels.unwrap(savedInstanceState.getParcelable("barname_key"));
        }
        else {

            currentBar = Parcels.unwrap(getIntent().getParcelableExtra("barname_key"));
        }
        menu = (MenuView.ItemView) findViewById(R.id.action_favorite);
        Log.d(logTag, "Current bar is " + currentBar.getBarname());
        barText = (TextView) findViewById(R.id.totalPriceBar);
        setTitle(currentBar.getBarname());
        setTabs();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerDrinksView);
        priceLayout = (LinearLayout) findViewById(R.id.priceLayout);

        priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheckoutActivity();
            }
        });

        int columnAmount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columnAmount = 4;
        }
        else {
            columnAmount = 2;
        }

        layoutManager = new GridLayoutManager(this, columnAmount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        catalogAdapter = new CatalogAdapter(this, this);
        getSupportLoaderManager().initLoader(ID_BAR_LOADER, null, this);
        recyclerView.setAdapter(catalogAdapter);
        catalogAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getBeer()));
    }

    private void setTabs() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabanim_tabs);
        TabLayout.Tab cocktail = tabs.newTab().setText(R.string.cocktail).setTag("Cocktail");

        tabs.addTab(tabs.newTab().setText(R.string.beer).setTag("Beer"));
        tabs.addTab(cocktail);
        tabs.addTab(tabs.newTab().setText(R.string.shots).setTag("Shots"));

        //Handles which drinks to show in the drinksview
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getTag().toString()) {
                    case "Cocktail":
                        catalogAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getCocktails()));
                        break;
                    case "Beer":
                        catalogAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getBeer()));
                        break;
                    case "Shots":
                        catalogAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getShots()));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("barname_key", Parcels.wrap(currentBar));

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.selected_bar_menu, menu);
        return true;
    }

    // Code taken from/inspired by:
    // https://stackoverflow.com/questions/7479992/handling-a-menu-item-click-event-android
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                startCheckoutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startCheckoutActivity() {
        Intent intentOpenBasket = new Intent(context, CheckoutActivity.class);

        intentOpenBasket.setData(BartyContract.getUriForSpecificBar(currentBar.getId()));

        // putExtra values that the POS needs here

        startActivityForResult(intentOpenBasket, CHECKOUT_COUNTER_RESULT);
    }

    @Override
    public void onClick(DrinkBase drink) {

        ContentResolver barContentResolver = getApplicationContext().getContentResolver();

        //Find the clicked drink
        String drinkSelection = BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID + "=" + currentBar.getId() + " AND " +
                BartyContract.BasketEntry.COLUMN_DRINK_NAME + " = ?";

        String[] drinkArgs = new String[]{drink.getName()};
        Cursor drinkCursor = barContentResolver.query(BartyContract.BasketEntry.CONTENT_URI_BASKET,
                CatalogActivity.BAR_DRINK_BASKET_PROJECTION,
                drinkSelection,
                drinkArgs,
                null
        );

        int drinkQuantity;
        if (drinkCursor.getCount() == 0) {
            drinkQuantity = 1;
        }
        else {
            drinkCursor.moveToFirst();
            drinkQuantity = drinkCursor.getInt(CatalogActivity.COLUMN_DRINK_QUANTITY) + 1;
        }

        //Create values to insert into db
        ContentValues value = ContentValueCreator.createContentValuesForDrinks(drink, currentBar.id, drinkQuantity);

        //Insert values into db
        barContentResolver.insert(
                BartyContract.BasketEntry.CONTENT_URI_BASKET,
                value);

        getSupportLoaderManager().restartLoader(ID_BAR_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_BAR_LOADER:

                Uri barQueryUri = BartyContract.getUriForSpecificBar(currentBar.getId());

                return new CursorLoader(mContext,
                        barQueryUri,
                        BAR_DRINK_BASKET_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        double totalPrice = 0;
        drinks = new ArrayList<DrinkBase>();
        if (data.getCount() != 0) {
            data.moveToFirst();

            for (int i = 0; i < data.getCount(); i++) {
                String drinkName = data.getString(COLUMN_DRINK_NAME);
                double drinkPrice = data.getFloat(COLUMN_DRINK_PRICE);
                long drinkQuantity = data.getLong(COLUMN_DRINK_QUANTITY);
                DrinkBase drink = new DrinkBase();
                drink.setPrice(((long) drinkPrice));
                drink.setName(drinkName);
                drink.drinkQuantity = drinkQuantity;

                drinks.add(drink);
                totalPrice += drinkPrice * drinkQuantity;

                data.moveToNext();
            }
        }

        barText.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CHECKOUT_COUNTER_RESULT) {
            //Here we make sure to always reset checkout basket amount when returning from the checkout counter
            getSupportLoaderManager().restartLoader(ID_BAR_LOADER, null, this);
        }
    }
}
