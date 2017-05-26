package com.example.norgaard.barty.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BartyService extends Service {

    public static final String SERVICE = "SERVICE";
    public static final String BARTY_SERVICE = "BartyService";

    public BartyService() {
        Log.d(SERVICE, BARTY_SERVICE + " in Constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(SERVICE, BARTY_SERVICE + " in onBind()");

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(SERVICE, BARTY_SERVICE + " in onCreate()");

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(SERVICE, BARTY_SERVICE + " in onStartCommand()");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(SERVICE, BARTY_SERVICE + " in onDestroy");

        super.onDestroy();
    }
}
