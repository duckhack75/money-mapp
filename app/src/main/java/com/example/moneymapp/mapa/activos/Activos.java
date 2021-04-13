package com.example.moneymapp.mapa.activos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.Mapa;
import com.example.moneymapp.mapa.activos.model.Activo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Activos extends AppCompatActivity {

    private FirebaseUser user;
    private TextView txvTotalActivosFinal;
    private DatabaseReference activosDatabaseReference, fuenteIngreseDatabaseReference,
            inversionesDatabaseReference, posesionesDatabaseReference, liquidosDatabaseReference,
            oDatabase;
    private FirebaseDatabase databaseFirebase;
    private String usuario;
    private Activo nuevoActivo;
    private boolean validaUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activos);

        txvTotalActivosFinal =findViewById(R.id.txvTotalActivosFinal);

       // reset =findViewById(R.id.txvReset);
       // reset.setPaintFlags(reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
       // reset.setText("reset");


        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();


        databaseFirebase =FirebaseDatabase.getInstance();

        fuenteIngreseDatabaseReference = FirebaseDatabase.getInstance().
                getReference("fuenteIngresos");
        inversionesDatabaseReference = FirebaseDatabase.getInstance().
                getReference("inversiones");
        posesionesDatabaseReference = FirebaseDatabase.getInstance().getReference("posesiones");
        liquidosDatabaseReference = FirebaseDatabase.getInstance().getReference("liquidos");
        activosDatabaseReference = FirebaseDatabase.getInstance().getReference("activos");


        verificaUsuario();
         obtenerTotalActivos();

    }

    public String separadorMiles(String num){
        int numero=Integer.parseInt(num);
        String nuevoNumero = String.format("%,d", numero);
        String numfinal=nuevoNumero.replace(",",".");
        return numfinal;
    }

    private void verificaUsuario() {
        Query query=activosDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    validaUsuario=true;
                }

                if (validaUsuario==false){
                    //aqui solamente creo el nuevo objeto activo para ingresarle datos
                    nuevoActivo = new Activo();
                    nuevoActivo.setCorreo(usuario);
                    nuevoActivo.setActivosTotal("0");

                    String clave= activosDatabaseReference.push().getKey();
                    activosDatabaseReference.child(clave).setValue(nuevoActivo);
                    //aquí comienza a crear y registrar la fecha de inicio
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void obtenerTotalActivos(){
        Query q=activosDatabaseReference.orderByChild("correo").equalTo(usuario);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo activo =datanapshot.getValue(Activo.class);
                    txvTotalActivosFinal.setText(separadorMiles(activo.getActivosTotal()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void irFuenteIngresos(View v){
        Intent irFuenteIngresos = new Intent(this, FuenteIngreso.class);
        startActivity(irFuenteIngresos);
    }

    public void irInversiones(View v){
        Intent irInversiones = new Intent(this, Inversiones.class);
        startActivity(irInversiones);
    }

    public void irPosesiones(View v){
        Intent irPosesiones = new Intent(this, Posesiones.class);
        startActivity(irPosesiones);
    }

    public void irLiquidos(View v){
        Intent irLiquidos = new Intent(this, Liquidos.class);
        startActivity(irLiquidos);
    }

    public void irMapa(View v){
        Intent irMapa = new Intent(this, Mapa.class);
        startActivity(irMapa);
    }

    public void irOtros(View v){
        Intent irOtros = new Intent(this, Otros.class);
        startActivity(irOtros);
    }
}
