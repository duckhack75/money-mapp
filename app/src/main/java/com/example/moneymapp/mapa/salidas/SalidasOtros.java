package com.example.moneymapp.mapa.salidas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.salidas.model.Salida;
import com.example.moneymapp.mapa.salidas.model.SalidaOtro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SalidasOtros extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vacuentas, vaahorro, vaotrossalidas, totalotros;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText cuentas, ahorro, otrossalidas;
    private SalidaOtro nuevootros;
    private int c, int_cuentas, int_ahorro, int_otrossalidas,
            int_totalotrossalidas, total_final_otros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salidas_otros);



        cuentas= (EditText) findViewById(R.id.edtScuentasporcobrar);
        ahorro = (EditText) findViewById(R.id.edtSahorro);
        otrossalidas = (EditText) findViewById(R.id.edtOtrosotrossalidas);
        totalotros = (TextView) findViewById(R.id.txvTotalSotros);



        vacuentas = (TextView) findViewById(R.id.txvVScuentasporcobrar);
        vaahorro = (TextView) findViewById(R.id.txvVSahorro);
        vaotrossalidas = (TextView) findViewById(R.id.txvVaotrosotrossalidas);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario = user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Salida_otros");
        //acceso a la tabla Presupuesto activo glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Salidas");
        //muestra de valores actuales, usando where usuario
        Query query = mDatabase.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SalidaOtro obj;
                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    obj = datanapshot.getValue(SalidaOtro.class);
                    vacuentas.setText("$ " + separador(obj.getCuentas()));
                    vaahorro.setText("$ " + separador(obj.getAhorro()));
                    vaotrossalidas.setText("$ " + separador(obj.getOtrasSalidas()));
                    totalotros.setText("$ " + separador(obj.getTotalOtrasSalidas()));
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


    //método para volver a salidas
    public void iraAsalidas(View v){
        Intent iraactivo = new Intent(this, Salidas.class);
        startActivity(iraactivo);
    }

    //método para ingresar presupuesto
    public void ingresarTotalOtrosSalidas(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q = mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c = 0;
                SalidaOtro obj;

                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(SalidaOtro.class);
                    //cálculo del total del presupuesto Autos
                    int_cuentas = Integer.parseInt(obj.getCuentas());
                    int_ahorro = Integer.parseInt(obj.getAhorro());
                    int_otrossalidas = Integer.parseInt(obj.getOtrasSalidas());
                    int_totalotrossalidas = Integer.parseInt(obj.getTotalOtrasSalidas());


                    //obtención de la "primary key"
                    String clave = datanapshot.getKey();
                    total_final_otros= 0;

                    if (!cuentas.getText().toString().isEmpty()) {
                        int_cuentas = int_cuentas + Integer.parseInt(cuentas.getText().toString());
                        total_final_otros = total_final_otros + Integer.parseInt(cuentas.getText().toString());
                        mDatabase.child(clave).child("cuentas").setValue(String.valueOf(int_cuentas));
                        cuentas.setText("");
                    }
                    if (!ahorro.getText().toString().isEmpty()) {
                        int_ahorro = int_ahorro + Integer.parseInt(ahorro.getText().toString());
                        total_final_otros = total_final_otros + Integer.parseInt(ahorro.getText().toString());
                        mDatabase.child(clave).child("ahorro").setValue(String.valueOf(int_ahorro));
                        ahorro.setText("");
                    }
                    if (!otrossalidas.getText().toString().isEmpty()) {
                        int_otrossalidas = int_otrossalidas + Integer.parseInt(otrossalidas.getText().toString());
                        total_final_otros = total_final_otros + Integer.parseInt(otrossalidas.getText().toString());
                        mDatabase.child(clave).child("otrossalidas").setValue(String.valueOf(int_otrossalidas));
                        otrossalidas.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalSalidasOtros(total_final_otros);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_otros = total_final_otros + int_totalotrossalidas;
                    mDatabase.child(clave).child("totalotrossalidas").setValue(String.valueOf(total_final_otros));

                    //se realiza otra consulta para actualizar los datos
                    Query q = mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SalidaOtro obj3;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj3 = datanapshot.getValue(SalidaOtro.class);
                                vacuentas.setText("$ " + separador(obj3.getCuentas()));
                                vaahorro.setText("$ " + separador(obj3.getAhorro()));
                                vaotrossalidas.setText("$ " + separador(obj3.getOtrasSalidas()));
                                totalotros.setText("$ " + separador(obj3.getTotalOtrasSalidas()));
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
                    nuevootros = new SalidaOtro();
                    nuevootros.setCorreo(usuario);

                    if (cuentas.getText().toString().isEmpty()) {
                        nuevootros.setCuentas("0");
                    } else {
                        nuevootros.setCuentas(cuentas.getText().toString());
                    }

                    if (ahorro.getText().toString().isEmpty()) {
                        nuevootros.setAhorro("0");
                    } else {
                        nuevootros.setAhorro(ahorro.getText().toString());
                    }

                    if (otrossalidas.getText().toString().isEmpty()) {
                        nuevootros.setOtrasSalidas("0");
                    } else {
                        nuevootros.setOtrasSalidas(otrossalidas.getText().toString());
                    }


                    //suma del total inicial
                    int_cuentas = Integer.parseInt(nuevootros.getCuentas());
                    int_ahorro = Integer.parseInt(nuevootros.getAhorro());
                    int_otrossalidas = Integer.parseInt(nuevootros.getOtrasSalidas());

                    total_final_otros = 0;
                    total_final_otros = int_totalotrossalidas + int_cuentas + int_ahorro + int_otrossalidas;
                    nuevootros.setTotalOtrasSalidas(String.valueOf(total_final_otros));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalSalidasOtros(total_final_otros);
                    String clave = mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevootros);

                    //se realiza otra consulta para actualizar los datos
                    Query qu = mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SalidaOtro obj4;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj4 = datanapshot.getValue(SalidaOtro.class);
                                vacuentas.setText("$ " + separador(obj4.getCuentas()));
                                vaahorro.setText("$ " + separador(obj4.getAhorro()));
                                vaotrossalidas.setText("$ " + separador(obj4.getOtrasSalidas()));
                                totalotros.setText("$ " + separador(obj4.getTotalOtrasSalidas()));
                            }
                            //limpiar campos input
                            cuentas.setText("");
                            ahorro.setText("");
                            otrossalidas.setText("");
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
    public void sumarTotalSalidasOtros(final int v){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Salida m =datanapshot.getValue(Salida.class);
                    String clave=datanapshot.getKey();
                    z=z+Integer.parseInt(m.getSalidaTotal());
                    //modificacion
                    tDatabase.child(clave).child("salidas_total").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para restar los valores ingresados por cada item al presupuesto global
    public void restarTotalSalidas(final int n ){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Salida obj7 =datanapshot.getValue(Salida.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getSalidaTotal())-y;
                    //modificacion
                    tDatabase.child(clave).child("salidas_total").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para actualizar datos presupuesto auto
    public void actualizarTotalOtrosSalidas(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SalidaOtro obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(SalidaOtro.class);
                    //cálculo del total del presupuesto Autos
                    int_cuentas = Integer.parseInt(obj.getCuentas());
                    int_ahorro = Integer.parseInt(obj.getAhorro());
                    int_otrossalidas = Integer.parseInt(obj.getOtrasSalidas());
                    int_totalotrossalidas = Integer.parseInt(obj.getTotalOtrasSalidas());

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

                    if (!ahorro.getText().toString().isEmpty()){
                        int_ahorro=int_ahorro-Integer.parseInt(ahorro.getText().toString());
                        contador=contador+Integer.parseInt(ahorro.getText().toString());
                        if (int_ahorro<0){
                            valida=false;
                        }
                    }

                    if (!otrossalidas.getText().toString().isEmpty()){
                        int_otrossalidas=int_otrossalidas-Integer.parseInt(otrossalidas.getText().toString());
                        contador=contador+Integer.parseInt(otrossalidas.getText().toString());
                        if (int_otrossalidas<0){
                            valida=false;
                        }
                    }



                    //validación de campos llenos
                    if (cuentas.getText().toString().isEmpty() && ahorro.getText().toString().isEmpty()
                            && otrossalidas.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(SalidasOtros.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                       restarTotalSalidas(contador);//método que resta al presupuesto globa
                        if(!cuentas.getText().toString().isEmpty()){
                            int_totalotrossalidas=int_totalotrossalidas-Integer.parseInt(cuentas.getText().toString());
                            mDatabase.child(clave).child("totalotrossalidas").setValue(String.valueOf(int_totalotrossalidas));
                            mDatabase.child(clave).child("cuentas").setValue(String.valueOf(int_cuentas));
                            cuentas.setText("");
                        }

                        if(!ahorro.getText().toString().isEmpty()){
                            int_totalotrossalidas=int_totalotrossalidas-Integer.parseInt(ahorro.getText().toString());
                            mDatabase.child(clave).child("totalotrossalidas").setValue(String.valueOf(int_totalotrossalidas));
                            mDatabase.child(clave).child("ahorro").setValue(String.valueOf(int_ahorro));
                            ahorro.setText("");
                        }


                        if(!otrossalidas.getText().toString().isEmpty()){
                            int_totalotrossalidas=int_totalotrossalidas-Integer.parseInt(otrossalidas.getText().toString());
                            mDatabase.child(clave).child("totalotrossalidas").setValue(String.valueOf(int_totalotrossalidas));
                            mDatabase.child(clave).child("otrossalidas").setValue(String.valueOf(int_otrossalidas));
                            otrossalidas.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                SalidaOtro obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3 = datanapshot.getValue(SalidaOtro.class);
                                    vacuentas.setText("$ " + separador(obj3.getCuentas()));
                                    vaahorro.setText("$ " + separador(obj3.getAhorro()));
                                    vaotrossalidas.setText("$ " + separador(obj3.getOtrasSalidas()));
                                    totalotros.setText("$ " + separador(obj3.getTotalOtrasSalidas()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(SalidasOtros.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        //limpiar campos input
                        cuentas.setText("");
                        ahorro.setText("");
                        otrossalidas.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
