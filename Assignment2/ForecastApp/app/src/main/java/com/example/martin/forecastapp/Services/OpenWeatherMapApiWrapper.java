package com.example.martin.forecastapp.Services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class OpenWeatherMapApiWrapper {

    private Context _context;
    private String _stringResponse;
    private RequestQueue _queue;

    OpenWeatherMapApiWrapper(Context context) {
        this._context = context;
        callApi();
    }

    private void callApi() {

        if (_queue == null) {
            _queue = Volley.newRequestQueue(_context);
        }

        String url = "https://jsonplaceholder.typicode.com/posts/1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        _stringResponse = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _stringResponse = "No good!";
                    }
                }
        );

        _queue.add(stringRequest);
    }
}
