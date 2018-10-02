package com.example.asus.kanpaylasmaproje;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ASUS on 29.8.2017.
 */

public class giris  extends Fragment {


    EditText mailet, sifreet;
    Button giris;

    private FirebaseAuth auth;

    FirebaseDatabase db;
    FirebaseAuth.AuthStateListener authStateListener;

    private Button kaydol;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.giris, container, false);

        mailet = (EditText) view.findViewById(R.id.editeposta);
        sifreet = (EditText) view.findViewById(R.id.editesifre);


        giris = (Button) view.findViewById(R.id.btngiris);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              girisYap();
             //   startActivity(new Intent(getActivity(),konum.class));




    }
});


        auth=FirebaseAuth.getInstance();


        return  view;

    }

   private void girisYap() {

        String email = mailet.getText().toString();
        final String parola = sifreet.getText().toString();
        if (TextUtils.isEmpty(email)) {

            Toast.makeText(getActivity(), "Lütfen emailinizi giriniz", Toast.LENGTH_SHORT).show();
            return;
        }
        //Parola girilmemiş ise kullanıcıyı uyarıyoruz
        if (TextUtils.isEmpty(parola)) {

            Toast.makeText(getActivity(), "Lütfen parolanızı giriniz", Toast.LENGTH_SHORT).show();

            return;

        }
        auth.signInWithEmailAndPassword(email, parola).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Log.d("email",user.getEmail());
                    String kullanici=user.getEmail();
                    startActivity(new Intent(getActivity(), navigator_drawer.class));

                } else {
                    Log.e("Giriş Hatası", task.getException().getMessage());
                    Toast.makeText(getActivity(), "Kullanıcı adı veya şifre başarısız", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}