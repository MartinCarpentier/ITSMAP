package com.example.norgaard.barty.BarSale;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.norgaard.barty.Models.Bar;
import com.example.norgaard.barty.Models.DrinkBase;
import com.example.norgaard.barty.R;

import org.parceler.Parcels;

import java.math.BigDecimal;
import java.util.ArrayList;

import dk.danskebank.mobilepay.sdk.MobilePay;
import dk.danskebank.mobilepay.sdk.model.Payment;

public class BarSale extends AppCompatActivity implements
        DrinksAdapter.DrinksOnClickHandler {

    private TextView testView;
    private RecyclerView recyclerView;
    public DrinksAdapter drinksAdapter;
    private LinearLayoutManager layoutManager;
    private Bar currentBar;
    private MenuView.ItemView menu;
    private int MOBILEPAY_REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_sale);

        Intent intent = getIntent();
        currentBar = (Bar) Parcels.unwrap(getIntent().getParcelableExtra("barname_key"));
        menu = (MenuView.ItemView) findViewById(R.id.action_favorite);
        Log.d("Barsale", "Current bar is " + currentBar.getBarname());

        setTitle(currentBar.getBarname());

        setTabs();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerDrinksView);

        //progressBar = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        drinksAdapter = new DrinksAdapter(this, this);

        recyclerView.setAdapter(drinksAdapter);

        drinksAdapter.swapData(new ArrayList<DrinkBase>(currentBar.drinks.getBeer()));
    }

    private void setTabs() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabanim_tabs);
        TabLayout.Tab cocktail = tabs.newTab().setText("Cocktail");

        tabs.addTab(tabs.newTab().setText("Beer"));
        tabs.addTab(cocktail);
        tabs.addTab(tabs.newTab().setText("Shots"));

        //Handles which drinks to show in the drinksview
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
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

    // Code taken from/inspired by:
    // https://stackoverflow.com/questions/7479992/handling-a-menu-item-click-event-android
    // https://github.com/MobilePayDev/MobilePay-AppSwitch-SDK/wiki/Getting-started-on-Android
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                boolean isMobilePayInstalled = MobilePay.getInstance().isMobilePayInstalled(getApplicationContext());
                if (isMobilePayInstalled) {
                    Payment payment = new Payment();
                    payment.setProductPrice(new BigDecimal(5.0));
                    payment.setOrderId("86715c57-8840-4a6f-af5f-07ee89107ece");
                    Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(payment);
                    startActivityForResult(paymentIntent, MOBILEPAY_REQUEST_CODE);
                } else {
                    Intent intent = MobilePay.getInstance().createDownloadMobilePayIntent(getApplicationContext());
                    startActivity(intent);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(DrinkBase drink) {

    }
}
