package com.example.norgaard.bardy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState != null) {
            // Restore state
        }
        else {
            Intent intent = getIntent();
        }
    }

    public  void onSignupClicked(){
        // Do soemthing
    }
}
