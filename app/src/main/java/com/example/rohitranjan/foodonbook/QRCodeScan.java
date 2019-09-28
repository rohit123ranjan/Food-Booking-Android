package com.example.rohitranjan.foodonbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScan extends AppCompatActivity {
    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_PHONE = "itemPhone";
    public static final String ITEM_ID1 = "itemid2";
    String nameU,phoneU;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        Intent i = getIntent();
        nameU = i.getStringExtra(LoginActivity.ITEM_NAME);
        phoneU = i.getStringExtra(LoginActivity.ITEM_PHONE);
        userId = i.getStringExtra(LoginActivity.ITEM_ID1);

    }

    public void scanqr(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(QRCodeScan.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("scanning");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    public void withoutCont(View view){
        finish();
        //Toast.makeText(QRCodeScan.this,  "hello "+userId + " "+ nameU+ " "+phoneU, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(QRCodeScan.this, userCategory.class);
        intent.putExtra(ITEM_NAME, nameU);
        intent.putExtra(ITEM_PHONE, phoneU);
        intent.putExtra(ITEM_ID1, userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        //Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
        if (result!=null && result.getContents()!=null){
            if (result.getContents().equalsIgnoreCase("food")){
                Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(QRCodeScan.this)
                        .setTitle("Scan Result")
                        .setMessage(result.getContents())
                        .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                Intent intent = new Intent(QRCodeScan.this, userCategory.class);
                                intent.putExtra(ITEM_NAME, nameU);
                                intent.putExtra(ITEM_PHONE, phoneU);
                                intent.putExtra(ITEM_ID1, userId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
