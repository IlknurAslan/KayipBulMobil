package com.example.asus.kanpaylasmaproje;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Map;

public class Bilgilerim extends AppCompatActivity {


    Button kaydet, foto;
    private String userID;
    private KisiModel kisiModel;
    private DatabaseReference mDatabase;
    private Map<String, Object> postValues;


    String kisiAd, kullanıcıAd, kresim;
    private static final int SELECT_SINGLE_PICTURE = 101;
    private ImageView selectedImagePreview;
    EditText isimET, kad;
    Uri selectedImageUri;
    TextView konumiki;
    TextView sehirkategori;
    Uri secilenResim;
    ImageView resim;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    private static final String TAG = "Bilgilerim";


    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgilerim);
        konumiki = (TextView) findViewById(R.id.bilgilerimkonum);
        isimET = (EditText) findViewById(R.id.isim);
        kad = (EditText) findViewById(R.id.kad);
        kaydet = (Button) findViewById(R.id.kayit);
        resim = (ImageView) findViewById(R.id.resim);
        foto = (Button) findViewById(R.id.fotodegistir);
        selectedImagePreview = (ImageView) findViewById(R.id.resim);
        //Intent i = getIntent(); // Bilgilerin önceki activityden alınması
      //  String sehiriki = i.getStringExtra("konum");
      //  Log.d("sehirmerakkk", sehiriki);

       // konumiki.setText(sehiriki);
        checkFilePermissions();

        final Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .borderWidthDp(4)
                .cornerRadiusDp(35)
                .oval(true)
                .build();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userID = user.getUid();

        Log.d("userID:", userID);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_SINGLE_PICTURE);

            }
        });
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (kisiModel == null) {
                    kisiModel = new KisiModel();
                    kisiModel.setAdSoyad(isimET.getText().toString());
                    kisiModel.setKullanici_adi(kad.getText().toString());
                    kisiModel.setPath(String.valueOf(selectedImageUri));
                    mDatabase.child("users").child(userID).setValue(kisiModel);
                    mDatabase.child("kayipara").child(userID).child("kullanici_adi").setValue(kisiModel.getKullanici_adi());
                    Toast.makeText(Bilgilerim.this, "Kaydedildi", Toast.LENGTH_SHORT).show();
                    Log.i("SAVE","saveEntry: Kaydedildi.");
                    startActivity(new Intent(Bilgilerim.this, Harita.class));

                } else {
                    kisiModel.setAdSoyad(isimET.getText().toString());
                    kisiModel.setKullanici_adi(kad.getText().toString());
                    kisiModel.setUid(mDatabase.child("users").child(userID).toString());
                    kisiModel.setPath(String.valueOf(selectedImageUri));
                    postValues = kisiModel.toMap();
                    kisiModel.setUid(mDatabase.child("users").child(userID).toString());
                    mDatabase.child("users").child(userID).updateChildren(postValues);
                    //mDatabase.child("kayipara").puzchild(userID).updateChildren(postValues);
                    Toast.makeText(Bilgilerim.this, "Güncellendi", Toast.LENGTH_SHORT).show();
                    Log.i("SAVE", "saveEntry: Güncellendi.");


                }
            }
        });
        mDatabase.child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KisiModel user = dataSnapshot.getValue(KisiModel.class);
                if (user == null) {
                    Toast.makeText(Bilgilerim.this, "Lütfen Profil bilgilerini ekleyiniz", Toast.LENGTH_SHORT).show();

                } else {

                    kisiAd = user.getAdSoyad();
                    kullanıcıAd = user.getKullanici_adi();
                    isimET.setText(kisiAd);
                    kad.setText(kullanıcıAd);
                    kresim = user.getPath();
                    secilenResim = Uri.parse(kresim);
                    Picasso.with(Bilgilerim.this).load(secilenResim).fit().transform(transformation).into(resim);
                }
            }

            public void FotografDegistir(View view) {
                Intent i;
                i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_SINGLE_PICTURE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Hata", "Hata mesajı");
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {
                selectedImageUri = data.getData();
                Picasso.with(Bilgilerim.this).load(selectedImageUri).fit().transform(transformation).into(selectedImagePreview);

            }

        } else {
            // report failure
            Toast.makeText(getApplicationContext(), "Failed to get intent data ", Toast.LENGTH_LONG).show();
            Log.d(MainActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = Bilgilerim.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += Bilgilerim.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

}



    //super.onActivityResult(requestCode,resultCode,data);


