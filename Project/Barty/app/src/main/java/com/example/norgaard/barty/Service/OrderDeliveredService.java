package com.example.norgaard.barty.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by marti on 31-05-2017.
 */

public class OrderDeliveredService extends IntentService {
    public OrderDeliveredService() {
        super(OrderDeliveredService.class.getSimpleName());
    }

    private String log_tag = OrderDeliveredService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        Log.d(log_tag, "Received notification action: " + action);

    }
}