   package com.example.rohitranjan.foodonbook;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

   public class QrcodeGenerator extends AppCompatActivity {

    ImageView img;
    Button genBn;
    String text2Qr;
    MultiFormatWriter multi = new MultiFormatWriter();
    FirebaseAuth mAuth;
    String textValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        img = (ImageView) findViewById(R.id.image);
        genBn = (Button)findViewById(R.id.generateButton);
        textValue = "food";
        //String s = textValue.get(0);


        genBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    BitMatrix bitMatrix = multi.encode(textValue, BarcodeFormat.QR_CODE, 300,300);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    img.setImageBitmap(bitmap);
                }catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });

    }
}
