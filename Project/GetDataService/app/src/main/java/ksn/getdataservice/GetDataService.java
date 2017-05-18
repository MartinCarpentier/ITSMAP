package ksn.getdataservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class GetDataService extends IntentService {

    public GetDataService() {
        super("GetDataService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // get data from server
        Log.d("GetDataService","Service started");
        stopSelf(); // stop service
    }


}
