package com.example.martin.developercv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.martin.developercv.Model.DeveloperInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditCvActivity extends AppCompatActivity {

    @BindView(R.id.saveButton)
    Button saveButton;

    @BindView(R.id.cancelButton)
    Button cancelButton;

    @BindView(R.id.editText)
    EditText nameText;

    @BindView(R.id.editText2)
    EditText idText;

    @BindView(R.id.RadioButtons)
    RadioGroup radioButtons;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cv);

        ButterKnife.bind(this);

        setTitle(getString(R.string.edit_resource));

        String name = "";
        String id = "";

        if(savedInstanceState != null)
        {
            name = savedInstanceState.getString(getString(R.string.NameKey));
            id = savedInstanceState.getString(getString(R.string.IdKey));

            int radioButtonId = savedInstanceState.getInt(getString(R.string.androidKey));

            radioButtons.check(radioButtonId);
        }
        else
        {
            DeveloperInfo developerInfo = (DeveloperInfo) getIntent().getParcelableExtra("developerInfo");

            name = developerInfo.getName();
            id = String.valueOf(developerInfo.getId());

            if(developerInfo.isAndroid())
            {
                radioButtons.check(R.id.yesRadioButton);
            }
            else
            {
                radioButtons.check(R.id.noRadioButton);
            }
        }

        idText.setText(id);
        nameText.setText(name);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                RadioButton selectedRadioButton = (RadioButton) findViewById(radioButtons.getCheckedRadioButtonId());

                boolean yesSelected = selectedRadioButton.getText() == getString(R.string.yes);

                DeveloperInfo info = new DeveloperInfo(nameText.getText().toString(), idText.getText().toString(), String.valueOf(yesSelected));
                resultIntent.putExtra("developerResult", info);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.NameKey), nameText.getText().toString());
        outState.putString(getString(R.string.IdKey), idText.getText().toString());
        outState.putInt(getString(R.string.androidKey), radioButtons.getCheckedRadioButtonId());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}
