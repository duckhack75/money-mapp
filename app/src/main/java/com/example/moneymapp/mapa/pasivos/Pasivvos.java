package com.example.moneymapp.mapa.pasivos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.Mapa;
import com.example.moneymapp.mapa.pasivos.model.Pasivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Pasivvos extends AppCompatActivity {
    private FirebaseUser user;
    private TextView txvtotalpasivosfinal, reset;
    private DatabaseReference mDatabase, mDatabase2, gDatabase, sDatabase;
    private FirebaseDatabase database;
    private String usuario;
    private Pasivo nuevopasivo;
    public int int_total,c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasivvos);
        txvtotalpasivosfinal=findViewById(R.id.txvTpasivosfinal);
        // reset =findViewById(R.id.txvReset);
        // reset.setPaintFlags(reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // reset.setText("reset");

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //creacion de muchas instancias para ser eliminadas
        database=FirebaseDatabase.getInstance();

        gDatabase = FirebaseDatabase.getInstance().getReference("Pasivoscongarantia");
        sDatabase = FirebaseDatabase.getInstance().getReference("Pasivossingarantia");

        mDatabase2 = FirebaseDatabase.getInstance().getReference("Mpasivos");


        verificaUsuario(mDatabase2);
        getTotalpasivos(mDatabase2);
    }

    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }

    private void verificaUsuario(final DatabaseReference mDatabase2) {
        //se consulta a la bbdd para validar si existe ya el usuario y si no existe crear otro
        Query q2=mDatabase2.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){

                    c++;
                }

                if(c!=0){
                    //ya existe el usuario registrado y se cuentan los días de el presupuesto mensual, si son 30 se cierra el estado,
                    //guardaría la fecha inicial para que si son m´s de 30 días no active nada , se guardaría día, mes y año
                }else{
                    //aqui solamente creo el nuevo objeto activo para ingresarle datos
                    nuevopasivo= new Pasivo();
                    nuevopasivo.setCorreo(usuario);
                    nuevopasivo.setPasivosTotal("0");

                    String clave= mDatabase2.push().getKey();
                    mDatabase2.child(clave).setValue(nuevopasivo);

                    //aquí comienza a crear y registrar la fecha de inicio
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //muestra el total activos en pantalla
    public void getTotalpasivos(DatabaseReference mDatabase2){
        //obtención de la "primary key"
        Query q=mDatabase2.orderByChild("correo").equalTo(usuario);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Pasivo x =datanapshot.getValue(Pasivo.class);
                    String clave=datanapshot.getKey();
                    txvtotalpasivosfinal.setText(separador(x.getPasivosTotal()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //ir a fuentes de ingreso
    public void iraCongarantia(View v){
        Intent iracongarantia = new Intent(this, Congarantia.class);
        startActivity(iracongarantia);
    }

    //método para ir a casa
    public void iraSingarantia(View v){
        Intent irasingarantia = new Intent(this, Singarantia.class);
        startActivity(irasingarantia);
    }



    //método para ir a Servicios
    public void iraMapa(View v){
        Intent iramapa = new Intent(this, Mapa.class);
        startActivity(iramapa);
    }


}
