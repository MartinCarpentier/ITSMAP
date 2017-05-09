package com.example.martin.forecastapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mbc on 08-05-2017.
 */

public class AutoStart extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context,WeatherService.class);
        context.startService(intent);
        Log.i("AutoStart", "started");
    }
}
