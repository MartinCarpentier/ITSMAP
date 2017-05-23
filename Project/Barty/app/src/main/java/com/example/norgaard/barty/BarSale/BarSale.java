package com.example.norgaard.barty.BarSale;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
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
import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.DrinkBase;
import com.example.norgaard.barty.Models.Drinks;
import com.example.norgaard.barty.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

import org.parceler.Parcels;

import java.util.ArrayList;

import static android.support.v7.recyclerview.R.attr.layoutManager;

public class BarSale extends AppCompatActivity implements
        DrinksAdapter.DrinksOnClickHandler {

    private TextView testView;
    private RecyclerView recyclerView;
    public DrinksAdapter drinksAdapter;
    private LinearLayoutManager layoutManager;
    private Bar currentBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_sale);

        Intent intent = getIntent();
        currentBar = (Bar) Parcels.unwrap(getIntent().getParcelableExtra("barname_key"));

        Log.d("Barsale", "Current bar is " + currentBar.getBarname());

        setTitle(currentBar.getBarname());

        setTabs();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerDrinksView);

        //progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        drinksAdapter = new DrinksAdapter(this, this);

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

    }
}
