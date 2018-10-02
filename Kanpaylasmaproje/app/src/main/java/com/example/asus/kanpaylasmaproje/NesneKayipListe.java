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

public class NesneKayipListe extends AppCompatActivity {
    String kategori="nesne";
    private FirebaseAuth firebaseKullanicilar;
    private DatabaseReference veritabaniReferans;
    private ListView list;
    private NesneKayipAdapter nesneKayipAdapter;
    private List<KayipPaylasModel> nesnepaylasilanList;
    private ProgressDialog yüklemeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nesne_kayip_liste);
        setTitle("NESNE KAYIP LİSTESİ");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.listnesnekayip);

       nesnepaylasilanList = new ArrayList<>();

        firebaseKullanicilar = FirebaseAuth.getInstance();
        veritabaniReferans = FirebaseDatabase.getInstance().getReference("kayipara");

        yüklemeDialog = new ProgressDialog(NesneKayipListe.this);
        yüklemeDialog.setMessage("Yükleniyor...");
        yüklemeDialog.setCancelable(false);
        yüklemeDialog.show();

        DatabaseReference ddRef= FirebaseDatabase.getInstance().getReference().child("kayipara");
        DatabaseReference ddRef1= FirebaseDatabase.getInstance().getReference().child("users");

        veritabaniReferans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                yüklemeDialog.dismiss();
                nesnepaylasilanList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (kategori.equals(postSnapshot.child("kategori").getValue().toString())) {
                        KayipPaylasModel userObj = postSnapshot.getValue(KayipPaylasModel.class);
                        nesnepaylasilanList.add(userObj);
                    }
                }
                nesneKayipAdapter = new NesneKayipAdapter(getApplicationContext(),nesnepaylasilanList);
                list.setAdapter(nesneKayipAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                yüklemeDialog.dismiss();
                Toast.makeText(NesneKayipListe.this, "DATABASE error", Toast.LENGTH_SHORT).show();
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
        if(nesneKayipAdapter!=null){
            nesneKayipAdapter.notifyDataSetChanged();
        }
    }

}