package com.example.martin.forecastapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoStart extends BroadcastReceiver {

    public void onReceive(Context context, Intent arg1) {
        Intent intent = new Intent(context, WeatherService.class);
        context.startService(intent);
        Log.i("AutoStart", "started");
    }
}
