package com.example.adnane.tp4;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adnane.tp4.bd.MyDatabase;
import com.example.adnane.tp4.bd.Etablissement;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

public class Create extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 1;
    private final static String ETAB_NAME = "NAME";
    private final static String ETAB_LABEL = "LABEL";
    private final static String ETAB_IMG = "IMG";
    static Etablissement etablissement = new Etablissement();
    static MyDatabase mydatabase;
    boolean UPDATE = false;
    ImageView image;
    String image_base64;
    EditText label, name;

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void myClick(View v){
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    public void lastClick(View v){
        if(!(label.getText().toString()).matches("") && !(name.getText().toString()).matches("") && !(image_base64.matches(""))){
           // MainActivity.mydatabase.mydao().getUser(login.getText().toString());
            etablissement.setImage(image_base64);
            etablissement.setLabel(label.getText().toString());
            etablissement.setName(name.getText().toString());
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);
            List<Address> listOfAddress;
            try {
                listOfAddress = geocoder.getFromLocationName(label.getText().toString(), 1);
                if(listOfAddress != null && !listOfAddress.isEmpty()){
                    Address address = listOfAddress.get(0);
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();
                    etablissement.setLatitude(latitude);
                    etablissement.setLongitude(longitude);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            if(UPDATE){Create.mydatabase.mydao2().updateEtablissement(etablissement);}
            else {Create.mydatabase.mydao2().addEtablissement(etablissement);}
            Intent intent = new Intent(this, Mylist.class);
            startActivity(intent);
        }
        else if((label.getText().toString()).matches("")){
            Toast.makeText(this, "Labes is missing", Toast.LENGTH_SHORT).show();
        }
        else if((name.getText().toString()).matches("")){
            Toast.makeText(this, "Name is missing", Toast.LENGTH_SHORT).show();
        }
        else if(image_base64.matches("")){
            Toast.makeText(this, "Image is missing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Créer Etablissement");
        setContentView(R.layout.create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mydatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "user_bd").allowMainThreadQueries().build();
        Button upload = (Button) findViewById(R.id.Upload);
        Button submit = (Button) findViewById(R.id.submit);
        label = (EditText) findViewById(R.id.editText2);
        name = (EditText) findViewById(R.id.editText4);
        image = (ImageView) findViewById(R.id.defaultImage);
        if(getIntent().getExtras() != null){
            Intent intent = getIntent();
            name.setText(intent.getStringExtra(ETAB_NAME));
            label.setText(intent.getStringExtra(ETAB_LABEL));
            image_base64 = intent.getStringExtra(ETAB_IMG);
            Bitmap bitmap = decodeBase64(image_base64);
            image.setImageBitmap(bitmap);
            UPDATE = true;
        }
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myClick(v);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lastClick(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmap = Bitmap.createScaledBitmap(bitmap, 110, 95, true);
                image.setImageBitmap(bitmap);
                // still modifiable ^^
                image_base64 = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
