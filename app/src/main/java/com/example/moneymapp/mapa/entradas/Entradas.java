package com.example.moneymapp.mapa.entradas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.Mapa;
import com.example.moneymapp.mapa.entradas.model.Entrada;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Entradas extends AppCompatActivity {
    private FirebaseUser user;
    private TextView txvtotalentradasfinal, reset;
    private DatabaseReference mDatabase, mDatabase2, fDatabase, iDatabase,oDatabase;
    private FirebaseDatabase database;
    private String usuario;
    private Entrada nuevaentrada;
    public int int_total,c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas);

        txvtotalentradasfinal=findViewById(R.id.txvTEntradasfinal);



        // reset =findViewById(R.id.txvReset);
        // reset.setPaintFlags(reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // reset.setText("reset");


        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //creacion de muchas instancias para ser eliminadas
        database=FirebaseDatabase.getInstance();

        fDatabase = FirebaseDatabase.getInstance().getReference("Efuenteingresos");
        iDatabase = FirebaseDatabase.getInstance().getReference("Einversiones");
        oDatabase = FirebaseDatabase.getInstance().getReference("Eotros");

        mDatabase2 = FirebaseDatabase.getInstance().getReference("Entradas");


        verificaUsuario(mDatabase2);
        getTotalentradas(mDatabase2);

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
                    nuevaentrada= new Entrada();
                    nuevaentrada.setCorreo(usuario);
                    nuevaentrada.setEntradasTotal("0");

                    String clave= mDatabase2.push().getKey();
                    mDatabase2.child(clave).setValue(nuevaentrada);

                    //aquí comienza a crear y registrar la fecha de inicio
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //muestra el total activos en pantalla
    public void getTotalentradas(DatabaseReference mDatabase2){
        //obtención de la "primary key"
        Query q=mDatabase2.orderByChild("correo").equalTo(usuario);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Entrada x =datanapshot.getValue(Entrada.class);
                    String clave=datanapshot.getKey();
                    txvtotalentradasfinal.setText(separador(x.getEntradasTotal()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //ir a fuentes de ingreso
    public void irEFingresos(View v){
        Intent irafingresos = new Intent(this, Efuentesingresos.class);
        startActivity(irafingresos);
    }

    //método para ir a casa
    public void iraEinversiones(View v){
        Intent irainversiones = new Intent(this,Einversiones.class);
        startActivity(irainversiones);
    }


    //método para ir a Servicios
    public void iraMapa(View v){
        Intent iramapa = new Intent(this, Mapa.class);
        startActivity(iramapa);
    }

    //método para ir a Auto
    public void iraEOtros(View v){
        Intent iraotros = new Intent(this, Eotros.class);
        startActivity(iraotros);
    }
}
