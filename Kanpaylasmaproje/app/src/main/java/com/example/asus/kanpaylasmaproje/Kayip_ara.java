package com.example.asus.kanpaylasmaproje;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Map;

public class Kayip_ara extends AppCompatActivity {
    Button yukle, paylas;
    private String userID;
    private KayipPaylasModel kayipPaylasModel;
    private KisiModel kisiModel;
    private MesajModel mesajModel;
    private DatabaseReference mDatabase;
    private Map<String, Object> postValues1;
    private static final int RESIM_ISTEK= 123;

    private Bundle extras = null;

    String aciklama, iletisim, kresim,yakinlik;
    private static final int SELECT_SINGLE_PICTURE = 101;
    private Uri dosyaYolu;
    private ProgressDialog ekleDiyalog;
    private ImageView selectedImagePreview;
    private FirebaseAuth mKullanici;
    EditText aciklamaet, iletisimet;
    Uri selectedImageUri;
    private FirebaseStorage firebaseDepolama;
    Uri secilenResim;
    ImageView resim;
    CheckBox yakinlikcheck;
    TextView tasinanYazi;
    Spinner spinner,spinnerkategori;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapterkategori;
    private static final String TAG = "Kayip_ara";

    public Kayip_ara() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayip_ara);
        setTitle("KAYIP PAYLAŞ");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.yakinlik_spinner);
        spinnerkategori=(Spinner)findViewById(R.id.kategori);
        yakinlikcheck = (CheckBox)findViewById(R.id.yakinlik);
        adapterkategori = ArrayAdapter.createFromResource(this, R.array.kategori, android.R.layout.simple_spinner_item);
        adapterkategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkategori.setAdapter(adapterkategori);

        adapter = ArrayAdapter.createFromResource(this, R.array.country_names, android.R.layout.simple_selectable_list_item);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spinner.setAdapter(adapter);
        DatabaseReference ddRef1= FirebaseDatabase.getInstance().getReference().child("users");
        spinner.setVisibility(View.INVISIBLE);

        yakinlikcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    spinner.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("Not checked!!!!");
                    spinner.setVisibility(View.INVISIBLE);
                }
            }
        });
        aciklamaet = (EditText) findViewById(R.id.editaciklama);
        iletisimet = (EditText) findViewById(R.id.editiletisim);
      //  tasinanYazi = (TextView) findViewById(R.id.konumtut);
        yukle = (Button) findViewById(R.id.btnkayipresim);
        resim = (ImageView) findViewById(R.id.kayipresim);
        paylas = (Button) findViewById(R.id.btnpaylas);
        selectedImagePreview = (ImageView) findViewById(R.id.kayipresim);
       // tasinanYazi.setText(getIntent().getExtras().getString("konum"));

        final Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.RED)
                .borderWidthDp(4)
                .cornerRadiusDp(35)
                .oval(false)
                .build();

      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDepolama = FirebaseStorage.getInstance();
        mKullanici = FirebaseAuth.getInstance();
        userID = user.getUid();


        Log.d("userID:", userID);
        mDatabase= FirebaseDatabase.getInstance().getReference();

        yukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent i;
              //  i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(i, SELECT_SINGLE_PICTURE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), RESIM_ISTEK);

            }
        });

        paylas.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                if (kayipPaylasModel == null) {
                    kayipPaylasModel = new KayipPaylasModel();

                    kayipPaylasModel.setAciklama(aciklamaet.getText().toString());
                    kayipPaylasModel.setIletisim(iletisimet.getText().toString());
                    kayipPaylasModel.setPath(String.valueOf(dosyaYolu));
                    kayipPaylasModel.setId(userID);
                    kayipPaylasModel.setYakinlik(spinner.getSelectedItem().toString().trim());
                    kayipPaylasModel.setKategori(spinnerkategori.getSelectedItem().toString().trim());
                    mDatabase.child("kayipara").child(userID).child("aciklama").setValue(kayipPaylasModel.getAciklama());
                    mDatabase.child("kayipara").child(userID).child("iletisim").setValue(kayipPaylasModel.getIletisim());
                    mDatabase.child("kayipara").child(userID).child("path").setValue(kayipPaylasModel.getPath());
                    mDatabase.child("kayipara").child(userID).child("id").setValue(userID);
                    mDatabase.child("kayipara").child(userID).child("yakinlik").setValue(kayipPaylasModel.getYakinlik());
                    mDatabase.child("kayipara").child(userID).child("kategori").setValue(kayipPaylasModel.getKategori());

                    Toast.makeText(Kayip_ara.this, "Kaydedildi", Toast.LENGTH_SHORT).show();
                    Log.i("SAVE", "saveEntry: Kaydedildi.");
                    startActivity(new Intent(Kayip_ara.this, KayipListe.class));


                }

                if (dosyaYolu != null) {
                    ekleDiyalog = new ProgressDialog(Kayip_ara.this);
                    ekleDiyalog.setMessage("Paylaşım Ekleniyor...");
                    ekleDiyalog.setCancelable(false);
                    ekleDiyalog.show();
                    StorageReference storageRef = firebaseDepolama.getReference().child("paylasResim").child(mKullanici.getCurrentUser().getUid());
                    storageRef.putFile(dosyaYolu).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ekleDiyalog.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.lin2), "Kayıp Paylaşımı Eklendi!", Snackbar.LENGTH_LONG);
                            snackbar.show();

                            selectedImagePreview.setImageBitmap(null);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //progressDialog.dismiss();
                            Toast.makeText(Kayip_ara.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }

               // mDatabase.child("kayipara").child(userID).child("kullanici_adi").setValue(kisiModel.getKullanici_adi());
            String token= FirebaseInstanceId.getInstance().getToken();

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        public void FotografDegistir(View view) {
            Intent i;
            i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_SINGLE_PICTURE);

        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int istekKod, int sonucKod, Intent veri) {
        final Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .borderWidthDp(4)
                .cornerRadiusDp(35)
                .oval(false)
                .build();
            super.onActivityResult(istekKod, sonucKod, veri);

            if (istekKod == RESIM_ISTEK && sonucKod == RESULT_OK && veri != null && veri.getData() != null) {
                dosyaYolu = veri.getData();
                try {
                    Picasso.with(Kayip_ara.this).load(dosyaYolu).resize(850, 450).into(selectedImagePreview);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = Kayip_ara.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += Kayip_ara.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

}







