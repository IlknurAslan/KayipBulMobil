package com.example.asus.kanpaylasmaproje;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InsanKayipListe extends AppCompatActivity {

    // List<String> arrList = new ArrayList<String>();
    //  KayipListeAdapter adapter;
    // KayipListeAdapter adapter1;
    String kategori="insan";
    private FirebaseAuth firebaseKullanicilar;
    private DatabaseReference veritabaniReferans;
    private ListView list;
    private KayipListeAdapter insankayipListeAdapter;
    private List<KayipPaylasModel> insanpaylasilanList;
    private ProgressDialog yüklemeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insan_kayip_liste);
        setTitle("İNSAN KAYIP LİSTESİ");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.insanlistkayip);

        insanpaylasilanList = new ArrayList<>();

        firebaseKullanicilar = FirebaseAuth.getInstance();
        veritabaniReferans = FirebaseDatabase.getInstance().getReference("kayipara");

        yüklemeDialog = new ProgressDialog(InsanKayipListe.this);
        yüklemeDialog.setMessage("Yükleniyor...");
        yüklemeDialog.setCancelable(false);
        yüklemeDialog.show();

        DatabaseReference ddRef= FirebaseDatabase.getInstance().getReference().child("kayipara");
        DatabaseReference ddRef1= FirebaseDatabase.getInstance().getReference().child("users");

        veritabaniReferans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                yüklemeDialog.dismiss();
                insanpaylasilanList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (kategori.equals(postSnapshot.child("kategori").getValue().toString())) {
                        KayipPaylasModel userObj = postSnapshot.getValue(KayipPaylasModel.class);
                        insanpaylasilanList.add(userObj);
                    }
                }

                insankayipListeAdapter = new KayipListeAdapter(getApplicationContext(),insanpaylasilanList);
                list.setAdapter(insankayipListeAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                yüklemeDialog.dismiss();
                Toast.makeText(InsanKayipListe.this, "DATABASE error", Toast.LENGTH_SHORT).show();
            }
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
    @Override
    protected void onResume() {
        super.onResume();
        if(insankayipListeAdapter!=null){
            insankayipListeAdapter.notifyDataSetChanged();
        }
    }

}

