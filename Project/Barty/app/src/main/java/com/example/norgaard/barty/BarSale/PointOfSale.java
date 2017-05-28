package com.example.norgaard.barty.BarSale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.norgaard.barty.Data.BartyContract;
import com.example.norgaard.barty.Models.DrinkBase;
import com.example.norgaard.barty.R;

import java.math.BigDecimal;

import dk.danskebank.mobilepay.sdk.MobilePay;
import dk.danskebank.mobilepay.sdk.model.Payment;

public class PointOfSale extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        PointOfSaleAdapter.PointOnSaleOnClickHandler{

    private CardView mobilePay;
    private Button goToPayment;
    private int MOBILEPAY_REQUEST_CODE = 666;
    private Spinner paymentMethods;
    private String selectedPaymentMethod;
    private Context mContext = this;
    private PointOfSale pos = this;
    private Uri currentBarUri;
    private RecyclerView recyclerView;
    private PointOfSaleAdapter posAdapter;
    private LinearLayoutManager layoutManager;
    private TextView totalPriceText;

    private static final int ID_BASKET_LOADER = 44;

    public static final String[] BASKET_DRINK_BASKET_PROJECTION = {
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
        setContentView(R.layout.activity_point_of_sale);
        //goToPayment = (Button) findViewById(R.id.btnGoToPayment);
        //paymentMethods = (Spinner) findViewById(R.id.spinnerPaymentMethod);
        //paymentMethods.setOnItemSelectedListener(this);

        Intent currentIntent = getIntent();
        currentBarUri = currentIntent.getData();

        // Code taken from/inspired  by:
        // https://developer.android.com/guide/topics/ui/controls/spinner.html
        ArrayAdapter<CharSequence> paymentMethodsArrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.payment_methods,
                android.R.layout.simple_spinner_item);

        totalPriceText = (TextView)findViewById(R.id.totalPricePOS);


        //paymentMethods.setAdapter(paymentMethodsArrayAdapter);
        /*goToPayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                switch (selectedPaymentMethod) {
                    case "MobilePay":
                        initMobilePayPayment();
                        break;
                    case "Visa":
                        initStripe(cardType.VISA);
                        break;
                    case "Mastercard":
                        initStripe(cardType.MASTERCARD);
                        break;
                }
            }
        });*/

        recyclerView = (RecyclerView) findViewById(R.id.posRecyclerView);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(ID_BASKET_LOADER, null, this);

        posAdapter = new PointOfSaleAdapter(this, this);

        recyclerView.setAdapter(posAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        //do things
                        ContentResolver barCuntentResolver = mContext.getContentResolver();

                        String drinkSelection = BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID + "=" + currentBarUri.getLastPathSegment() + " AND " +
                                BartyContract.BasketEntry.COLUMN_DRINK_NAME + " = ?";

                        PointOfSaleAdapter.PointOfSaleViewHolder posViewHolder = (PointOfSaleAdapter.PointOfSaleViewHolder)viewHolder;
                        String[] drinkArgs = new String[] {posViewHolder.drinkName.getText().toString()};

                        barCuntentResolver.delete(
                                BartyContract.BasketEntry.CONTENT_URI_BASKET,
                                drinkSelection,
                                drinkArgs);

                        getSupportLoaderManager().restartLoader(ID_BASKET_LOADER, null, pos);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mobilePay = (CardView)findViewById(R.id.mobilePayButton);
        mobilePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos.initMobilePayPayment();
            }
        });

    }

    // https://github.com/MobilePayDev/MobilePay-AppSwitch-SDK/wiki/Getting-started-on-Android
    private void initMobilePayPayment() {
        boolean isMobilePayInstalled = MobilePay.getInstance().isMobilePayInstalled(getApplicationContext());
        if (isMobilePayInstalled) {
            Payment payment = new Payment();
            payment.setProductPrice(new BigDecimal(totalPriceText.getText().toString()));
            payment.setOrderId("86715c57-8840-4a6f-af5f-07ee89107ece");
            Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(payment);
            startActivityForResult(paymentIntent, MOBILEPAY_REQUEST_CODE);
        }
        else {
            Intent intent = MobilePay.getInstance().createDownloadMobilePayIntent(getApplicationContext());
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedPaymentMethod = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_BASKET_LOADER:

                String sortOrder = BartyContract.BasketEntry.COLUMN_DRINK_NAME + " ASC";

                return new CursorLoader(mContext,
                        currentBarUri,
                        BASKET_DRINK_BASKET_PROJECTION,
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

                totalPrice += drinkPrice*drinkQuantity;

                data.moveToNext();
            }
        }

        totalPriceText.setText(String.valueOf(totalPrice));
        posAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        posAdapter.swapData(null);

    }

    @Override
    public void onAddOrDelete(ContentValues values) {
        ContentResolver barCuntentResolver = mContext.getContentResolver();

            barCuntentResolver.insert(
                    BartyContract.BasketEntry.CONTENT_URI_BASKET,
                    values);


        getSupportLoaderManager().restartLoader(ID_BASKET_LOADER, null, this);
    }

    @Override
    public void onDelete(String drinkName) {
        ContentResolver barCuntentResolver = mContext.getContentResolver();

        String drinkSelection = BartyContract.BasketEntry.COLUMN_FOREIGN_BAR_ID + "=" + currentBarUri.getLastPathSegment() + " AND " +
                BartyContract.BasketEntry.COLUMN_DRINK_NAME + " = ?";

        String[] drinkArgs = new String[] {drinkName};

        barCuntentResolver.delete(
                BartyContract.BasketEntry.CONTENT_URI_BASKET,
                drinkSelection,
                drinkArgs);

        getSupportLoaderManager().restartLoader(ID_BASKET_LOADER, null, this);
    }
}
