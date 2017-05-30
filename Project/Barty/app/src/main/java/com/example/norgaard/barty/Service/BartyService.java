package com.example.norgaard.barty.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.norgaard.barty.Database.BartyContract;
import com.example.norgaard.barty.Models.Order;
import com.example.norgaard.barty.R;
import com.example.norgaard.barty.Utilities.ContentValueCreator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

public class BartyService extends Service {

    public static final String[] ORDER_PROJECTION = {
            BartyContract.OrderEntry.COLUMN_ORDER_PRICE,
            BartyContract.OrderEntry.COLUMN_ORDER_STATUS,
            BartyContract.OrderEntry.COLUMN_FOREIGN_BAR_ID,
            BartyContract.OrderEntry.COLUMN_ORDER_STATUS_TAG
    };

    public static final int COLUMN_ORDER_PRICE = 0;
    public static final int COLUMN_ORDER_STATUS = 1;
    public static final int COLUMN_FOREIGN_BAR_ID = 2;
    public static final int COLUMN_ORDER_STATUS_TAG = 3;

    // Logging
    public static final String LOG_TAG = "BartyServiceLogTag";

    // Timer
    private LoopRequest loopRequest;
    private long delay = 15 * 1000;
    private static Timer timer;

    // Service
    private boolean isServiceStarted;
    private FirebaseDatabase firebase;
    private ArrayList<Order> currentOrders;
    private DatabaseReference currentOrderRef;

    public BartyService() {
        Log.d(LOG_TAG, "Constructor invoked");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand()");

        if (isServiceStarted == false && intent != null) {

            Log.d(LOG_TAG, "Service started");
            isServiceStarted = true;

            timer = new Timer();
            Date date = new Date();
            loopRequest = new LoopRequest();
            timer.scheduleAtFixedRate(loopRequest, date, delay);

            //Check if there is pending orders
            startLookingForOrders();
        }
        else {
            addEventListenerForOrder(intent);
            Log.d(LOG_TAG, "Service already started");
        }

        return START_STICKY;
    }

    private void addEventListenerForOrder(Intent intent) {
        String firebaseTag = intent.getStringExtra("firebaseTag");

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        currentOrderRef = db.getReference().child("Orders/" + firebaseTag + "/Status");

        Query query = currentOrderRef.orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onDataChangeImplementation(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startLookingForOrders() {

        //Retrieve orders from database
        ContentResolver barCuntentResolver = getApplicationContext().getContentResolver();

        Cursor cursor = barCuntentResolver.query(
                BartyContract.OrderEntry.CONTENT_URI_ORDER,
                ORDER_PROJECTION,
                null,
                null,
                null);

        ArrayList<Order>orders = new ArrayList<Order>();

        try{
            while (cursor.moveToNext()) {
                String firebaseTag = cursor.getString(BartyService.COLUMN_ORDER_STATUS_TAG);
                String status = cursor.getString(BartyService.COLUMN_ORDER_STATUS);
                double price = cursor.getDouble(BartyService.COLUMN_ORDER_PRICE);
                int foreignBarId = cursor.getInt(BartyService.COLUMN_FOREIGN_BAR_ID);

                orders.add(new Order(firebaseTag, foreignBarId, price, status));
            }
        }
        finally {
            cursor.close();
        }

        currentOrders = orders;

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        for(int i = 0; i < currentOrders.size(); i++)
        {
            Order order = currentOrders.get(i);

            String tag = order.firebaseOrderTag;

            currentOrderRef = db.getReference().child("Orders/" + tag + "/Status");

            currentOrderRef.orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    onDataChangeImplementation(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(LOG_TAG, "cancelled");
                }
            });

        }
    }

    private void onDataChangeImplementation(DataSnapshot dataSnapshot) {
        Log.d(LOG_TAG, "data changed");

        String currentOrder = dataSnapshot.getRef().getParent().getKey().toString();
        String value = String.valueOf(dataSnapshot.getValue());

        Log.d(LOG_TAG, "Current order is " + currentOrder);
        if(Objects.equals(value, OrderStatus.Ready.toString()))
        {
            CreateNotificationForFirebase(currentOrder);
        }

        Log.d(LOG_TAG, "Value was " + value);
    }

    private void CreateNotificationForFirebase(String currentOrder) {

        Log.d(LOG_TAG, "Send a notification");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_location_city_white_48px)
                        .setContentTitle("Barty order is ready")
                        .setContentText("You can now go and retrieve your order, show the bartenders the following" +
                                "id to receive your drinks - " + currentOrder);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        isServiceStarted = false;

        super.onDestroy();
    }

    private class LoopRequest extends TimerTask {

        private String time;

        @Override
        public void run() {
            time = Calendar.getInstance().getTime().toString();
            Log.d(LOG_TAG, "Run method invoked at: " + time + ".");
        }
    }
}
