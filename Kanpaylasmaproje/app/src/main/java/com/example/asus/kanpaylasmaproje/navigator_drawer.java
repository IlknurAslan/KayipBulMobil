package com.example.asus.kanpaylasmaproje;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class navigator_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener authStateListener;
    TextView email;
    Intent intent;
    Uri secilenResim;
    private String userID,kresim;
    private KisiModel kisiModel;
    private DatabaseReference mDatabase;
    ImageView resim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationVie = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationVie.getHeaderView(0);
        RunAnimation();
        resim=(ImageView)headerView.findViewById(R.id.resim);
       // Content alanına fragment yüklemek için
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userID = user.getUid();

        Log.d("userID:", userID);

        //Profil resmini oval yapmayı sağlayan metod.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();


        mDatabase.child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KisiModel user = dataSnapshot.getValue(KisiModel.class);
                if(user==null){

                    Toast.makeText(navigator_drawer.this, "Lütfen Profil bilgilerini ekleyiniz", Toast.LENGTH_SHORT).show();

                }
                else{

                    kresim=user.getPath();
                    Log.d("userpath",kresim);
                    secilenResim= Uri.parse(kresim);
                    Picasso.with(navigator_drawer.this).load(secilenResim).fit().transform(transformation).into(resim);
                }
            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hata","Hata mesajı");
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigator_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profil) {
            Intent intent = new Intent(this,Bilgilerim.class);
            startActivity(intent);

        } else if (id == R.id.nav_bildirimler) {
            Intent intent = new Intent(this,KayipListe.class);
            startActivity(intent);


        } else if (id == R.id.nav_haberpaylas) {
            Intent intent = new Intent(this,Kayip_ara.class);
            startActivity(intent);

        } else if (id == R.id.nav_mesajlar) {
            Intent intent = new Intent(this,Mesajlas.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_hayvan) {
            Intent intent = new Intent(this,EvcilKayipListe.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_insan) {
            Intent intent = new Intent(this,InsanKayipListe.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_nesne) {
            Intent intent = new Intent(this,NesneKayipListe.class);
            startActivity(intent);
            // return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.txt_nasil_oynanir);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
}
