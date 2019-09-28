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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CartPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    private List<PojoCart> imgList;
    private ListView lv;
    private ProgressDialog progressDialog;
    public static final String CART_DATABASE_PATH = "cartPage";
    public static final String USER_DATABASE_PATH = "Users";
    public static final String ITEM_ID1 = "itemid2";
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private cartAdapter adapter;
    TextView total;
    String myid,s,id_name,id_phone;
    String oName,oPhone,oEmail,oTotal;
    public static final String TOTAL = "total";
    public static final String NAME_S = "nameS";
    public static final String PHONE_S = "phoneS";
    public static final String ITEMNAME_S = "arrayItemName";
    public static final String QUANTITY_S = "arrayQuantity";
    public static final String PRICE_S = "arrayPrice";

    public static final String SEND_NAME = "sendName";
    public static final String SEND_PHONE = "sendPhone";

    ArrayList<String> arrayItemName;
    ArrayList<String> arrayQuantity;
    ArrayList<String> arrayPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart_page);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        myid = i.getStringExtra(ITEM_ID1);
        id_name = i.getStringExtra(SEND_NAME);
        id_phone = i.getStringExtra(SEND_PHONE);
        Toast.makeText(this, id_name+""+id_phone, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);
        total = (TextView)findViewById(R.id.priceAll);

        //show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading image....");
        progressDialog.show();

        //idPrivate = getSharedPreferences(SignupActivity.ID_PRIVATE, Context.MODE_PRIVATE);
        //data = idPrivate.getString(SignupActivity.KEY_PRIVATE, "NA");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(CART_DATABASE_PATH).child(myid);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                imgList.clear();
                //fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Imageupload class require default constructor
                    PojoCart img = snapshot.getValue(PojoCart.class);
                    imgList.add(img);
                    int sum = 0;
                    ArrayList<String> total = new ArrayList<>();
                    for (int i = 0;i<imgList.size();i++){
                        total.add(String.valueOf(imgList.get(i).getStoreTotal()));
                        sum += Integer.valueOf(total.get(i));
                        //Toast.makeText(CartPage.this, total.get(i), Toast.LENGTH_SHORT).show();
                    }
                    s= String.valueOf(sum);

                }
                Toast.makeText(CartPage.this, s, Toast.LENGTH_SHORT).show();
                total.setText(s);
                //Init adapter
                adapter = new cartAdapter(CartPage.this, R.layout.cart_item, imgList);
                //set adapter for list view
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PojoCart pojoCart = imgList.get(i);
                showUpdateDialog(pojoCart.getNewId());
                return false;
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
        DatabaseReference drItem = FirebaseDatabase.getInstance().getReference(CART_DATABASE_PATH).child(myid).child(itemId);
        DatabaseReference drnewItem = FirebaseDatabase.getInstance().getReference("OrderPath").child(myid).child(itemId);
        drItem.removeValue();
        drnewItem.removeValue();
        Toast.makeText(this, "Cart item is deleted", Toast.LENGTH_SHORT).show();
    }



    public void OrderNow(View view) {
        oName = id_name;
        oPhone = id_phone;
        Toast.makeText(this, oName+"   "+oPhone, Toast.LENGTH_SHORT).show();
        /*DatabaseReference mDataCartRef = FirebaseDatabase.getInstance().getReference("OrderPath").child(myid);
        oTotal = total.getText().toString();
        pojoOrder order_pojo = new pojoOrder(oName,oPhone,oTotal);
        mDataCartRef.setValue(order_pojo);
        Toast.makeText(getApplicationContext(), "Order Successful!", Toast.LENGTH_SHORT).show();
        finish();*/
        Intent intent = new Intent(getApplicationContext(), SelectTable.class);
        intent.putExtra(TOTAL, total.getText().toString());
        intent.putExtra(ITEM_ID1, myid);
        intent.putExtra(NAME_S, oName);
        intent.putExtra(PHONE_S, oPhone);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void addMore(View view){
        Intent intent = new Intent(getApplicationContext(), userCategory.class);
        intent.putExtra(ITEM_ID1, myid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        //startActivity(new Intent(this,userCategory.class));
        Intent intent = new Intent(getApplicationContext(), userCategory.class);
        intent.putExtra(ITEM_ID1, myid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }

}
