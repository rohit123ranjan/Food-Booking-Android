package com.example.rohitranjan.foodonbook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectTable extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView tableSelect;
    public static final String ITEM_ID1 = "itemid2";
    public static final String NAME_S = "nameS";
    public static final String PHONE_S = "phoneS";
    public static final String TOTAL = "total";
    public static final String DATA = "data";
    String myid,amountTotal,oName,oPhone;
    Spinner spinner;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_table);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        myid = i.getStringExtra(ITEM_ID1);
        amountTotal = i.getStringExtra(TOTAL);
        oName = i.getStringExtra(NAME_S);
        oPhone = i.getStringExtra(PHONE_S);

        spinner = (Spinner) findViewById(R.id.spinner);
        button=(Button)findViewById(R.id.orderNow);
        tableSelect = findViewById(R.id.tableSelect);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Table 1");
        categories.add("Table 2");
        categories.add("Table 3");
        categories.add("Table 4");
        categories.add("Table 5");
        categories.add("Tabel 6");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
        final String s = item+" is selected";
        tableSelect.setText(s);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SelectTable.this,PayNow.class);
                intent.putExtra(TOTAL, amountTotal);
                intent.putExtra(ITEM_ID1, myid);
                intent.putExtra(NAME_S, oName);
                intent.putExtra(DATA,String.valueOf(spinner.getSelectedItem()));
                intent.putExtra(PHONE_S, oPhone);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Selected the table first ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent(getApplicationContext(), CartPage.class);
        intent.putExtra(ITEM_ID1, myid);
        intent.putExtra(NAME_S, oName);
        intent.putExtra(PHONE_S, oPhone);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }

}
