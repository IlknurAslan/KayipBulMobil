package com.example.asus.kanpaylasmaproje;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ASUS on 29.8.2017.
 */

public class kaydol extends Fragment {
    private FirebaseAuth auth;
    private EditText mail, sifre;
    FirebaseDatabase db;
    Spinner spinner;
    FirebaseAuth.AuthStateListener authStateListener;

    private Button kaydol;

   // private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
   // private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.kaydol, container, false);
        ///////spinner = (Spinner) view.findViewById(R.id.spinner);
        auth = FirebaseAuth.getInstance();
        sifre = (EditText) view.findViewById(R.id.editparola);

        mail = (EditText) view.findViewById(R.id.editgmail);


        kaydol = (Button) view.findViewById(R.id.kaydol);

      kaydol.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              String email1 = mail.getText().toString();
              final String parola1 = sifre.getText().toString();


              if(TextUtils.isEmpty(email1)){
                  Toast.makeText(view.getContext(),"Lütfen emailinizi giriniz",Toast.LENGTH_SHORT).show();
                  return;
              }
              if(TextUtils.isEmpty(parola1)){
                  Toast.makeText(view.getContext(),"Lütfen parolanızı giriniz",Toast.LENGTH_SHORT).show();
                  return;
              }
              if (parola1.length()<6){
                  Toast.makeText(view.getContext(),"Parola en az 6 haneli olmalıdır",Toast.LENGTH_SHORT).show();
                  return;
              }

              auth.createUserWithEmailAndPassword(email1,parola1).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {

                      if (task.isSuccessful()) {
                          Intent intent =new Intent(getActivity(),giris.class);
                          startActivity(intent);


                          Toast.makeText(getActivity(), "Kayıt Yapıldı",

                                  Toast.LENGTH_SHORT).show();


                      }
                      else
                      {
                          Toast.makeText(getActivity(), "Kayıt Yapılamadı...",

                                  Toast.LENGTH_SHORT).show();
                      }
                  }
              });

          }
      });


        return  view;

        }



    }