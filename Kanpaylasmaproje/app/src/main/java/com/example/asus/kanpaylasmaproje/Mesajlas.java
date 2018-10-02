package com.example.asus.kanpaylasmaproje;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Mesajlas extends AppCompatActivity {

    EditText et_mesaj;
    Button gonder;
    ListView lv_chatyap;
    FirebaseDatabase database;
    private String userID;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesajlas);


        setTitle("MESAJLAŞMA");

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_mesaj=(EditText) findViewById(R.id.mesajyaz);
        gonder=(Button)findViewById(R.id.gonder);
        lv_chatyap=(ListView)findViewById(R.id.mesaj);
        lv_chatyap.setDivider(null);///cizgileri siler

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase =FirebaseDatabase.getInstance().getReference().child("kayipara");
        userID = user.getUid();


        final ArrayList<MesajModel> mesajList=new ArrayList<MesajModel>();
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        database=FirebaseDatabase.getInstance();
        final DatabaseReference dbRef=database.getReference("kayipara").child(userID).child("mesajlar");

        gonder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api= Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                String gonderen=firebaseUser.getEmail();
                String mesaj=et_mesaj.getText().toString();
                SimpleDateFormat adf=new SimpleDateFormat("HH:mm:dd");
                String zaman=adf.format(new Date());

                dbRef.push().setValue(new MesajModel(gonderen,mesaj,zaman));
                et_mesaj.setText("");
            }
        });
        final CustomAdapter adapter=new CustomAdapter(this,mesajList,firebaseUser);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mesajList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    mesajList.add(ds.getValue(MesajModel.class));
                }
                lv_chatyap.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}
