package com.example.norgaard.barty.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BartyService extends Service {

    // Logging
    public static final String LOG_TAG = "BartyServiceLogTag";

    // Timer
    private LoopRequest loopRequest;
    private long delay = 2 * 1000;
    private static Timer timer;

    private boolean isServiceStarted;

    public BartyService() {
        Log.d(LOG_TAG, "Constructor invoked");
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand()");

        if (isServiceStarted == false && intent != null) {
            Log.d(LOG_TAG, "Service started");
            isServiceStarted = true;

            timer = new Timer();
            Date date = new Date();
            loopRequest = new LoopRequest();
            timer.scheduleAtFixedRate(loopRequest, date, delay);
        }
        else {
            Log.d(LOG_TAG, "Service already started");
        }

        return START_STICKY;
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
