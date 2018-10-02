package com.example.asus.kanpaylasmaproje;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Harita extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {



    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    IOException ex;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FirebaseDatabase database;
    TextView Konumtext;

    EditText konumedit;
    Button konumkaydet;
    private String konumgonderme;
    private KisiModel kisiModel;
    private String userID;
    private DatabaseReference mDatabase;
    public static String konumtut = "konum";
    private DatabaseReference dbUserLocationRef;


    private static final int ERROR_DIALOG_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harita);
        getSupportActionBar().setTitle("konumum");
         Konumtext = (TextView) findViewById(R.id.konum);
        konumedit = (EditText) findViewById(R.id.konumedit);
        konumkaydet = (Button) findViewById(R.id.btnkonumkaydet);

        mapFrag=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFrag.getMapAsync(this);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userID = user.getUid();
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        database=FirebaseDatabase.getInstance();
        final DatabaseReference dbRef=database.getReference("users").child(userID).child("konum");
        final DatabaseReference dbRef1=database.getReference("kayipara").child(userID).child("konum");
        konumkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String  konumyeni=Konumtext.getText().toString();
                String konum=konumedit.getText().toString();
               Log.d("konumm",konumedit.getText().toString());
                mDatabase = FirebaseDatabase.getInstance().getReference();

                     mDatabase.child("users").child(userID).child("konum").setValue(konum);
                     mDatabase.child("kayipara").child(userID).child("konum").setValue(konum);
                    Toast.makeText(Harita.this, "Kaydedildi", Toast.LENGTH_SHORT).show();
                    Intent sonrakiEkran = new Intent(getApplicationContext(), navigator_drawer.class); //Bilginin diğer aktivitelere iletilmesi
                    sonrakiEkran.putExtra("konum", Konumtext.getText().toString());
                    startActivity(sonrakiEkran);




            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();



        if(mGoogleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            startActivity(new Intent(this.getBaseContext(), Bilgilerim.class));
            String s = ;
            Intent i = new Intent(getApplicationContext(), NewActivity.class);
            i.putExtra("send_string",s);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }
    */

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
           // double latitude = 0;
           // double longitude=0;
            List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

          //  String add = "";
            if (addresses.size() > 0) {
               // for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
               //     add += addresses.get(0).getAddressLine(i) + "\n";
                Log.d("aaadres",addresses.get(0).getAddressLine(0));
                Log.d("city",addresses.get(0).getAdminArea());
            //  Log.d("sehir",addresses.get(0).getCountryName());
                //System.out.print(addresses.get(0).getCountryName());
                 konumedit.setText(addresses.get(0).getAdminArea());
                Konumtext.setText(addresses.get(0).getAdminArea());
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                //  addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                markerOptions.position(latLng);
                markerOptions.title(addresses.get(0).getAdminArea());

                // city=latLng
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

            }





        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        // List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
       /*
        String city;
        String adreesses;
        Geocoder geocoder;
        geocoder = new Geocoder(Harita.this, Locale.getDefault());
        //Place current location marker
        */

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Harita.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                        //eger mgooglemap null degılse mainactivity gec
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }




}
