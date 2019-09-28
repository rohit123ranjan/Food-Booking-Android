package com.example.rohitranjan.foodonbook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class userItem extends AppCompatActivity {

    FirebaseAuth mAuth;
    private List<ItemUpload> imgList;
    private ListView lv;
    public static final String ITEM_ID1 = "itemid2";
    public static final String ITEM_ID = "itemid";
    String id,myid;
    //String data;
    private ItemListAdapter adapter;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,mDataCartRef;
    public static final String FB_DATABASE_PATH = "listmenu";
    public static final String CART_DATABASE_PATH = "cartPage";

    SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog,progressDialog1;
    //item dialog
    int minteger = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        mAuth = FirebaseAuth.getInstance();

        // findViewById(R.id.addItem).setOnClickListener(this);

        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewItem);

        Intent i = getIntent();
        myid = i.getStringExtra(ITEM_ID1);
        id = i.getStringExtra(ITEM_ID);


        //show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading image....");
        progressDialog.show();
        //idPrivate = getSharedPreferences(SignupActivity.ID_PRIVATE, Context.MODE_PRIVATE);
        //data = idPrivate.getString(SignupActivity.KEY_PRIVATE, "NA");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child(id);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                imgList.clear();

                //fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Imageupload class require default constructor
                    ItemUpload img = snapshot.getValue(ItemUpload.class);
                    imgList.add(img);
                }

                //Init adapter
                adapter = new ItemListAdapter(userItem.this, R.layout.admin_item, imgList);
                //set adapter for list view
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemUpload itemUpload = imgList.get(i);
                showAddCart(itemUpload.getUserId(), itemUpload.getName(), itemUpload.getUrl(), itemUpload.getInfoItem(), itemUpload.getPriceItem());
            }
        });

    }

    private void showAddCart(final String itemId, final String itemName, final String imageUrl,
                             final String itemInfo, final String itemPrice){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.category_dialog, null);

        dialogBuilder.setView(dialogView);

        final TextView itemTextName = dialogView.findViewById(R.id.itemNameValue);
        final TextView priceTag = dialogView.findViewById(R.id.priceTag);
        final TextView descTag = dialogView.findViewById(R.id.descTag);
        final ImageView imageLoad = dialogView.findViewById(R.id.imageLoad);
        final TextView increaseInteger = dialogView.findViewById(R.id.increase);
        final TextView decreaseInteger = dialogView.findViewById(R.id.decrease);
        final TextView displayInteger = dialogView.findViewById(R.id.integer_number);
        final Button addToCart = dialogView.findViewById(R.id.addToCart);

        increaseInteger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = minteger + 1;
                if (minteger > 10){
                    minteger = 10;
                }
                displayInteger.setText(String.valueOf(minteger));
            }
        });
        decreaseInteger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = minteger - 1;
                if (minteger < 0 ){
                    minteger = 0;
                }
                displayInteger.setText(String.valueOf(minteger));
            }
        });

        itemTextName.setText(itemName);
        priceTag.setText(itemPrice);
        descTag.setText(itemInfo);
        Glide.with(dialogView).load(imageUrl).into(imageLoad);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(userItem.this, myid, Toast.LENGTH_SHORT).show();

                //sharedPreferences = getSharedPreferences(LoginActivity.myPreference,Context.MODE_PRIVATE);
                //myid = sharedPreferences.getString(LoginActivity.idUser,"NA");

                mDataCartRef = FirebaseDatabase.getInstance().getReference(CART_DATABASE_PATH).child(myid);
                DatabaseReference newOrderRef = FirebaseDatabase.getInstance().getReference("OrderPath").child(myid);
                DatabaseReference newOrderRef1 = FirebaseDatabase.getInstance().getReference("OrderFetch").child(myid);
                final String uploadId = mDataCartRef.push().getKey();
                final String totalPrice =  String.valueOf(Integer.valueOf(displayInteger.getText().toString()) * Integer.valueOf(itemPrice));
                final String quantity = displayInteger.getText().toString();
                PojoCart pojoCart = new PojoCart(itemId,uploadId,itemTextName.getText().toString(),
                        imageUrl,descTag.getText().toString(), priceTag.getText().toString(),
                        quantity,totalPrice);
                assert uploadId != null;
                mDataCartRef.child(uploadId).setValue(pojoCart);
                newOrderRef.child(uploadId).setValue(pojoCart);
                newOrderRef1.child(uploadId).setValue(pojoCart);
                Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                finish();
                alertDialog.dismiss();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
