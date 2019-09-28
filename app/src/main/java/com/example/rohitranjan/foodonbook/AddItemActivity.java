package com.example.rohitranjan.foodonbook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private List<ItemUpload> imgList;
    private DatabaseReference mDatabaseRef;
    private ListView lv;
    String id;
    private ItemListAdapter adapter;
    String uploadId;
    private ProgressDialog progressDialog,progressDialog1;
    FirebaseAuth mAuth;

    private StorageReference mStorageRef;
    private ImageView imageView;
    private EditText txtItemName,txtItemDesc,txtItemPrice;
    private Uri imgUri;

    public static final String FB_STORAGE_PATH = "listmenu/";
    public static final String FB_DATABASE_PATH = "listmenu";
    public static final int REQUEST_CODE = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //adding items
        imageView = findViewById(R.id.imageLoad);
        txtItemName = findViewById(R.id.editTextName);
        txtItemDesc = findViewById(R.id.editTextDesc);
        txtItemPrice = findViewById(R.id.editTextPrice);
        //-----------------------


        mAuth = FirebaseAuth.getInstance();

       // findViewById(R.id.addItem).setOnClickListener(this);

        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewItem);

        Intent i = getIntent();
        id = i.getStringExtra(ImageListActivity.ITEM_ID);

        //show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loading image....");
        progressDialog.show();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(AddItemActivity.FB_DATABASE_PATH).child(id);

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
                adapter = new ItemListAdapter(AddItemActivity.this, R.layout.admin_item, imgList);
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
                ItemUpload itemUpload = imgList.get(i);

                showUpdateDialog(itemUpload.getUserId());
                return false;
            }
        });
    }

    //show dialog box
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
        DatabaseReference drItem = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child(id).child(itemId);
        drItem.removeValue();

        Toast.makeText(this, "Item is deleted", Toast.LENGTH_SHORT).show();
    }
    //---------------------

    //adding new item

    public void btnBrowse_Click(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"), REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imgUri = data.getData();
            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imageView.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void btnUpload(View v){
        Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
        if (imgUri != null){
            progressDialog1 = new ProgressDialog(this);
            progressDialog1.setTitle("Uploading image");
            progressDialog1.show();

            //Get the storage
            final StorageReference ref = mStorageRef.child(FB_STORAGE_PATH + System.currentTimeMillis() + "."+getImageExt(imgUri));

            //Add file to reference

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //dimiss dialog when success
                            progressDialog1.dismiss();
                            uploadId = mDatabaseRef.push().getKey();
                            Toast.makeText(getApplicationContext(), "Item Saved Successfully!", Toast.LENGTH_SHORT).show();
                            ItemUpload itemUpload = new ItemUpload(txtItemName.getText().toString(), uri.toString(),uploadId,txtItemDesc.getText().toString(),txtItemPrice.getText().toString());

                            //save image info in to firebase database
                            mDatabaseRef.child(uploadId).setValue(itemUpload);
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //dimiss dialog when error
                    progressDialog1.dismiss();
                    //display success toast
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //Show upload progress

                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog1.setMessage("Uploaded" + (int)progress+"%");
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    //-----------------------------------------------------------

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
       /* switch (view.getId()){
            case R.id.addItem:

                break;
        }*/
    }
}