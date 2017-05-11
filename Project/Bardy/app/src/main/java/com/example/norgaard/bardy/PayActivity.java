package com.example.norgaard.bardy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PayActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        if (savedInstanceState != null) {
            // Restore state
        }
        else {
            Intent intent = getIntent();
        }
    }

    public void onPayNowClicked() {
        // Intent intent = new Intent(context, /*Mobilepay*/);
    }
}
