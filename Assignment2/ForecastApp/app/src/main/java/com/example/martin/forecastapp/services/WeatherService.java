package com.example.martin.forecastapp.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.martin.forecastapp.data.ForecastContract;
import com.example.martin.forecastapp.models.WeatherInfo;
import com.example.martin.forecastapp.utils.Utilities;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class WeatherService extends Service {

    private static final String BROADCAST_BACKGROUND_SERVICE_RESULT = "100";
    private static final String EXTRA_TASK_RESULT = "101";
    private static Timer requestTimer;
    long delay = 30 * 60 * 1000; // 30 min delay in milliseconds
    //long delay = 30 * 1000; // 30 sec delay in milliseconds
    LoopRequest task = new LoopRequest();

    final private String LOG_TAG_WEATHER_SERVICE = "WeatherService";

    private static final long DEFAULT_WAIT = 10 * 1000;
    private Boolean isServiceStarted;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onCreate()");
        requestTimer = new Timer();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onStartCommand()");

        if (isServiceStarted == null || isServiceStarted == false && intent != null) {
            Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onStartCommand with wait: " + DEFAULT_WAIT + "s");
            isServiceStarted = true;

            //volleyJsonObjectRequest();

            requestTimer.cancel();
            requestTimer = new Timer();
            Date executionDate = new Date(); // no params = now
            requestTimer.scheduleAtFixedRate(task, executionDate, delay);
        } else {
            Log.d(LOG_TAG_WEATHER_SERVICE, "Background service onStartCommand is already started");
        }

        return START_STICKY;
    }

    private class LoopRequest extends TimerTask {
        public void run() {
            volleyJsonObjectRequest();
        }
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

    public void volleyJsonObjectRequest() {

        String REQUEST_TAG = "com.androidtutorialpoint.volleyJsonObjectRequest";

        URL weatherUrl = Utilities.createWeatherUrlForAarhus();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(weatherUrl.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        Gson gson = new Gson();
                        JSONArray weatherArray = new JSONArray();
                        try {
                            weatherArray = response.getJSONArray("list");


                            List<WeatherInfo> weatherInfo = new ArrayList<WeatherInfo>();
                            if (weatherArray != null) {
                                for (int i = 0; i < weatherArray.length(); i++) {
                                    weatherInfo.add(gson.fromJson(weatherArray.getString(i), WeatherInfo.class));
                                }
                            }

                            //Type listType = new TypeToken<ArrayList<WeatherInfo>>() {}.getType();
                            //WeatherInfo weatherInfo = gson.from(jsonString, WeatherInfo.class);

                            Log.d(TAG, "Weatherinfo created: size -> " + weatherInfo.size());

                            ContentValues[] values = Utilities.createContentValuesForWeatherInfos(weatherInfo);

                            //Insert values into db
                            ContentResolver ForecastContentResolver = getApplicationContext().getContentResolver();

                            ForecastContentResolver.bulkInsert(
                                    ForecastContract.ForecastEntry.CONTENT_URI,
                                    values);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Add the request to the queue
        Volley.newRequestQueue(this).add(jsonObjectReq);
    }
}
