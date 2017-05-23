package com.example.norgaard.barty.BarSale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.norgaard.barty.BarDistanceAdapter;
import com.example.norgaard.barty.Data.BartyContract;
import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.DrinkBase;
import com.example.norgaard.barty.Models.Drinks;
import com.example.norgaard.barty.R;
import com.example.norgaard.barty.Utilities;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import org.parceler.Parcels;

import java.util.ArrayList;

import static android.support.v7.recyclerview.R.attr.layoutManager;

public class BarSale extends AppCompatActivity implements
        DrinksAdapter.DrinksOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor>{

    private TextView testView;
    private RecyclerView recyclerView;
    public DrinksAdapter drinksAdapter;
    private LinearLayoutManager layoutManager;
    private Bar currentBar;
    public ArrayList<DrinkBase> drinks;
    private long currentbBarId;
    private Context mContext = this;
    private String logStuff = BarSale.class.toString();

    private static final int ID_BAR_LOADER = 44;

    public static final String[] BAR_DRINK_BASKET_PROJECTION = {
            BartyContract.BasketEntry.COLUMN_DRINK_NAME,
            BartyContract.BasketEntry.COLUMN_DRINK_PRICE,
            BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID
    };

    public static final int COLUMN_DRINK_NAME = 0;
    public static final int COLUMN_DRINK_PRICE = 1;
    public static final int COLUMN_FOREIGN_BAR_ID = 2;


    private TextView currentDrinkPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_sale);

        Intent intent = getIntent();
        currentBar = (Bar) Parcels.unwrap(getIntent().getParcelableExtra("barname_key"));
        currentDrinkPriceText = (TextView)findViewById(R.id.currentDrinkPriceText);

        Log.d("Barsale", "Current bar is " + currentBar.getBarname());

        setTitle(currentBar.getBarname());

        setTabs();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerDrinksView);

        //progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        drinksAdapter = new DrinksAdapter(this, this);

        getSupportLoaderManager().initLoader(ID_BAR_LOADER, null, this);

        recyclerView.setAdapter(drinksAdapter);

        drinksAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getBeer()));
    }

    private void setTabs() {
        TabLayout tabs = (TabLayout)findViewById(R.id.tabanim_tabs);
        TabLayout.Tab cocktail = tabs.newTab().setText("Cocktail");

        tabs.addTab(tabs.newTab().setText("Beer"));
        tabs.addTab(cocktail);
        tabs.addTab(tabs.newTab().setText("Shots"));

        //Handles which drinks to show in the drinksview
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString())
                {
                    case "Cocktail":

                        drinksAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getCocktails()));

                        break;
                    case "Beer":
                        drinksAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getBeer()));
                        break;

                    case "Shots":
                        drinksAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getShots()));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.selected_bar_menu, menu);
        return true;
    }

    @Override
    public void onClick(DrinkBase drink) {

            ContentValues value = Utilities.createContentValuesForDrink(drink, currentbBarId);

            //Insert values into db
            ContentResolver barCuntentResolver = getApplicationContext().getContentResolver();



            barCuntentResolver.insert(
                    BartyContract.BasketEntry.CONTENT_URI_BASKET,
                    value);

            Log.d("stuff", "asoid");


        getSupportLoaderManager().restartLoader(ID_BAR_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_BAR_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri barQueryUri = BartyContract.getUriForSpecificBar(currentBar.getBarname());
                /* Sort order: Ascending by date */
                String sortOrder = BartyContract.BasketEntry.COLUMN_DRINK_PRICE + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
                return new CursorLoader(mContext,
                        barQueryUri,
                        BAR_DRINK_BASKET_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        double totalPrice = 0;
        drinks = new ArrayList<DrinkBase>();
        if(data.getCount() != 0)
        {
            data.moveToFirst();
            currentbBarId = data.getLong(COLUMN_FOREIGN_BAR_ID);

            for(int i = 0; i < data.getCount(); i++)
            {
                String drinkName = data.getString(COLUMN_DRINK_NAME);
                double drinkPrice = data.getFloat(COLUMN_DRINK_PRICE);
                DrinkBase drink = new DrinkBase();
                drink.setPrice(((long) drinkPrice));
                drink.setName(drinkName);

                drinks.add(drink);
                totalPrice += drinkPrice;

                data.moveToNext();
            }
        }

        currentDrinkPriceText.setText(String.valueOf(totalPrice));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
