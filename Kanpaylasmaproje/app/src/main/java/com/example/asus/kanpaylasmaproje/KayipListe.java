package com.example.asus.kanpaylasmaproje;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
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

public class KayipListe extends AppCompatActivity {

   // List<String> arrList = new ArrayList<String>();
  //  KayipListeAdapter adapter;
   // KayipListeAdapter adapter1;
    String kategori="insan";
    private FirebaseAuth firebaseKullanicilar;
    private DatabaseReference veritabaniReferans;
    private ListView list;
    private Button resimmesaj;
    private KayipListeAdapter kayipListeAdapter;
    private List<KayipPaylasModel> paylasilanList;
    private ProgressDialog yüklemeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayip_liste);
       //getSupportActionBar().setTitle(R.string.Kayip);
        setTitle("KAYIP PAYLAŞIMLARI");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    
      //  final ArrayList<KayipPaylasModel> Kayiplist = new ArrayList<KayipPaylasModel>();
       // final ArrayList<KisiModel> Kayiplist1 = new ArrayList<KisiModel>();
        list = (ListView) findViewById(R.id.listkayip);
       // final ListView listView =(ListView)findViewById(R.id.listkayip);
        paylasilanList = new ArrayList<>();

        firebaseKullanicilar = FirebaseAuth.getInstance();
        veritabaniReferans = FirebaseDatabase.getInstance().getReference("kayipara");

        yüklemeDialog = new ProgressDialog(KayipListe.this);
        yüklemeDialog.setMessage("Yükleniyor...");
        yüklemeDialog.setCancelable(false);
        yüklemeDialog.show();

        DatabaseReference ddRef= FirebaseDatabase.getInstance().getReference().child("kayipara");
        DatabaseReference ddRef1= FirebaseDatabase.getInstance().getReference().child("users");

        veritabaniReferans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                yüklemeDialog.dismiss();
                paylasilanList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    KayipPaylasModel userObj = postSnapshot.getValue(KayipPaylasModel.class);
                    paylasilanList.add(userObj);
                }

                kayipListeAdapter = new KayipListeAdapter(getApplicationContext(),paylasilanList);
                list.setAdapter(kayipListeAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                yüklemeDialog.dismiss();
                Toast.makeText(KayipListe.this, "DATABASE error", Toast.LENGTH_SHORT).show();
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
        if(kayipListeAdapter!=null){
            kayipListeAdapter.notifyDataSetChanged();
        }
    }

}

