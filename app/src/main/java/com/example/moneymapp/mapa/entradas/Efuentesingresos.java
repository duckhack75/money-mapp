package com.example.moneymapp.mapa.entradas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.entradas.model.Efuentesingreso;
import com.example.moneymapp.mapa.entradas.model.Entrada;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Efuentesingresos extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vatrabajo, vanegocio,vaotrosfingresos,totalfingresos;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText trabajo, negocio, otrosfingresos;
    private Efuentesingreso nuevofingreso;
    private int c,int_trabajo, int_negocio, int_otrosfingresos,
            int_totalfingresos, total_final_fingresos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_efuentesingreso);


        trabajo=(EditText)findViewById(R.id.edtEtrabajo);
        negocio=(EditText)findViewById(R.id.edtEnegocioempresa);
        otrosfingresos=(EditText)findViewById(R.id.edtEotrosfingresos);
        totalfingresos=(TextView)findViewById(R.id.txvTotalEfingresos);

        //inicializamos el obj de separador de miles
       /*String mostrar=separador2("5.000");
        prueba.setText(mostrar);*/


        vatrabajo=(TextView)findViewById(R.id.txvVEtrabajo);
        vanegocio=(TextView)findViewById(R.id.txvVEnegocioempresa);
        vaotrosfingresos=(TextView)findViewById(R.id.txvVEotrosfingresos);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Efuenteingresos");
        //acceso a la tabla Presupuesto activo glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Entradas");
        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Efuentesingreso obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Efuentesingreso.class);
                    vatrabajo.setText("$ "+separador(obj.getTrabajo()));
                    vanegocio.setText("$ "+separador(obj.getNegocio()));
                    vaotrosfingresos.setText("$ "+separador(obj.getOtrasFuentesIngresos()));
                    totalfingresos.setText("$ "+separador(obj.getTotalFuentesIngresos()));
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


    //método para ingresar presupuesto
    public void ingresarTotalEfuentesIngresos(View view){

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Efuentesingreso obj;

                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Efuentesingreso.class);
                    //cálculo del total del presupuesto Autos
                    int_trabajo=Integer.parseInt(obj.getTrabajo());
                    int_negocio=Integer.parseInt(obj.getNegocio());
                    int_otrosfingresos=Integer.parseInt(obj.getOtrasFuentesIngresos());
                    int_totalfingresos=Integer.parseInt(obj.getTotalFuentesIngresos());


                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_fingresos=0;

                    if(!trabajo.getText().toString().isEmpty()){
                        int_trabajo=int_trabajo+Integer.parseInt(trabajo.getText().toString());
                        total_final_fingresos=total_final_fingresos+Integer.parseInt(trabajo.getText().toString());
                        mDatabase.child(clave).child("trabajo").setValue(String.valueOf(int_trabajo));
                        trabajo.setText("");
                    }
                    if(!negocio.getText().toString().isEmpty()){
                        int_negocio=int_negocio+Integer.parseInt(negocio.getText().toString());
                        total_final_fingresos=total_final_fingresos+Integer.parseInt(negocio.getText().toString());
                        mDatabase.child(clave).child("negocio").setValue(String.valueOf(int_negocio));
                        negocio.setText("");
                    }
                    if(!otrosfingresos.getText().toString().isEmpty()){
                        int_otrosfingresos=int_otrosfingresos+Integer.parseInt(otrosfingresos.getText().toString());
                        total_final_fingresos=total_final_fingresos+Integer.parseInt(otrosfingresos.getText().toString());
                        mDatabase.child(clave).child("otrosfingresos").setValue(String.valueOf(int_otrosfingresos));
                        otrosfingresos.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalEntradas(total_final_fingresos);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_fingresos=total_final_fingresos+int_totalfingresos;
                    mDatabase.child(clave).child("tfingresos").setValue(String.valueOf(total_final_fingresos));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Efuentesingreso obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Efuentesingreso.class);
                                vatrabajo.setText("$ "+separador(obj3.getTrabajo()));
                                vanegocio.setText("$ "+separador(obj3.getNegocio()));
                                vaotrosfingresos.setText("$ "+separador(obj3.getOtrasFuentesIngresos()));
                                totalfingresos.setText("$ "+separador(obj3.getTotalFuentesIngresos()));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if(c!=0){
                    //actualización del presupuesto del usuario


                }else{
                    //creo el auto
                    nuevofingreso= new Efuentesingreso();
                    nuevofingreso.setCorreo(usuario);

                    if (trabajo.getText().toString().isEmpty()){
                        nuevofingreso.setTrabajo("0");
                    }else {
                        nuevofingreso.setTrabajo(trabajo.getText().toString());
                    }

                    if (negocio.getText().toString().isEmpty()){
                        nuevofingreso.setNegocio("0");
                    }else {
                        nuevofingreso.setNegocio(negocio.getText().toString());
                    }

                    if (otrosfingresos.getText().toString().isEmpty()){
                        nuevofingreso.setOtrasFuentesIngresos("0");
                    }else {
                        nuevofingreso.setOtrasFuentesIngresos(otrosfingresos.getText().toString());
                    }


                    //suma del total inicial
                    int_trabajo=Integer.parseInt(nuevofingreso.getTrabajo());
                    int_negocio=Integer.parseInt(nuevofingreso.getNegocio());
                    int_otrosfingresos=Integer.parseInt(nuevofingreso.getOtrasFuentesIngresos());

                    total_final_fingresos=0;
                    total_final_fingresos=int_totalfingresos+int_trabajo+int_negocio+int_otrosfingresos;
                    nuevofingreso.setTotalFuentesIngresos(String.valueOf(total_final_fingresos));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalEntradas(total_final_fingresos);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevofingreso);

                    //se realiza otra consulta para actualizar los datos
                    Query qu=mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Efuentesingreso obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Efuentesingreso.class);
                                vatrabajo.setText("$ "+separador(obj4.getTrabajo()));
                                vanegocio.setText("$ "+separador(obj4.getNegocio()));
                                vaotrosfingresos.setText("$ "+separador(obj4.getOtrasFuentesIngresos()));
                                totalfingresos.setText("$ "+separador(obj4.getTotalFuentesIngresos()));

                            }
                            //limpiar campos input
                            trabajo.setText("");
                            negocio.setText("");
                            otrosfingresos.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    // mDatabase.child("Casas").child(clave).setValue(nuevacasa);
                    //Toast.makeText(Casa.this,"Datos ingresada",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sumarTotalEntradas(final int v){

        //obtención de la "primary key"
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

    //método para sumar los valores ingresados por cada item al presupuesto global
    public void restarEntradasTotal(final int n ){

        //obtención de la "primary key"
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

    //método para actualizar datos presupuesto auto
    public void actualizarTotalEfuentesIngresos(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Efuentesingreso obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Efuentesingreso.class);
                    //cálculo del total del presupuesto Autos
                    int_trabajo=Integer.parseInt(obj.getTrabajo());
                    int_negocio=Integer.parseInt(obj.getNegocio());
                    int_otrosfingresos=Integer.parseInt(obj.getOtrasFuentesIngresos());
                    int_totalfingresos=Integer.parseInt(obj.getTotalFuentesIngresos());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!trabajo.getText().toString().isEmpty()){
                        int_trabajo=int_trabajo-Integer.parseInt(trabajo.getText().toString());
                        contador=contador+Integer.parseInt(trabajo.getText().toString());
                        if (int_trabajo<0){
                            valida=false;
                        }
                    }

                    if (!negocio.getText().toString().isEmpty()){
                        int_negocio=int_negocio-Integer.parseInt(negocio.getText().toString());
                        contador=contador+Integer.parseInt(negocio.getText().toString());
                        if (int_negocio<0){
                            valida=false;
                        }
                    }

                    if (!otrosfingresos.getText().toString().isEmpty()){
                        int_otrosfingresos=int_otrosfingresos-Integer.parseInt(otrosfingresos.getText().toString());
                        contador=contador+Integer.parseInt(otrosfingresos.getText().toString());
                        if (int_otrosfingresos<0){
                            valida=false;
                        }
                    }



                    //validación de campos llenos
                    if (trabajo.getText().toString().isEmpty() && negocio.getText().toString().isEmpty()
                            && otrosfingresos.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Efuentesingresos.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarEntradasTotal(contador);//método que resta al presupuesto globa
                        if(!trabajo.getText().toString().isEmpty()){
                            int_totalfingresos=int_totalfingresos-Integer.parseInt(trabajo.getText().toString());
                            mDatabase.child(clave).child("tfingresos").setValue(String.valueOf(int_totalfingresos));
                            mDatabase.child(clave).child("trabajo").setValue(String.valueOf(int_trabajo));
                            trabajo.setText("");
                        }

                        if(!negocio.getText().toString().isEmpty()){
                            int_totalfingresos=int_totalfingresos-Integer.parseInt(negocio.getText().toString());
                            mDatabase.child(clave).child("tfingresos").setValue(String.valueOf(int_totalfingresos));
                            mDatabase.child(clave).child("negocio").setValue(String.valueOf(int_negocio));
                            negocio.setText("");
                        }


                        if(!otrosfingresos.getText().toString().isEmpty()){
                            int_totalfingresos=int_totalfingresos-Integer.parseInt(otrosfingresos.getText().toString());
                            mDatabase.child(clave).child("tfingresos").setValue(String.valueOf(int_totalfingresos));
                            mDatabase.child(clave).child("otrosfingresos").setValue(String.valueOf(int_otrosfingresos));
                            otrosfingresos.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Efuentesingreso obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Efuentesingreso.class);
                                    vatrabajo.setText("$ "+separador(obj3.getTrabajo()));
                                    vanegocio.setText("$ "+separador(obj3.getNegocio()));
                                    vaotrosfingresos.setText("$ "+separador(obj3.getOtrasFuentesIngresos()));
                                    totalfingresos.setText("$ "+separador(obj3.getTotalFuentesIngresos()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Efuentesingresos.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        trabajo.setText("");
                        negocio.setText("");
                        otrosfingresos.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //método para volver a activos
    public void iraEntradas(View v){
        Intent iraentradas = new Intent(this, Entradas.class);
        startActivity(iraentradas);
    }



}
