package com.example.moneymapp.mapa.entradas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.activos.model.Inversion;
import com.example.moneymapp.mapa.entradas.model.Einversion;
import com.example.moneymapp.mapa.entradas.model.Entrada;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Einversiones extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vanegocios, vabraices,vabvalores,vametales, vapintelectual, vaproteccion,vaotrosinversiones,totalinversiones;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText negocios, braices, bvalores, metales, pintelectual, proteccion, otrosinversiones;
    private Inversion nuevainversion;
    private int c,int_negocios, int_braices, int_bvalores,int_metales, int_pintelectual, int_proteccion, int_otrosinversiones,
            int_totalinversiones, total_final_inversiones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einversiones);

        negocios=(EditText)findViewById(R.id.edtEnegocios);
        braices=(EditText)findViewById(R.id.edtEbraices);
        bvalores=(EditText)findViewById(R.id.edtEbvalores);
        metales=(EditText)findViewById(R.id.edtEmetales);
        pintelectual=(EditText)findViewById(R.id.edtEpintelectual);
        proteccion=(EditText)findViewById(R.id.edtEproteccion);
        otrosinversiones=(EditText)findViewById(R.id.edtEotrosinversiones);
        totalinversiones=(TextView)findViewById(R.id.txvTotalEinversiones);


        vanegocios=(TextView)findViewById(R.id.txvVEnegocios);
        vabraices=(TextView)findViewById(R.id.txvVEbraices);
        vabvalores=(TextView)findViewById(R.id.txvVEbvalores);
        vametales=(TextView)findViewById(R.id.txvVEmetales);
        vapintelectual=(TextView)findViewById(R.id.txvVEpintelectual);
        vaproteccion=(TextView)findViewById(R.id.txvVEproteccion);
        vaotrosinversiones=(TextView)findViewById(R.id.txvVEotrosinversiones);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Casas de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("EInversiones");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Entradas");


        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Einversion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Einversion.class);
                    vanegocios.setText("$ "+separador(obj.getNegocios()));
                    vabraices.setText("$ "+separador(obj.getBienesRaices()));
                    vabvalores.setText("$ "+separador(obj.getBolsaValores()));
                    vametales.setText("$ "+separador(obj.getMetales()));
                    vapintelectual.setText("$ "+separador(obj.getPropiedadIntelectual()));
                    vaproteccion.setText("$ "+separador(obj.getProteccion()));
                    vaotrosinversiones.setText("$ "+separador(obj.getOtrasInversiones()));
                    totalinversiones.setText("$ "+separador(obj.getTotalInversiones()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //m??todo que separa por miles los n??meros
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }


    //m??todo para volver a activos
    public void iraEntradas(View v){
        Intent iraactivo = new Intent(this, Entradas.class);
        startActivity(iraactivo);
    }


    //m??todo para ingresar presupuesto
    public void ingresarTotalEinversiones(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
               Einversion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformaci??n a enteros
                    obj=datanapshot.getValue(Einversion.class);
                    vanegocios.setText("$ "+separador(obj.getNegocios()));
                    vabraices.setText("$ "+separador(obj.getBienesRaices()));
                    vabvalores.setText("$ "+separador(obj.getBolsaValores()));
                    vametales.setText("$ "+separador(obj.getMetales()));
                    vapintelectual.setText("$ "+separador(obj.getPropiedadIntelectual()));
                    vaproteccion.setText("$ "+separador(obj.getProteccion()));
                    vaotrosinversiones.setText("$ "+separador(obj.getOtrasInversiones()));
                    totalinversiones.setText("$ "+separador(obj.getTotalInversiones()));

                    //c??lculo del total del presupuesto Casas
                    int_negocios=Integer.parseInt(obj.getNegocios());
                    int_braices=Integer.parseInt(obj.getBienesRaices());
                    int_bvalores=Integer.parseInt(obj.getBolsaValores());
                    int_metales=Integer.parseInt(obj.getMetales());
                    int_pintelectual=Integer.parseInt(obj.getPropiedadIntelectual());
                    int_proteccion=Integer.parseInt(obj.getProteccion());
                    int_otrosinversiones=Integer.parseInt(obj.getOtrasInversiones());
                    int_totalinversiones=Integer.parseInt(obj.getTotalInversiones());

                    //obtenci??n de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_inversiones=0;

                    if(!negocios.getText().toString().isEmpty()){
                        int_negocios=int_negocios+Integer.parseInt(negocios.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(negocios.getText().toString());
                        mDatabase.child(clave).child("negocios").setValue(String.valueOf(int_negocios));
                        negocios.setText("");
                    }
                    if(!braices.getText().toString().isEmpty()){
                        int_braices=int_braices+Integer.parseInt(braices.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(braices.getText().toString());
                        mDatabase.child(clave).child("braices").setValue(String.valueOf(int_braices));
                        braices.setText("");
                    }
                    if(!bvalores.getText().toString().isEmpty()){
                        int_bvalores=int_bvalores+Integer.parseInt(bvalores.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(bvalores.getText().toString());
                        mDatabase.child(clave).child("bvalores").setValue(String.valueOf(int_bvalores));
                        bvalores.setText("");
                    }
                    if(!metales.getText().toString().isEmpty()){
                        int_metales=int_metales+Integer.parseInt(metales.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(metales.getText().toString());
                        mDatabase.child(clave).child("metales").setValue(String.valueOf(int_metales));
                        metales.setText("");
                    }
                    if(!pintelectual.getText().toString().isEmpty()){
                        int_pintelectual=int_pintelectual+Integer.parseInt(pintelectual.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(pintelectual.getText().toString());
                        mDatabase.child(clave).child("pintelectual").setValue(String.valueOf(int_pintelectual));
                        pintelectual.setText("");
                    }
                    if(!proteccion.getText().toString().isEmpty()){
                        int_proteccion=int_proteccion+Integer.parseInt(proteccion.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(proteccion.getText().toString());
                        mDatabase.child(clave).child("proteccion").setValue(String.valueOf(int_proteccion));
                        proteccion.setText("");
                    }
                    if(!otrosinversiones.getText().toString().isEmpty()){
                        int_otrosinversiones=int_otrosinversiones+Integer.parseInt(otrosinversiones.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(otrosinversiones.getText().toString());
                        mDatabase.child(clave).child("otrasinversiones").setValue(String.valueOf(int_otrosinversiones));
                        otrosinversiones.setText("");
                    }
                    //ingreso del total final a la bbdd
                    sumarTotalEntradas(total_final_inversiones);
                    total_final_inversiones=total_final_inversiones+int_totalinversiones;
                    mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(total_final_inversiones));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Einversion obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Einversion.class);
                                vanegocios.setText("$ "+separador(obj3.getNegocios()));
                                vabraices.setText("$ "+separador(obj3.getBienesRaices()));
                                vabvalores.setText("$ "+separador(obj3.getBolsaValores()));
                                vametales.setText("$ "+separador(obj3.getMetales()));
                                vapintelectual.setText("$ "+separador(obj3.getPropiedadIntelectual()));
                                vaproteccion.setText("$ "+separador(obj3.getProteccion()));
                                vaotrosinversiones.setText("$ "+separador(obj3.getOtrasInversiones()));
                                totalinversiones.setText("$ "+separador(obj3.getTotalInversiones()));

                            }
                            //limpiar campos input
                            negocios.setText("");
                            braices.setText("");
                            bvalores.setText("");
                            metales.setText("");
                            pintelectual.setText("");
                            proteccion.setText("");
                            otrosinversiones.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if(c!=0){
                    //actualizaci??n del presupuesto del usuario



                }else{
                    //creo la casa
                    nuevainversion= new Inversion();
                    nuevainversion.setCorreo(usuario);

                    if (negocios.getText().toString().isEmpty()){
                        nuevainversion.setNegocios("0");
                    }else {
                        nuevainversion.setNegocios(negocios.getText().toString());
                    }

                    if (braices.getText().toString().isEmpty()){
                        nuevainversion.setBienesRaices("0");
                    }else {
                        nuevainversion.setBienesRaices(braices.getText().toString());
                    }

                    if (bvalores.getText().toString().isEmpty()){
                        nuevainversion.setBolsaValores("0");
                    }else {
                        nuevainversion.setBolsaValores(bvalores.getText().toString());
                    }

                    if (metales.getText().toString().isEmpty()){
                        nuevainversion.setMetales("0");
                    }else {
                        nuevainversion.setMetales(metales.getText().toString());
                    }

                    if (pintelectual.getText().toString().isEmpty()){
                        nuevainversion.setPropiedadIntelectual("0");
                    }else {
                        nuevainversion.setPropiedadIntelectual(pintelectual.getText().toString());
                    }

                    if (proteccion.getText().toString().isEmpty()){
                        nuevainversion.setProteccion("0");
                    }else {
                        nuevainversion.setProteccion(proteccion.getText().toString());
                    }

                    if (otrosinversiones.getText().toString().isEmpty()){
                        nuevainversion.setOtrasInversiones("0");
                    }else {
                        nuevainversion.setOtrasInversiones(otrosinversiones.getText().toString());
                    }


                    //suma del total inicial

                    int_negocios=Integer.parseInt(nuevainversion.getNegocios());
                    int_braices=Integer.parseInt(nuevainversion.getBienesRaices());
                    int_bvalores=Integer.parseInt(nuevainversion.getBolsaValores());
                    int_metales=Integer.parseInt(nuevainversion.getMetales());
                    int_pintelectual=Integer.parseInt(nuevainversion.getPropiedadIntelectual());
                    int_proteccion=Integer.parseInt(nuevainversion.getProteccion());
                    int_otrosinversiones=Integer.parseInt(nuevainversion.getOtrasInversiones());


                    total_final_inversiones=0;
                    total_final_inversiones=int_totalinversiones+int_negocios+int_braices+int_bvalores+int_metales+
                            int_pintelectual+int_proteccion+ int_otrosinversiones;

                    nuevainversion.setTotalInversiones(String.valueOf(total_final_inversiones));
                    //aqu?? se invoca la funci??n para cambiar presupuesto general
                    sumarTotalEntradas(total_final_inversiones);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevainversion);

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Einversion obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Einversion.class);
                                vanegocios.setText("$ "+separador(obj4.getNegocios()));
                                vabraices.setText("$ "+separador(obj4.getBienesRaices()));
                                vabvalores.setText("$ "+separador(obj4.getBolsaValores()));
                                vametales.setText("$ "+separador(obj4.getMetales()));
                                vapintelectual.setText("$ "+separador(obj4.getPropiedadIntelectual()));
                                vaproteccion.setText("$ "+separador(obj4.getProteccion()));
                                vaotrosinversiones.setText("$ "+separador(obj4.getOtrasInversiones()));
                                totalinversiones.setText("$ "+separador(obj4.getTotalInversiones()));
                            }
                            //limpiar campos input
                            negocios.setText("");
                            braices.setText("");
                            bvalores.setText("");
                            metales.setText("");
                            pintelectual.setText("");
                            proteccion.setText("");
                            otrosinversiones.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    negocios.setText("");
                    braices.setText("");
                    bvalores.setText("");
                    metales.setText("");
                    pintelectual.setText("");
                    proteccion.setText("");
                    otrosinversiones.setText("");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //m??todo para sumar a Entradas
    public void sumarTotalEntradas(final int v){
        //obtenci??n de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Entrada m =datanapshot.getValue(Entrada.class);
                    String clave=datanapshot.getKey();
                    z=z+Integer.parseInt(m.getEntradasTotal());
                    //modificacion
                    tDatabase.child(clave).child("entradas_total").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //m??todo para sumar los valores ingresados por cada item al presupuesto global
    public void restarEntradasTotal(final int n ){

        //obtenci??n de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Entrada obj7 =datanapshot.getValue(Entrada.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getEntradasTotal())-y;
                    //modificacion
                    tDatabase.child(clave).child("entradas_total").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //m??todo para actualizar datos presupuesto casa
    public void actualizarTotalEinversiones(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Einversion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformaci??n a enteros
                    obj=datanapshot.getValue(Einversion.class);

                    //c??lculo del total del total inversiones
                    int_negocios=Integer.parseInt(obj.getNegocios());
                    int_braices=Integer.parseInt(obj.getBienesRaices());
                    int_bvalores=Integer.parseInt(obj.getBolsaValores());
                    int_metales=Integer.parseInt(obj.getMetales());
                    int_pintelectual=Integer.parseInt(obj.getPropiedadIntelectual());
                    int_proteccion=Integer.parseInt(obj.getProteccion());
                    int_otrosinversiones=Integer.parseInt(obj.getOtrasInversiones());
                    int_totalinversiones=Integer.parseInt(obj.getTotalInversiones());




                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validaci??n si todos los campos est??n llenos
                    int contador=0;//contador para actualizar el presupuesto global
                    if (!negocios.getText().toString().isEmpty()){
                        int_negocios=int_negocios-Integer.parseInt(negocios.getText().toString());
                        contador=contador+Integer.parseInt(negocios.getText().toString());
                        if (int_negocios<0){
                            valida=false;
                        }
                    }

                    if (!braices.getText().toString().isEmpty()){
                        int_braices=int_braices-Integer.parseInt(braices.getText().toString());
                        contador=contador+Integer.parseInt(braices.getText().toString());
                        if (int_braices<0){
                            valida=false;
                        }
                    }

                    if (!bvalores.getText().toString().isEmpty()){
                        int_bvalores=int_bvalores-Integer.parseInt(bvalores.getText().toString());
                        contador=contador+Integer.parseInt(bvalores.getText().toString());
                        if (int_bvalores<0){
                            valida=false;
                        }
                    }


                    if (!metales.getText().toString().isEmpty()){
                        int_metales=int_metales-Integer.parseInt(metales.getText().toString());
                        contador=contador+Integer.parseInt(metales.getText().toString());
                        if (int_metales<0){
                            valida=false;
                        }
                    }

                    if (!pintelectual.getText().toString().isEmpty()){
                        int_pintelectual=int_pintelectual-Integer.parseInt(pintelectual.getText().toString());
                        contador=contador+Integer.parseInt(pintelectual.getText().toString());
                        if (int_pintelectual<0){
                            valida=false;
                        }
                    }

                    if (!proteccion.getText().toString().isEmpty()){
                        int_proteccion=int_proteccion-Integer.parseInt(proteccion.getText().toString());
                        contador=contador+Integer.parseInt(proteccion.getText().toString());
                        if (int_proteccion<0){
                            valida=false;
                        }
                    }

                    if (!otrosinversiones.getText().toString().isEmpty()){
                        int_otrosinversiones=int_otrosinversiones-Integer.parseInt(otrosinversiones.getText().toString());
                        contador=contador+Integer.parseInt(otrosinversiones.getText().toString());
                        if (int_otrosinversiones<0){
                            valida=false;
                        }
                    }

                    //validaci??n de campos llenos
                    if (negocios.getText().toString().isEmpty() && braices.getText().toString().isEmpty()
                            && bvalores.getText().toString().isEmpty() && metales.getText().toString().isEmpty()
                            && pintelectual.getText().toString().isEmpty() && proteccion.getText().toString().isEmpty()
                            && otrosinversiones.getText().toString().isEmpty()){
                        valida2=false;
                    }


                    //c??lculo del total del presupuesto Casas
                    if (valida2==false){
                        Toast.makeText(Einversiones.this,"error! debe llenar 1 o m??s campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){


                        //obtenci??n de la "primary key"
                        String clave=datanapshot.getKey();
                        restarEntradasTotal(contador);//m??todo para restar el presupuesto global
                        if(!negocios.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(negocios.getText().toString());
                            mDatabase.child(clave).child("negocios").setValue(String.valueOf(int_negocios));
                            mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(int_totalinversiones));
                            negocios.setText("");
                        }

                        if(!braices.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(braices.getText().toString());
                            mDatabase.child(clave).child("braices").setValue(String.valueOf(int_braices));
                            mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(int_totalinversiones));
                            braices.setText("");
                        }

                        if(!bvalores.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(bvalores.getText().toString());
                            mDatabase.child(clave).child("bvalores").setValue(String.valueOf(int_bvalores));
                            mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(int_totalinversiones));
                            bvalores.setText("");
                        }

                        if(!metales.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(metales.getText().toString());
                            mDatabase.child(clave).child("metales").setValue(String.valueOf(int_metales));
                            mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(int_totalinversiones));
                            metales.setText("");
                        }

                        if(!pintelectual.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(pintelectual.getText().toString());
                            mDatabase.child(clave).child("pintelectual").setValue(String.valueOf(int_pintelectual));
                            mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(int_totalinversiones));
                            pintelectual.setText("");
                        }

                        if(!proteccion.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(proteccion.getText().toString());
                            mDatabase.child(clave).child("proteccion").setValue(String.valueOf(int_proteccion));
                            mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(int_totalinversiones));
                            proteccion.setText("");
                        }

                        if(!otrosinversiones.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(otrosinversiones.getText().toString());
                            mDatabase.child(clave).child("otrasinversiones").setValue(String.valueOf(int_otrosinversiones));
                            mDatabase.child(clave).child("tinversiones").setValue(String.valueOf(int_totalinversiones));
                            otrosinversiones.setText("");
                        }

                        //else if para seleccion de que servicio modificar
                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales despu??s de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Einversion obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Einversion.class);
                                    vanegocios.setText("$ "+separador(obj3.getNegocios()));
                                    vabraices.setText("$ "+separador(obj3.getBienesRaices()));
                                    vabvalores.setText("$ "+separador(obj3.getBolsaValores()));
                                    vametales.setText("$ "+separador(obj3.getMetales()));
                                    vapintelectual.setText("$ "+separador(obj3.getPropiedadIntelectual()));
                                    vaproteccion.setText("$ "+separador(obj3.getProteccion()));
                                    vaotrosinversiones.setText("$ "+separador(obj3.getOtrasInversiones()));
                                    totalinversiones.setText("$ "+separador(obj3.getTotalInversiones()));

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Einversiones.this,"error! ingresar valor menor o igual al actual!",Toast.LENGTH_LONG).show();

                        negocios.setText("");
                        braices.setText("");
                        bvalores.setText("");
                        metales.setText("");
                        pintelectual.setText("");
                        proteccion.setText("");
                        otrosinversiones.setText("");
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
