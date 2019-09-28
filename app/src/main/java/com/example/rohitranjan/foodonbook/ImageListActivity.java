package com.example.rohitranjan.foodonbook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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

public class ImageListActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String ITEM_NAME = "itemname";
    public static final String ITEM_ID = "itemid";
    private DatabaseReference mDatabaseRef;
    private List<ImageUpload> imgList;
    private StorageReference mStorageRef;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATA_PATH = "image";
    private SwipeMenuListView lv;
    private ImageView imageView;
    private ImageListAdapter adapter;

    private Uri imgUri;
    public static final int REQUEST_CODE = 1234;
    private ProgressDialog progressDialog;
    private TextView username,email,phone;
    String nameU,phoneU,id1;

    DatabaseReference rootRef, demoRef;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_list);

        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        ((TextView) findViewById(R.id.home_click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageListActivity.this,ImageListActivity.class);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.myOrder_click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageListActivity.this,FetchOrder.class));
            }
        });
        ((TextView) findViewById(R.id.QrCode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageListActivity.this,QrcodeGenerator.class));
            }
        });
        ((TextView) findViewById(R.id.logOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ImageListActivity.this,LoginActivity.class));
            }
        });

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);


        loadUserInformation(); //displaying user profile menu

        findViewById(R.id.addCategory).setOnClickListener(this);

        imgList = new ArrayList<>();
        lv = (SwipeMenuListView) findViewById(R.id.listViewImage);

        //show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading image....");
        progressDialog.show();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(MainActivity.FB_DATABASE_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                imgList.clear();
                //fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Imageupload class require default constructor
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }

                //Init adapter
                adapter = new ImageListAdapter(ImageListActivity.this, R.layout.image_item, imgList);
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
                Intent intent = new Intent(getApplicationContext(),AddItemActivity.class);
                intent.putExtra(ITEM_ID, imageUpload.getUserId());
                intent.putExtra(ITEM_NAME, imageUpload.getName());
                startActivity(intent);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                //deleteItem.setBackground(new ColorDrawable(Color.rgb(0xff,
                    //    0xff, 0xff)));
                deleteItem.setWidth(150);
                deleteItem.setIcon(R.drawable.a_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        lv.setMenuCreator(creator);

        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ImageUpload itemUpload = imgList.get(position);

                        showUpdateDialog(itemUpload.getUserId());
                        break;
                }
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
        DatabaseReference drItem = FirebaseDatabase.getInstance().getReference(FB_DATA_PATH).child(itemId);
        drItem.removeValue();
        DatabaseReference drItem1 = FirebaseDatabase.getInstance().getReference("listmenu").child(itemId);
        drItem1.removeValue();
        Toast.makeText(this, "Item is deleted", Toast.LENGTH_SHORT).show();
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
        nameU = i.getStringExtra(LoginActivity.ITEM_NAME);
        phoneU = i.getStringExtra(LoginActivity.ITEM_PHONE);
        id1 = i.getStringExtra(LoginActivity.ITEM_ID1);

        rootRef = FirebaseDatabase.getInstance().getReference("Users");
        //database reference pointing to demo node
        if(id1 != null){
            demoRef = rootRef.child(id1);

            demoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pojoProfile value = dataSnapshot.getValue(pojoProfile.class);
                    if(value != null) {
                        Toast.makeText(ImageListActivity.this, value.getUsername(), Toast.LENGTH_SHORT).show();
                        username.setText(value.getUsername());
                        phone.setText(value.getPhoneNumber());
                    }else{
                        Toast.makeText(ImageListActivity.this, "NO Data found!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }if(nameU != null || phoneU != null){

            Toast.makeText(ImageListActivity.this, nameU + "---" + phoneU, Toast.LENGTH_SHORT).show();
            username.setText(nameU);
            phone.setText(phoneU);

        }



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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addCategory:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }


}