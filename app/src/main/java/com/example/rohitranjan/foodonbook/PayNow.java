package com.example.rohitranjan.foodonbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;


public class PayNow extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    public static final String ITEM_ID1 = "itemid2";
    public static final String TOTAL = "total";
    public static final String NAME_S = "nameS";
    public static final String PHONE_S = "phoneS";
    public static final String DATA = "data";
    public static final String ITEMNAME_S = "arrayItemName";
    public static final String QUANTITY_S = "arrayQuantity";
    public static final String PRICE_S = "arrayPrice";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Conf.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    TextView edtAmount;
    String myid,amountTotal,oName,oPhone,dataTable;
    ArrayList<String> arrayItemName;
    ArrayList<String> arrayQuantity;
    ArrayList<String> arrayPrice;
    String amount = "";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);

        Intent i = getIntent();
        myid = i.getStringExtra(ITEM_ID1);
        amountTotal = i.getStringExtra(TOTAL);
        oName = i.getStringExtra(NAME_S);
        oPhone = i.getStringExtra(PHONE_S);
        dataTable = i.getStringExtra(DATA);

        //Start Paypal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnPayNow = (Button)findViewById(R.id.btnPayNow);
        edtAmount = (TextView)findViewById(R.id.edtAmount);
        edtAmount.setText(amountTotal);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });

    }

    private void processPayment(){
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amountTotal)),"USD",
                "Payment For Food",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null)
                {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this,PaymentDetails.class)
                                    .putExtra("PaymentDetails",paymentDetails)
                                    .putExtra("PaymentAmount",amountTotal)
                                    .putExtra(ITEM_ID1,myid)
                                    .putExtra(NAME_S,oName)
                                    .putExtra(PHONE_S,oPhone)
                                    .putExtra(TOTAL,amountTotal)
                                    .putExtra(DATA,dataTable)
                        );


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}
