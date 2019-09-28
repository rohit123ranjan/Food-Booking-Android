package com.example.rohitranjan.foodonbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminsubFetch extends AppCompatActivity {
    FirebaseAuth mAuth;
    private List<PojoCart> imgList;
    private ListView lv;
    public static final String ITEM_ID4 = "itemid4";
    private DatabaseReference mDatabaseRef;
    String id;
    private fetchItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adminsub_fetch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewItem);
        Intent i = getIntent();
        id = i.getStringExtra(ITEM_ID4);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("OrderFetch").child(id);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imgList.clear();

                //fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Imageupload class require default constructor
                    PojoCart img = snapshot.getValue(PojoCart.class);
                    imgList.add(img);
                }

                //Init adapter
                adapter = new fetchItemAdapter(AdminsubFetch.this, R.layout.fetch_item, imgList);
                //set adapter for list view
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
