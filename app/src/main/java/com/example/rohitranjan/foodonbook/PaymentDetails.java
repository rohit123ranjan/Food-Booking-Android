package com.example.rohitranjan.foodonbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentDetails extends AppCompatActivity {

    TextView txtId,txtAmount,txtStatus;
    RatingBar ratingbar;
    String rating;
    public static final String ITEM_ID1 = "itemid2";
    public static final String ITEM_ID2 = "itemid3";
    public static final String TOTAL = "total";
    public static final String NAME_S = "nameS";
    public static final String PHONE_S = "phoneS";
    public static final String DATA = "data";
    public static final String RATING = "rating";

    public static final String ITEMNAME_S = "arrayItemName";
    public static final String QUANTITY_S = "arrayQuantity";
    public static final String PRICE_S = "arrayPrice";
    String myid,amountTotal,oName,oPhone,oTotal,dataTable;
    ArrayList<String> arrayItemName;
    ArrayList<String> arrayQuantity;
    ArrayList<String> arrayPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = (TextView)findViewById(R.id.txtId);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);

        //Get Intent
        Intent intent = getIntent();
        myid = intent.getStringExtra(ITEM_ID1);
        amountTotal = intent.getStringExtra(TOTAL);
        oName = intent.getStringExtra(NAME_S);
        oPhone = intent.getStringExtra(PHONE_S);
        dataTable = intent.getStringExtra(DATA);
        oTotal = amountTotal;

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showDetails(JSONObject response, String paymentAmount) {

        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText("$"+paymentAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void myOrder(View view) {

        rating=String.valueOf(ratingbar.getRating());
        Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();

        DatabaseReference mDataCartRef = FirebaseDatabase.getInstance().getReference("OrderInfo").child(myid);
        pojoOrder order_pojo = new pojoOrder(oName,oPhone,oTotal,dataTable,rating);
        mDataCartRef.setValue(order_pojo);
        Toast.makeText(getApplicationContext(), "Order Successful!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
        intent.putExtra(ITEM_ID2, myid);
        intent.putExtra(TOTAL, amountTotal);
        intent.putExtra(NAME_S, oName);
        intent.putExtra(PHONE_S, oPhone);
        intent.putExtra(DATA,dataTable);
        intent.putExtra(RATING,rating);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
