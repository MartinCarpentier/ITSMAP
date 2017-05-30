package com.example.norgaard.barty.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.norgaard.barty.Activities.Maps.MapsActivity;

/**
 * Created by marti on 30-05-2017.
 */

public class NewOrderBroadcastReceiver extends BroadcastReceiver{
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Received order broadcast");
        Intent serviceIntent = new Intent(context, BartyService.class);
        serviceIntent.putExtra("firebaseTag", intent.getStringExtra("firebaseTag"));

        //This will "restart" the service
        context.startService(serviceIntent);

    }
}
