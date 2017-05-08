package com.example.martin.forecastapp.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class WeatherService extends Service {

    private static final String BROADCAST_BACKGROUND_SERVICE_RESULT = "100";
    private static final String EXTRA_TASK_RESULT = "101";

    final private String LOG_TAG_WEATHER_SERVICE = "WeatherService";

    private static final long DEFAULT_WAIT = 10 * 1000;
    private Boolean isServiceStarted;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onStartCommand()");

        if (isServiceStarted == false && intent != null) {
            Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onStartCommand with wait: " + DEFAULT_WAIT + "s");
            isServiceStarted = true;
            doBackgroundStuff();
        }
        else {
            Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onStartCommand is already started");
        }

        return START_STICKY;
    }

    private void doBackgroundStuff() {
        AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(LOG_TAG_WEATHER_SERVICE, "AsyncTask onPreExecute()");
            }

            @Override
            protected String doInBackground(Object... params) {
                String s = "Background job";

                try {
                    Log.d(LOG_TAG_WEATHER_SERVICE, "AsyncTask doInBackground() started");
                    Thread.sleep(DEFAULT_WAIT);
                    Log.d(LOG_TAG_WEATHER_SERVICE, "AsyncTask doInBackground() finished");
                }
                catch (Exception e) {
                    s += "did not finish due to error";
                    return s;
                }

                s += " completed after " + DEFAULT_WAIT + "s";
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d(LOG_TAG_WEATHER_SERVICE, "AsyncTask onPostExecute()");

                broadcastTaskResult(s);

                if (isServiceStarted) {
                    doBackgroundStuff();
                }
            }
        };

        task.execute();
    }

    private void broadcastTaskResult(String s) {
        Log.d(LOG_TAG_WEATHER_SERVICE, "Background service broadcastTaskResult()");

        Intent intent = new Intent();
        intent.setAction(BROADCAST_BACKGROUND_SERVICE_RESULT);
        intent.putExtra(EXTRA_TASK_RESULT, s);

        Log.d(LOG_TAG_WEATHER_SERVICE, "Broadcasting: " + s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onDestroy()");
        isServiceStarted = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onBind()");
        return null;
    }
}