package com.example.rohitranjan.foodonbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.List;

public class userCategory extends AppCompatActivity {

    public static final String ITEM_NAME = "itemname";
    public static final String ITEM_ID = "itemid";
    public static final String ITEM_ID1 = "itemid2";
    public static final String SEND_NAME = "sendName";
    public static final String SEND_PHONE = "sendPhone";
    public static final String ITEM_ID2 = "itemid3";
    private TextView username,email,phone;
    public static final String myPreference = "IdPref";
    private List<ImageUpload> imgList;
    private ListView lv;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private ImageListAdapter adapter;
    public static final String FB_DATABASE_PATH = "image";
    public static final String REQUEST = "123";
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    DatabaseReference rootRef, demoRef;
    String nameU,phoneU,id1,myid,newid,resultRequest;
    String sendName,sendPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_category);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        myid = i.getStringExtra(ITEM_ID1);
        newid = i.getStringExtra(ITEM_ID2);
        resultRequest = i.getStringExtra("code");

        Toast.makeText(this, resultRequest, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();

        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.user_menu_left)
                .inject();

        ((TextView) findViewById(R.id.home_click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userCategory.this,userCategory.class);
                intent.putExtra(ITEM_ID1, myid);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.myOrder_click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userCategory.this,OrderActivity.class);
                intent.putExtra(ITEM_ID2, newid);
                intent.putExtra(ITEM_ID1, myid);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.myCart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                id1 = i.getStringExtra(ITEM_ID1);
                rootRef = FirebaseDatabase.getInstance().getReference("Users");
                if(id1 != null){
                    demoRef = rootRef.child(id1);
                    demoRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pojoProfile value = dataSnapshot.getValue(pojoProfile.class);
                            if(value != null) {
                                //finish();
                                //Toast.makeText(userCategory.this, value.getUsername(), Toast.LENGTH_SHORT).show();
                                username.setText(value.getUsername());
                                phone.setText(value.getPhoneNumber());
                                Intent intent = new Intent(userCategory.this, CartPage.class);
                                intent.putExtra(ITEM_ID1, myid);
                                intent.putExtra(SEND_NAME, value.getUsername());
                                intent.putExtra(SEND_PHONE, value.getPhoneNumber());
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                //Toast.makeText(userCategory.this, sendPhone +""+sendName, Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(userCategory.this, "NO Data found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
        ((TextView) findViewById(R.id.logOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(LoginActivity.idUser);
                //editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(userCategory.this,LoginActivity.class));
            }
        });

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        loadUserInformation(); //displaying user profile menu

        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);

        //show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading image....");
        progressDialog.show();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                //fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Imageupload class require default constructor
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }

                //Init adapter
                adapter = new ImageListAdapter(userCategory.this, R.layout.image_item, imgList);
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
                ImageUpload imageUpload = imgList.get(i);
                Intent intent = new Intent(getApplicationContext(),userItem.class);
                intent.putExtra(ITEM_ID, imageUpload.getUserId());
                intent.putExtra(ITEM_NAME, imageUpload.getName());
                intent.putExtra(ITEM_ID1, myid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null){
            //handle the already login user
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            if(user.getEmail() != null){
                email.setText(user.getEmail());
            }
        }
        Intent i = getIntent();
        nameU = i.getStringExtra(QRCodeScan.ITEM_NAME);
        phoneU = i.getStringExtra(QRCodeScan.ITEM_PHONE);
        id1 = i.getStringExtra(QRCodeScan.ITEM_ID1);

        rootRef = FirebaseDatabase.getInstance().getReference("Users");
        //database reference pointing to demo node

        if(id1 != null){

            demoRef = rootRef.child(id1);
            Toast.makeText(userCategory.this, id1, Toast.LENGTH_SHORT).show();
            demoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pojoProfile value = dataSnapshot.getValue(pojoProfile.class);
                    if(value != null) {
                        Toast.makeText(userCategory.this, value.getUsername(), Toast.LENGTH_SHORT).show();
                        username.setText(value.getUsername());
                        phone.setText(value.getPhoneNumber());
                    }else{
                        Toast.makeText(userCategory.this, "NO Data found!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        /*if(nameU != null || phoneU != null){

            Toast.makeText(userCategory.this, nameU + "---" + phoneU, Toast.LENGTH_SHORT).show();
            username.setText(nameU);
            phone.setText(phoneU);

        }*/

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
