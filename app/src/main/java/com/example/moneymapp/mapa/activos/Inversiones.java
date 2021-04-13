package com.example.moneymapp.mapa.activos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.activos.model.Activo;
import com.example.moneymapp.mapa.activos.model.Inversion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Inversiones extends AppCompatActivity {

    private FirebaseUser user;
    private TextView vanegocios, vabraices,vabvalores,vametales, vapintelectual, vaproteccion,vaotrosinversiones,totalinversiones;
    private Boolean comprobar;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText negocios, braices, bvalores, metales, pintelectual, proteccion, otrosinversiones;
    private Inversion nuevainversion;
    private int c,int_negocios, int_braices, int_bvalores,int_metales, int_pintelectual, int_proteccion, int_otrosinversiones,
            int_totalinversiones, total_final_inversiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inversiones);

        negocios=(EditText)findViewById(R.id.edtAnegocios);
        braices=(EditText)findViewById(R.id.edtAbraices);
        bvalores=(EditText)findViewById(R.id.edtAbvalores);
        metales=(EditText)findViewById(R.id.edtAmetales);
        pintelectual=(EditText)findViewById(R.id.edtApintelectual);
        proteccion=(EditText)findViewById(R.id.edtAproteccion);
        otrosinversiones=(EditText)findViewById(R.id.edtAotrosinversiones);
        totalinversiones=(TextView)findViewById(R.id.txvTotalinversiones);


        vanegocios=(TextView)findViewById(R.id.txvVAnegocios);
        vabraices=(TextView)findViewById(R.id.txvVAbraices);
        vabvalores=(TextView)findViewById(R.id.txvVAbvalores);
        vametales=(TextView)findViewById(R.id.txvVAmetales);
        vapintelectual=(TextView)findViewById(R.id.txvVApintelectual);
        vaproteccion=(TextView)findViewById(R.id.txvVAproteccion);
        vaotrosinversiones=(TextView)findViewById(R.id.txvVAotrossinversiones);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Casas de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("inversiones");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("activos");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Inversion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Inversion.class);
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


    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }

    //método para volver a activos
    public void iraActivos(View v){
        Intent iraactivo = new Intent(this, Activos.class);
        startActivity(iraactivo);
    }


    //método para ingresar presupuesto
    public void ingresarTotalInversiones(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Inversion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Inversion.class);
                    vanegocios.setText("$ "+separador(obj.getNegocios()));
                    vabraices.setText("$ "+separador(obj.getBienesRaices()));
                    vabvalores.setText("$ "+separador(obj.getBolsaValores()));
                    vametales.setText("$ "+separador(obj.getMetales()));
                    vapintelectual.setText("$ "+separador(obj.getPropiedadIntelectual()));
                    vaproteccion.setText("$ "+separador(obj.getProteccion()));
                    vaotrosinversiones.setText("$ "+separador(obj.getOtrasInversiones()));
                    totalinversiones.setText("$ "+separador(obj.getTotalInversiones()));

                    //cálculo del total del presupuesto Casas
                    int_negocios=Integer.parseInt(obj.getNegocios());
                    int_braices=Integer.parseInt(obj.getBienesRaices());
                    int_bvalores=Integer.parseInt(obj.getBolsaValores());
                    int_metales=Integer.parseInt(obj.getMetales());
                    int_pintelectual=Integer.parseInt(obj.getPropiedadIntelectual());
                    int_proteccion=Integer.parseInt(obj.getProteccion());
                    int_otrosinversiones=Integer.parseInt(obj.getOtrasInversiones());
                    int_totalinversiones=Integer.parseInt(obj.getTotalInversiones());

                    //obtención de la "primary key"
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
                        mDatabase.child(clave).child("bienesRaices").setValue(String.valueOf(int_braices));
                        braices.setText("");
                    }
                    if(!bvalores.getText().toString().isEmpty()){
                        int_bvalores=int_bvalores+Integer.parseInt(bvalores.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(bvalores.getText().toString());
                        mDatabase.child(clave).child("bolsaValores").setValue(String.valueOf(int_bvalores));
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
                        mDatabase.child(clave).child("propiedadIntelectual").setValue(String.valueOf(int_pintelectual));
                        pintelectual.setText("");
                    }
                    if(!proteccion.getText().toString().isEmpty()){
                        int_proteccion=int_proteccion+Integer.parseInt(proteccion.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(proteccion.getText().toString());
                        mDatabase.child(clave).child("propiedadIntelectual").setValue(String.valueOf(int_proteccion));
                        proteccion.setText("");
                    }
                    if(!otrosinversiones.getText().toString().isEmpty()){
                        int_otrosinversiones=int_otrosinversiones+Integer.parseInt(otrosinversiones.getText().toString());
                        total_final_inversiones=total_final_inversiones+Integer.parseInt(otrosinversiones.getText().toString());
                        mDatabase.child(clave).child("otrasInversiones").setValue(String.valueOf(int_otrosinversiones));
                        otrosinversiones.setText("");
                    }
                    //ingreso del total final a la bbdd
                    sumarTotalActivos(total_final_inversiones);
                    total_final_inversiones=total_final_inversiones+int_totalinversiones;
                    mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(total_final_inversiones));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         Inversion obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Inversion.class);
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
                    //actualización del presupuesto del usuario



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
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalActivos(total_final_inversiones);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevainversion);

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Inversion obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Inversion.class);
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




    //método para sumar los valores ingresados por cada item al presupuesto global
    public void sumarTotalActivos(final int v){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo m =datanapshot.getValue(Activo.class);
                    String clave=datanapshot.getKey();
                    z=z+Integer.parseInt(m.getActivosTotal());
                    //modificacion
                    tDatabase.child(clave).child("activos_total").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para sumar los valores ingresados por cada item al presupuesto global
    public void restarTotalActivos(final int n ){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo obj7 =datanapshot.getValue(Activo.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getActivosTotal())-y;
                    //modificacion
                    tDatabase.child(clave).child("activosTotal").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para actualizar datos presupuesto casa
    public void actualizarTotalInversiones(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Inversion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Inversion.class);

                    //cálculo del total del total inversiones
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

                    //validación si todos los campos están llenos
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

                    //validación de campos llenos
                    if (negocios.getText().toString().isEmpty() && braices.getText().toString().isEmpty()
                            && bvalores.getText().toString().isEmpty() && metales.getText().toString().isEmpty()
                            && pintelectual.getText().toString().isEmpty() && proteccion.getText().toString().isEmpty()
                            && otrosinversiones.getText().toString().isEmpty()){
                        valida2=false;
                    }


                    //cálculo del total del presupuesto Casas
                    if (valida2==false){
                        Toast.makeText(Inversiones.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){


                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarTotalActivos(contador);//método para restar el presupuesto global
                        if(!negocios.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(negocios.getText().toString());
                            mDatabase.child(clave).child("negocios").setValue(String.valueOf(int_negocios));
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_totalinversiones));
                            negocios.setText("");
                        }

                        if(!braices.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(braices.getText().toString());
                            mDatabase.child(clave).child("bienesRaices").setValue(String.valueOf(int_braices));
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_totalinversiones));
                            braices.setText("");
                        }

                        if(!bvalores.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(bvalores.getText().toString());
                            mDatabase.child(clave).child("bolsaValores").setValue(String.valueOf(int_bvalores));
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_totalinversiones));
                            bvalores.setText("");
                        }

                        if(!metales.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(metales.getText().toString());
                            mDatabase.child(clave).child("metales").setValue(String.valueOf(int_metales));
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_totalinversiones));
                            metales.setText("");
                        }

                        if(!pintelectual.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(pintelectual.getText().toString());
                            mDatabase.child(clave).child("propiedadIntelectual").setValue(String.valueOf(int_pintelectual));
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_totalinversiones));
                            pintelectual.setText("");
                        }

                        if(!proteccion.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(proteccion.getText().toString());
                            mDatabase.child(clave).child("proteccion").setValue(String.valueOf(int_proteccion));
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_totalinversiones));
                            proteccion.setText("");
                        }

                        if(!otrosinversiones.getText().toString().isEmpty()){
                            int_totalinversiones=int_totalinversiones-Integer.parseInt(otrosinversiones.getText().toString());
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_otrosinversiones));
                            mDatabase.child(clave).child("totalInversiones").setValue(String.valueOf(int_totalinversiones));
                            otrosinversiones.setText("");
                        }

                        //else if para seleccion de que servicio modificar
                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Inversion obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Inversion.class);
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
                        Toast.makeText(Inversiones.this,"error! ingresar valor menor o igual al actual!",Toast.LENGTH_LONG).show();

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
