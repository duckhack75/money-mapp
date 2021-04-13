package com.example.moneymapp.instrucciones;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Instrucciones extends AppCompatActivity {

    private Spinner spinnerEtapas;
    private FirebaseUser user;
    private DatabaseReference etapasDatabaseReference;
    private String usuario;
    private Etapa etapa;
    private boolean validaEtapa;
    private int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);

        spinnerEtapas =findViewById(R.id.spinnerEtapas);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();


        etapasDatabaseReference = FirebaseDatabase.getInstance().getReference("etapas");
        verificaEtapa();

    }

    private void verificaEtapa() {
        Query query= etapasDatabaseReference.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Etapa etapaActual;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    etapaActual=datanapshot.getValue(Etapa.class);

                    //para mostrar estado deuda
                    if (etapaActual.getNombreEtapa().equals("Punto de Partida")){
                        muestraEtapaSpinner(0);
                        validaEtapa=true;
                    }else if(etapaActual.getNombreEtapa().equals("Optimizar")){
                        muestraEtapaSpinner(1);
                        validaEtapa=true;
                    }else if (etapaActual.getNombreEtapa().equals("Escalar")){
                        muestraEtapaSpinner(2);
                        validaEtapa=true;
                    }else if (etapaActual.getNombreEtapa().equals("Acelerar")){
                        muestraEtapaSpinner(3);
                        validaEtapa=true;
                    }else if (etapaActual.getNombreEtapa().equals("Mantener")){
                        muestraEtapaSpinner(4);
                        validaEtapa=true;
                    }else {
                        muestraEtapaSpinner(0);
                        validaEtapa=true;
                    }
                }
                if(validaEtapa){
                    String clave= etapasDatabaseReference.push().getKey();
                    etapa= new Etapa();
                    etapa.setCorreo(usuario);
                    etapa.setNombreEtapa("Punto de Partida");
                    etapasDatabaseReference.child(clave).setValue(etapa);
                    muestraEtapaSpinner(0);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void muestraEtapaSpinner(int posicion){
        String [] etapas= {"Punto de Partida","Optimizar","Escalar","Acelerar","Mantener"};
        ArrayList<String> arrayEtapas= new ArrayList<>(Arrays.asList(etapas));
        ArrayAdapter<String> arrayAdapterEtapas= new ArrayAdapter<>(this,R.layout.
                style_spinner, arrayEtapas);
        spinnerEtapas.setAdapter(arrayAdapterEtapas);
        spinnerEtapas.setSelection(posicion);
    }

    public void seleccionarEtapa(View view){
        Query q= etapasDatabaseReference.orderByChild("correo").equalTo(usuario);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            String clave;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    clave=datanapshot.getKey();
                }

                String selectetapa= (String) spinnerEtapas.getSelectedItem();
                if(selectetapa.equals("Punto de Partida")){
                    etapasDatabaseReference.child(clave).child("nombreEtapa").
                            setValue("Punto de Partida");
                }else if (selectetapa.equals("Optimizar")){
                    etapasDatabaseReference.child(clave).child("nombreEtapa").setValue("Optimizar");
                }else if (selectetapa.equals("Escalar")){
                    etapasDatabaseReference.child(clave).child("nombreEtapa").setValue("Escalar");
                }else if (selectetapa.equals("Acelerar")){
                    etapasDatabaseReference.child(clave).child("nombreEtapa").setValue("Acelerar");
                }else if (selectetapa.equals("Mantener")){
                    etapasDatabaseReference.child(clave).child("nombreEtapa").setValue("Mantener");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }



}
