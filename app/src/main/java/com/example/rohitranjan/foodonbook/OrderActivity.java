package com.example.rohitranjan.foodonbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    public static final String ITEM_ID2 = "itemid3";
    public static final String ITEM_ID1 = "itemid2";
    public static final String REQUEST = "123";
    public static final String ORDER_DATABASE_PATH = "OrderPath";
    public static final String CART_DATABASE_PATH = "cartPage";
    private DatabaseReference mDatabaseRef,cartDatabaseRef;

    public static final String TOTAL = "total";
    public static final String NAME_S = "nameS";
    public static final String PHONE_S = "phoneS";
    public static final String DATA = "data";
    public static final String RATING = "rating";
    TextView Username,number,amountValue,tableNum,ratingbar;
    private List<PojoCart> imgList;
    private orderAdapter adapter;
    private ListView lv;
    String myid;
    String amountTotal,oName,oPhone,dataTable,rating;

    ArrayList<String> arrayItemName;
    ArrayList<String> arrayQuantity;
    ArrayList<String> arrayPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Username = findViewById(R.id.Username);
        number = findViewById(R.id.number);
        amountValue = findViewById(R.id.amountValue);
        tableNum = findViewById(R.id.tableNum);
        ratingbar = findViewById(R.id.ratingbar);

        Intent i = getIntent();
        myid = i.getStringExtra(ITEM_ID2);
        amountTotal = i.getStringExtra(TOTAL);
        oName = i.getStringExtra(NAME_S);
        oPhone = i.getStringExtra(PHONE_S);
        dataTable = i.getStringExtra(DATA);
        rating = i.getStringExtra(RATING);

        Username.setText(oName);
        number.setText(oPhone);
        amountValue.setText(amountTotal);
        tableNum.setText(dataTable);
        ratingbar.setText(rating);
        lv = (ListView) findViewById(R.id.listViewImage);

        if (myid != null){
            imgList = new ArrayList<>();
            cartDatabaseRef = FirebaseDatabase.getInstance().getReference("OrderPath").child(myid);
            cartDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    imgList.clear();
                    //fetch image data from firebase database
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Imageupload class require default constructor
                        PojoCart img = snapshot.getValue(PojoCart.class);
                        imgList.add(img);
                        arrayItemName = new ArrayList<>();
                        arrayPrice = new ArrayList<>();
                        arrayQuantity = new ArrayList<>();
                        for (int i = 0;i<imgList.size();i++){
                            arrayItemName.add(String.valueOf(imgList.get(i).getItemName()));
                            arrayPrice.add(String.valueOf(imgList.get(i).getStoreTotal()));
                            arrayQuantity.add(String.valueOf(imgList.get(i).getStoreQuantity()));
                        }
                    }
                    Toast.makeText(OrderActivity.this, arrayItemName.get(0), Toast.LENGTH_SHORT).show();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("adminFetch");
                    //final String uploadId = mDatabaseRef.push().getKey();
                    pojoFetch pojofetch = new pojoFetch(myid,oName,oPhone,amountTotal,dataTable,rating,arrayItemName,arrayPrice,arrayQuantity);
                    assert myid != null;
                    mDatabaseRef.child(myid).setValue(pojofetch);
                    Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                    //Init adapter
                    adapter = new orderAdapter(OrderActivity.this, R.layout.order_list, imgList);
                    //set adapter for list view
                    lv.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference cartDelRef = FirebaseDatabase.getInstance().getReference("cartPage").child(myid);
            cartDelRef.removeValue();
        }
        //Toast.makeText(this, arrayItemName.get(0), Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        assert myid!=null;
        //startActivity(new Intent(this,userCategory.class));
        DatabaseReference orderPath = FirebaseDatabase.getInstance().getReference("OrderPath").child(myid);
        orderPath.removeValue();
        Intent intent = new Intent(getApplicationContext(), userCategory.class);
        intent.putExtra(ITEM_ID2, myid);
        intent.putExtra(ITEM_ID1, myid);
        intent.putExtra("code", REQUEST);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }
}
