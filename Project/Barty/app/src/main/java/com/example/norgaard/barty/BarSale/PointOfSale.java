package com.example.norgaard.barty.BarSale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.norgaard.barty.R;

import java.math.BigDecimal;

import dk.danskebank.mobilepay.sdk.MobilePay;
import dk.danskebank.mobilepay.sdk.model.Payment;

public class PointOfSale extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button goToPayment;
    private int MOBILEPAY_REQUEST_CODE = 999;
    private Spinner paymentMethods;
    private String selectedPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_sale);
        goToPayment = (Button) findViewById(R.id.btnGoToPayment);
        paymentMethods = (Spinner) findViewById(R.id.spinnerPaymentMethod);
        paymentMethods.setOnItemSelectedListener(this);

        // Code taken from/inspired  by:
        // https://developer.android.com/guide/topics/ui/controls/spinner.html
        ArrayAdapter<CharSequence> paymentMethodsArrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.payment_methods,
                android.R.layout.simple_spinner_item);

        paymentMethodsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        paymentMethods.setAdapter(paymentMethodsArrayAdapter);
        goToPayment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                switch (selectedPaymentMethod) {
                    case "MobilePay":
                        initMobilePayPayment();
                        break;
                    case "Visa":
                        initStripe(cardType.VISA);
                        break;
                    case "Mastercard":
                        initStripe(cardType.MASTERCARD);
                        break;
                }
            }
        });
    }

    // https://github.com/MobilePayDev/MobilePay-AppSwitch-SDK/wiki/Getting-started-on-Android
    private void initMobilePayPayment() {
        boolean isMobilePayInstalled = MobilePay.getInstance().isMobilePayInstalled(getApplicationContext());
        if (isMobilePayInstalled) {
            Payment payment = new Payment();
            payment.setProductPrice(new BigDecimal(5.0));
            payment.setOrderId("86715c57-8840-4a6f-af5f-07ee89107ece");
            Intent paymentIntent = MobilePay.getInstance().createPaymentIntent(payment);
            startActivityForResult(paymentIntent, MOBILEPAY_REQUEST_CODE);
        }
        else {
            Intent intent = MobilePay.getInstance().createDownloadMobilePayIntent(getApplicationContext());
            startActivity(intent);
        }
    }

    private void initStripe(cardType type) {

    }

    private enum cardType {
        MASTERCARD,
        VISA
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedPaymentMethod = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
