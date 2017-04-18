package com.example.martin.developercv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.martin.developercv.Model.DeveloperInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Context context = this;

    @BindView(R.id.nameText)
    TextView nameText;

    @BindView(R.id.androidText)
    TextView androidText;

    @BindView(R.id.idText)
    TextView idText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if(savedInstanceState != null)
        {
            nameText.setText(savedInstanceState.getString(getString(R.string.NameKey)));
            idText.setText(savedInstanceState.getString(getString(R.string.IdKey)));
            androidText.setText(savedInstanceState.getString(getString(R.string.androidKey)));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditCvActivity.class);

                DeveloperInfo info = new DeveloperInfo(nameText.getText().toString(), idText.getText().toString(), androidText.getText().toString());
                intent.putExtra("developerInfo", info);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.NameKey), nameText.getText().toString());
        outState.putString(getString(R.string.IdKey), idText.getText().toString());
        outState.putString(getString(R.string.androidKey), androidText.getText().toString());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {
        if (resultCode == RESULT_OK) {
            DeveloperInfo developerInfo = (DeveloperInfo) resultIntent.getParcelableExtra("developerResult");

            String name = developerInfo.getName();
            String id = developerInfo.getId();
            String android = String.valueOf(developerInfo.isAndroid());

            nameText.setText(name);
            idText.setText(id);
            androidText.setText(android);
        }
    }
}
