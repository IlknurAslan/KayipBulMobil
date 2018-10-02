package com.example.asus.kanpaylasmaproje;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ASUS on 03.03.2018.
 */

public class CustomAdapter extends BaseAdapter
{
    LayoutInflater layoutInflater;
    ArrayList<MesajModel> mesajlist;
    FirebaseUser fuser;
    FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private String userID;
    public CustomAdapter(Activity activity, ArrayList<MesajModel> mesajlist,FirebaseUser fuser){
        this.mesajlist=mesajlist;
        layoutInflater =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fuser =fuser;

        //this.kayipList1 =kayipList1;
    }


    @Override
    public int getCount() {
        return  mesajlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mesajlist.get(position);
    }

    @Override
    public long getItemId(int position) {
       return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satir = null;
        MesajModel mesaj = mesajlist.get(position);
        database = FirebaseDatabase.getInstance();

       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mesaj.getGonderen() == fuser.getEmail()){
        satir = layoutInflater.inflate(R.layout.layout_sag, null);
        TextView mailim = (TextView) satir.findViewById(R.id.mesajben);
        mailim.setText(mesaj.getGonderen());
        TextView mesajim = (TextView) satir.findViewById(R.id.mesajim);
        mesajim.setText(mesaj.getMesaj());
        TextView zamanim = (TextView) satir.findViewById(R.id.zamanim);
        zamanim.setText(mesaj.getZaman());

    }

        else
        {
            satir=layoutInflater.inflate(R.layout.layout_sol,null);
            TextView gonderenMail=(TextView) satir.findViewById(R.id.mesajgonderen);
            gonderenMail.setText(mesaj.getGonderen());
            TextView mesaji=(TextView) satir.findViewById(R.id.mesajı);
            mesaji.setText(mesaj.getMesaj());
            TextView zamani=(TextView) satir.findViewById(R.id.zamani);
            zamani.setText(mesaj.getZaman());

        }
        return  satir;
    }
}
