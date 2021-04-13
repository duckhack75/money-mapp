package com.example.moneymapp.mapa.salidas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.Mapa;
import com.example.moneymapp.mapa.activos.Liquidos;
import com.example.moneymapp.mapa.activos.Posesiones;
import com.example.moneymapp.mapa.salidas.model.Salida;
import com.example.moneymapp.presupuesto.Presupuesto;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Salidas extends AppCompatActivity {
    private FirebaseUser user;
    private TextView txvtotalsalidasfinal, reset;
    private DatabaseReference mDatabase, mDatabase2, fDatabase, iDatabase,pDatabase, lDatabase,
            oDatabase;
    private FirebaseDatabase database;
    private String usuario, presupuesto;
    private Salida nuevosalida;
    public int int_total,c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salidas);


        txvtotalsalidasfinal=findViewById(R.id.txvTsalidasfinal);



        // reset =findViewById(R.id.txvReset);
        // reset.setPaintFlags(reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // reset.setText("reset");


        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //creacion de muchas instancias para ser eliminadas
        database=FirebaseDatabase.getInstance();

        fDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");
        iDatabase = FirebaseDatabase.getInstance().getReference("Salida_inversiones");
        pDatabase = FirebaseDatabase.getInstance().getReference("Salida_ahorro");
        lDatabase = FirebaseDatabase.getInstance().getReference("Salida_otros");

        mDatabase2 = FirebaseDatabase.getInstance().getReference("Salidas");


        verificaUsuario(mDatabase2);
        getTotalsalidas(mDatabase2);


    }
    //método para cargar el presupuesto a las salidas
    private void cargaPresupuesto() {
        Query q2=fDatabase.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Presupuestoglobal nuevo=datanapshot.getValue(Presupuestoglobal.class);
                    //pruebapasivos.setText(String.valueOf(c));
                    presupuesto=nuevo.getPresupuestoTotal();
                    c++;
                }

                if(c==1){
                    ingresarDatoPresupuesto(presupuesto);
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //metodo para ingresar datos de presupuesto a la salida
    public void ingresarDatoPresupuesto(final String monto){
        Query q3=mDatabase2.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int x=Integer.parseInt(monto);
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Salida m =datanapshot.getValue(Salida.class);
                    //modificacion
                    int z=Integer.parseInt(m.getSalidaTotal())+x;
                    String clave=datanapshot.getKey();
                    mDatabase2.child(clave).child("salidas_total").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    nuevosalida= new Salida();
                    nuevosalida.setCorreo(usuario);
                    nuevosalida.setSalidaTotal("0");
                    nuevosalida.setAlterna("0");

                    String clave= mDatabase2.push().getKey();
                    mDatabase2.child(clave).setValue(nuevosalida);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //muestra el total activos en pantalla
    public void getTotalsalidas(DatabaseReference mDatabase2){
        //obtención de la "primary key"
        Query q=mDatabase2.orderByChild("correo").equalTo(usuario);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Salida x =datanapshot.getValue(Salida.class);
                    String clave=datanapshot.getKey();
                    txvtotalsalidasfinal.setText(separador(x.getSalidaTotal()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //método para ir a casa
    public void iraSinversiones(View v){
        Intent irainversiones = new Intent(this, SalidasInversiones.class);
        startActivity(irainversiones);
    }

    //método para ir a Auto
    public void iraSgato(View v){
        Intent iraposesiones = new Intent(this, Posesiones.class);
        startActivity(iraposesiones);
    }

    //método para ir a Auto
    public void iraSahorro(View v){
        Intent iraliquidos = new Intent(this, Liquidos.class);
        startActivity(iraliquidos);
    }

    //método para ir a Servicios
    public void iraPresupuesto(View v){
        Intent iramapa = new Intent(this, Presupuesto.class);
        startActivity(iramapa);
    }


    //método para ir a Servicios
    public void iraMapa(View v){
        Intent iramapa = new Intent(this, Mapa.class);
        startActivity(iramapa);
    }

    //método para ir a Auto
    public void iraSotros(View v){
        Intent iraotros = new Intent(this, SalidasOtros.class);
        startActivity(iraotros);
    }





}
