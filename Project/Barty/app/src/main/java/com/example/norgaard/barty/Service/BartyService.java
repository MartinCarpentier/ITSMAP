package com.example.norgaard.barty.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BartyService extends Service {

    // For logging purposes
    public static final String LOG_MESSAGE_START = "BartyService";
    public static final String SERVICE_LOG_TAG = "SERVICE_LOG_TAG";

    public BartyService() {
        Log.d(SERVICE_LOG_TAG, LOG_MESSAGE_START + " in Constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(SERVICE_LOG_TAG, LOG_MESSAGE_START + " in onBind()");

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(SERVICE_LOG_TAG, LOG_MESSAGE_START + " in onCreate()");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(SERVICE_LOG_TAG, LOG_MESSAGE_START + " in onStartCommand()");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(SERVICE_LOG_TAG, LOG_MESSAGE_START + " in onDestroy");

        super.onDestroy();
    }
}
