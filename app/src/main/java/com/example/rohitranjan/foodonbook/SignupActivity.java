package com.example.rohitranjan.foodonbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    DatabaseReference databaseReference;
    EditText editTextEmail, editTextPassword, editUserName, editPhoneNumber;
    private FirebaseAuth mAuth;
    String userId;
    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_PHONE = "itemPhone";
    public static final String ID_PRIVATE = "ID_PRIVATE";
    public static final String KEY_PRIVATE = "KEY_PRIVATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.emailSign);
        editTextPassword = findViewById(R.id.passSign);
        editUserName = findViewById(R.id.userText);
        editPhoneNumber = findViewById(R.id.numberText);
        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        findViewById(R.id.SignButton).setOnClickListener(this);
        findViewById(R.id.login_layout).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            //handle the already login user

        }
    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();
        final String username= editUserName.getText().toString().trim();
        final String phoneNumber= editPhoneNumber.getText().toString().trim();
        //final String id = databaseReference.push().getKey();

        if (username.isEmpty()||phoneNumber.isEmpty()||(phoneNumber.length() != 10)||email.isEmpty()||(!Patterns.EMAIL_ADDRESS.matcher(email).matches())||password.isEmpty()||(password.length()<6)){
            if (username.isEmpty())
            {
                editUserName.setError("Username is Required");
                editUserName.requestFocus();
            }
            if (phoneNumber.isEmpty()){
                editPhoneNumber.setError("Phone Number is Required");
                editPhoneNumber.requestFocus();
            }
            if (phoneNumber.length() != 10){
                editPhoneNumber.setError("Phone Number should be 10 digit");
                editPhoneNumber.requestFocus();
            }

            if (email.isEmpty()){
                editTextEmail.setError("Email is Required");
                editTextEmail.requestFocus();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextEmail.setError("Please enter a valid email");
                editTextEmail.requestFocus();
            }

            if (password.isEmpty()){
                editTextPassword.setError("Password is Required");
                editTextPassword.requestFocus();
            }

            if (password.length()<6){
                editTextPassword.setError("Minimum length of password should be 6");
                editTextPassword.requestFocus();
            }
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser user1 = mAuth.getCurrentUser();
                    if (user1 != null) {
                        userId = user1.getUid();
                    }

                    User user = new User(
                            username,
                            email,
                            phoneNumber,
                            userId
                    );
                    assert userId != null;
                    databaseReference.child(userId)
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                finish();
                                Toast.makeText(SignupActivity.this,"Registration Successful!" , Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                intent.putExtra(ITEM_NAME, username);
                                intent.putExtra(ITEM_PHONE, phoneNumber);
                                Toast.makeText(SignupActivity.this, username+"----"+phoneNumber, Toast.LENGTH_SHORT).show();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else {
                                //fail message
                                Toast.makeText(SignupActivity.this,"Some Error Occur!" , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SignButton:
                registerUser();
                break;
            case R.id.login_layout:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
