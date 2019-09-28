package com.example.rohitranjan.foodonbook;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FetchOrder extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private List<pojoFetch> imgList;
    private ListView lv,lvIem;
    public static final String ITEM_ID4 = "itemid4";
    private fetchAdapter adapter;
    List<String> Item,qnty,price;
    LinearLayout linear1,linear2,linear3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fetch_order);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("adminFetch");
        //DatabaseReference mDataitem = FirebaseDatabase.getInstance().getReference("adminFetch").child("arrayItemName");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imgList.clear();
                //fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    pojoFetch img = snapshot.getValue(pojoFetch.class);
                    imgList.add(img);
                    GenericTypeIndicator<List<String>> gti = new GenericTypeIndicator<List<String>>() {};
                     Item = snapshot.child("arrayItemName").getValue(gti);
                     qnty = snapshot.child("arrayQuantity").getValue(gti);
                     price = snapshot.child("arrayPrice").getValue(gti);
                }
                adapter = new fetchAdapter(FetchOrder.this, R.layout.fetch_list, imgList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                pojoFetch pojoFetch = imgList.get(i);
                showUpdateDialog(pojoFetch.getMyid());
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pojoFetch pojoFetch = imgList.get(i);
                Intent intent = new Intent(getApplicationContext(),userItem.class);
                intent.putExtra(ITEM_ID4, pojoFetch.getMyid());
                startActivity(intent);
            }
        });
    }

    private void showUpdateDialog(final String itemId){
        Toast.makeText(this, itemId, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final  View dialogView = inflater.inflate(R.layout.custom_popup, null);

        dialogBuilder.setView(dialogView);

        final Button ButtonDelete = dialogView.findViewById(R.id.txtDelete);
        final Button ButtonClose = dialogView.findViewById(R.id.txtClose);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        ButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(itemId);
                alertDialog.dismiss();
            }
        });
        ButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void deleteUser(String itemId){
        DatabaseReference drItem = FirebaseDatabase.getInstance().getReference("adminFetch").child(itemId);
        drItem.removeValue();
        Toast.makeText(this, "item is deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        /*assert myid!=null;
        //startActivity(new Intent(this,userCategory.class));
        Intent intent = new Intent(getApplicationContext(), userCategory.class);
        intent.putExtra(ITEM_ID1, myid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        return true;
    }
}
