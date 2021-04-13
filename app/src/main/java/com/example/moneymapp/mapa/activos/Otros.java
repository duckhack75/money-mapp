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
import com.example.moneymapp.mapa.activos.model.Otro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Otros extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vacuentas, vaeducacion, vaotrosactivos, totalotros;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText cuentas, educacion, otrosactivos;
    private Otro nuevootros;
    private int c, int_cuentas, int_educacion, int_otrosactivos,
            int_totalotrosactivos, total_final_otros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otros);



        cuentas= (EditText) findViewById(R.id.edtAcuentasporcobrar);
        educacion = (EditText) findViewById(R.id.edtAeducacion);
        otrosactivos = (EditText) findViewById(R.id.edtOtrosotrosactivos);
        totalotros = (TextView) findViewById(R.id.txvTotalAotros);



        vacuentas = (TextView) findViewById(R.id.txvVacuentasporcobrar);
        vaeducacion = (TextView) findViewById(R.id.txvVaeducacion);
        vaotrosactivos = (TextView) findViewById(R.id.txvVaotrosotrosactivos);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario = user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("otrosActivos");
        //acceso a la tabla Presupuesto activo glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("activos");
        //muestra de valores actuales, usando where usuario
        Query query = mDatabase.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Otro obj;
                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    obj = datanapshot.getValue(Otro.class);
                    vacuentas.setText("$ " + separador(obj.getCuentas()));
                    vaeducacion.setText("$ " + separador(obj.getEducacion()));
                    vaotrosactivos.setText("$ " + separador(obj.getOtrosActivos()));
                    totalotros.setText("$ " + separador(obj.getTotalOtrosActivos()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método que separa por miles los números
    public String separador(String num) {
        int x = Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal = str.replace(",", ".");
        return numfinal;
    }


    //método para ingresar presupuesto
    public void ingresarTotalOtrosActivos(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q = mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c = 0;
                Otro obj;

                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(Otro.class);
                    //cálculo del total del presupuesto Autos
                    int_cuentas = Integer.parseInt(obj.getCuentas());
                    int_educacion = Integer.parseInt(obj.getEducacion());
                    int_otrosactivos = Integer.parseInt(obj.getOtrosActivos());
                    int_totalotrosactivos = Integer.parseInt(obj.getTotalOtrosActivos());


                    //obtención de la "primary key"
                    String clave = datanapshot.getKey();
                    total_final_otros= 0;

                    if (!cuentas.getText().toString().isEmpty()) {
                        int_cuentas = int_cuentas + Integer.parseInt(cuentas.getText().toString());
                        total_final_otros = total_final_otros + Integer.parseInt(cuentas.getText().toString());
                        mDatabase.child(clave).child("cuentas").setValue(String.valueOf(int_cuentas));
                        cuentas.setText("");
                    }
                    if (!educacion.getText().toString().isEmpty()) {
                        int_educacion = int_educacion + Integer.parseInt(educacion.getText().toString());
                        total_final_otros = total_final_otros + Integer.parseInt(educacion.getText().toString());
                        mDatabase.child(clave).child("educacion").setValue(String.valueOf(int_educacion));
                        educacion.setText("");
                    }
                    if (!otrosactivos.getText().toString().isEmpty()) {
                        int_otrosactivos = int_otrosactivos + Integer.parseInt(otrosactivos.getText().toString());
                        total_final_otros = total_final_otros + Integer.parseInt(otrosactivos.getText().toString());
                        mDatabase.child(clave).child("otrosActivos").setValue(String.valueOf(int_otrosactivos));
                        otrosactivos.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalActivos(total_final_otros);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_otros = total_final_otros + int_totalotrosactivos;
                    mDatabase.child(clave).child("totalOtrosActivos").setValue(String.valueOf(total_final_otros));

                    //se realiza otra consulta para actualizar los datos
                    Query q = mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Otro obj3;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj3 = datanapshot.getValue(Otro.class);
                                vacuentas.setText("$ " + separador(obj3.getCuentas()));
                                vaeducacion.setText("$ " + separador(obj3.getEducacion()));
                                vaotrosactivos.setText("$ " + separador(obj3.getOtrosActivos()));
                                totalotros.setText("$ " + separador(obj3.getTotalOtrosActivos()));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if (c != 0) {
                    //actualización del presupuesto del usuario


                } else {
                    //creo el auto
                    nuevootros = new Otro();
                    nuevootros.setCorreo(usuario);

                    if (cuentas.getText().toString().isEmpty()) {
                        nuevootros.setCuentas("0");
                    } else {
                        nuevootros.setCuentas(cuentas.getText().toString());
                    }

                    if (educacion.getText().toString().isEmpty()) {
                        nuevootros.setEducacion("0");
                    } else {
                        nuevootros.setEducacion(educacion.getText().toString());
                    }

                    if (otrosactivos.getText().toString().isEmpty()) {
                        nuevootros.setOtrosActivos("0");
                    } else {
                        nuevootros.setOtrosActivos(otrosactivos.getText().toString());
                    }


                    //suma del total inicial
                    int_cuentas = Integer.parseInt(nuevootros.getCuentas());
                    int_educacion = Integer.parseInt(nuevootros.getEducacion());
                    int_otrosactivos = Integer.parseInt(nuevootros.getOtrosActivos());

                    total_final_otros = 0;
                    total_final_otros = int_totalotrosactivos + int_cuentas + int_educacion + int_otrosactivos;
                    nuevootros.setTotalOtrosActivos(String.valueOf(total_final_otros));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalActivos(total_final_otros);
                    String clave = mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevootros);

                    //se realiza otra consulta para actualizar los datos
                    Query qu = mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Otro obj4;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj4 = datanapshot.getValue(Otro.class);
                                vacuentas.setText("$ " + separador(obj4.getCuentas()));
                                vaeducacion.setText("$ " + separador(obj4.getEducacion()));
                                vaotrosactivos.setText("$ " + separador(obj4.getOtrosActivos()));
                                totalotros.setText("$ " + separador(obj4.getTotalOtrosActivos()));

                            }
                            //limpiar campos input
                            cuentas.setText("");
                            educacion.setText("");
                            otrosactivos.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
                    tDatabase.child(clave).child("activosTotal").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para actualizar datos presupuesto auto
    public void actualizarTotalOtrosactivos(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Otro obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(Otro.class);
                    //cálculo del total del presupuesto Autos
                    int_cuentas = Integer.parseInt(obj.getCuentas());
                    int_educacion = Integer.parseInt(obj.getEducacion());
                    int_otrosactivos = Integer.parseInt(obj.getOtrosActivos());
                    int_totalotrosactivos = Integer.parseInt(obj.getTotalOtrosActivos());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!cuentas.getText().toString().isEmpty()){
                        int_cuentas=int_cuentas-Integer.parseInt(cuentas.getText().toString());
                        contador=contador+Integer.parseInt(cuentas.getText().toString());
                        if (int_cuentas<0){
                            valida=false;
                        }
                    }

                    if (!educacion.getText().toString().isEmpty()){
                        int_educacion=int_educacion-Integer.parseInt(educacion.getText().toString());
                        contador=contador+Integer.parseInt(educacion.getText().toString());
                        if (int_educacion<0){
                            valida=false;
                        }
                    }

                    if (!otrosactivos.getText().toString().isEmpty()){
                        int_otrosactivos=int_otrosactivos-Integer.parseInt(otrosactivos.getText().toString());
                        contador=contador+Integer.parseInt(otrosactivos.getText().toString());
                        if (int_otrosactivos<0){
                            valida=false;
                        }
                    }



                    //validación de campos llenos
                    if (cuentas.getText().toString().isEmpty() && educacion.getText().toString().isEmpty()
                            && otrosactivos.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Otros.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarActivosTotal(contador);//método que resta al presupuesto globa
                        if(!cuentas.getText().toString().isEmpty()){
                            int_totalotrosactivos=int_totalotrosactivos-Integer.parseInt(cuentas.getText().toString());
                            mDatabase.child(clave).child("totalOtrosActivos").setValue(String.valueOf(int_totalotrosactivos));
                            mDatabase.child(clave).child("cuentas").setValue(String.valueOf(int_cuentas));
                            cuentas.setText("");
                        }

                        if(!educacion.getText().toString().isEmpty()){
                            int_totalotrosactivos=int_totalotrosactivos-Integer.parseInt(educacion.getText().toString());
                            mDatabase.child(clave).child("totalOtrosActivos").setValue(String.valueOf(int_totalotrosactivos));
                            mDatabase.child(clave).child("educacion").setValue(String.valueOf(int_educacion));
                            educacion.setText("");
                        }


                        if(!otrosactivos.getText().toString().isEmpty()){
                            int_totalotrosactivos=int_totalotrosactivos-Integer.parseInt(otrosactivos.getText().toString());
                            mDatabase.child(clave).child("totalOtrosActivos").setValue(String.valueOf(int_totalotrosactivos));
                            mDatabase.child(clave).child("otrosActivos").setValue(String.valueOf(int_otrosactivos));
                            otrosactivos.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Otro obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3 = datanapshot.getValue(Otro.class);
                                    vacuentas.setText("$ " + separador(obj3.getCuentas()));
                                    vaeducacion.setText("$ " + separador(obj3.getEducacion()));
                                    vaotrosactivos.setText("$ " + separador(obj3.getOtrosActivos()));
                                    totalotros.setText("$ " + separador(obj3.getTotalOtrosActivos()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Otros.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        //limpiar campos input
                        cuentas.setText("");
                        educacion.setText("");
                        otrosactivos.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //método para sumar los valores ingresados por cada item al presupuesto global
    public void restarActivosTotal(final int n ){

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




    //método para volver a activos
    public void iraActivos(View v){
        Intent iraactivo = new Intent(this, Activos.class);
        startActivity(iraactivo);
    }



}
