package com.example.norgaard.barty.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BartyService extends Service {

    // For logging purposes
    public static final String LOG_MESSAGE_START = "BartyService ";
    public static final String LOG_TAG = "LOG_TAG";

    private boolean isServiceStarted;

    public BartyService() {
        Log.d(LOG_TAG, LOG_MESSAGE_START + "Constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, LOG_MESSAGE_START + "onBind()");

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, LOG_MESSAGE_START + "onCreate()");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, LOG_MESSAGE_START + "onStartCommand()");

        if (isServiceStarted == false && intent != null) {
            Log.d(LOG_TAG, LOG_MESSAGE_START + "service started");
            isServiceStarted = true;

            // Do stuff 
        }
        else {
            Log.d(LOG_TAG, LOG_MESSAGE_START + "already started");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, LOG_MESSAGE_START + "onDestroy");

        super.onDestroy();
    }
}
