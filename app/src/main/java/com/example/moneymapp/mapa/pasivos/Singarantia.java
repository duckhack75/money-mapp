package com.example.moneymapp.mapa.pasivos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.pasivos.model.Agarantia;
import com.example.moneymapp.mapa.pasivos.model.Pasivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Singarantia extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vatarjetacredito, vaprestamopersonal, totalnogarantia;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText tarjetacredito, prestamopersonal;
    private Agarantia nuevonogarantia;
    private int c, int_tarjetacredito, int_prestamopersonal,
            int_totalnogarantia, total_final_nogarantia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singarantia);

        tarjetacredito = (EditText) findViewById(R.id.edtStarjetacredito);
        prestamopersonal = (EditText) findViewById(R.id.edtSprestamopersonal);
        totalnogarantia = (TextView) findViewById(R.id.txvTotalSingarantia);

        vatarjetacredito = (TextView) findViewById(R.id.txvVStarjetacredito);
        vaprestamopersonal = (TextView) findViewById(R.id.txvVSprestamopersonal);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario = user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Pasivossingarantia");
        //acceso a la tabla Presupuesto activo glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Mpasivos");
        //muestra de valores actuales, usando where usuario
        Query query = mDatabase.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Agarantia obj;
                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    obj = datanapshot.getValue(Agarantia.class);
                    vatarjetacredito.setText("$ " + separador(obj.getTarjetaCredito()));
                    vaprestamopersonal.setText("$ " + separador(obj.getPrestamoPersonal()));
                    totalnogarantia.setText("$ " + separador(obj.getTotalSinGarantia()));
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
    public void ingresarTotalSinGarantia(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q = mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c = 0;
                Agarantia obj;

                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(Agarantia.class);
                    //cálculo del total del presupuesto Autos
                    int_tarjetacredito = Integer.parseInt(obj.getTarjetaCredito());
                    int_prestamopersonal = Integer.parseInt(obj.getPrestamoPersonal());
                    int_totalnogarantia = Integer.parseInt(obj.getTotalSinGarantia());


                    //obtención de la "primary key"
                    String clave = datanapshot.getKey();
                    total_final_nogarantia = 0;

                    if (!tarjetacredito.getText().toString().isEmpty()) {
                        int_tarjetacredito = int_tarjetacredito + Integer.parseInt(tarjetacredito.getText().toString());
                        total_final_nogarantia = total_final_nogarantia + Integer.parseInt(tarjetacredito.getText().toString());
                        mDatabase.child(clave).child("tarjeta_credito").setValue(String.valueOf(int_tarjetacredito));
                        tarjetacredito.setText("");
                    }
                    if (!prestamopersonal.getText().toString().isEmpty()) {
                        int_prestamopersonal = int_prestamopersonal + Integer.parseInt(prestamopersonal.getText().toString());
                        total_final_nogarantia = total_final_nogarantia + Integer.parseInt(prestamopersonal.getText().toString());
                        mDatabase.child(clave).child("prestamo_personal").setValue(String.valueOf(int_prestamopersonal));
                        prestamopersonal.setText("");
                    }

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalPasivos(total_final_nogarantia);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_nogarantia = total_final_nogarantia + int_totalnogarantia;
                    mDatabase.child(clave).child("tsingarantia").setValue(String.valueOf(total_final_nogarantia));

                    //se realiza otra consulta para actualizar los datos
                    Query q = mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Agarantia obj3;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj3 = datanapshot.getValue(Agarantia.class);
                                vatarjetacredito.setText("$ " + separador(obj3.getTarjetaCredito()));
                                vaprestamopersonal.setText("$ " + separador(obj3.getPrestamoPersonal()));
                                totalnogarantia.setText("$ " + separador(obj3.getTotalSinGarantia()));
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
                    nuevonogarantia = new Agarantia();
                    nuevonogarantia.setCorreo(usuario);

                    if (tarjetacredito.getText().toString().isEmpty()) {
                        nuevonogarantia.setTarjetaCredito("0");
                    } else {
                        nuevonogarantia.setTarjetaCredito(tarjetacredito.getText().toString());
                    }

                    if (prestamopersonal.getText().toString().isEmpty()) {
                        nuevonogarantia.setPrestamoPersonal("0");
                    } else {
                        nuevonogarantia.setPrestamoPersonal(prestamopersonal.getText().toString());
                    }


                    //suma del total inicial
                    int_tarjetacredito = Integer.parseInt(nuevonogarantia.getTarjetaCredito());
                    int_prestamopersonal = Integer.parseInt(nuevonogarantia.getPrestamoPersonal());


                    total_final_nogarantia = 0;
                    total_final_nogarantia = int_totalnogarantia + int_tarjetacredito + int_prestamopersonal;
                    nuevonogarantia.setTotalSinGarantia(String.valueOf(total_final_nogarantia));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalPasivos(total_final_nogarantia);
                    String clave = mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevonogarantia);

                    //se realiza otra consulta para actualizar los datos
                    Query qu = mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Agarantia obj4;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj4 = datanapshot.getValue(Agarantia.class);
                                vatarjetacredito.setText("$ " + separador(obj4.getTarjetaCredito()));
                                vaprestamopersonal.setText("$ " + separador(obj4.getPrestamoPersonal()));
                                totalnogarantia.setText("$ " + separador(obj4.getTotalSinGarantia()));

                            }
                            //limpiar campos input
                            tarjetacredito.setText("");
                            prestamopersonal.setText("");
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


    //método para sumar los valores ingresados por cada item al presupuesto global
    public void sumarTotalPasivos(final int v){
        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Pasivo m =datanapshot.getValue(Pasivo.class);
                    String clave=datanapshot.getKey();
                    z=z+Integer.parseInt(m.getPasivosTotal());
                    //modificacion
                    tDatabase.child(clave).child("pasivo_total").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para actualizar datos presupuesto auto
    public void actualizarTotalSinGarantia(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Agarantia obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(Agarantia.class);
                    //cálculo del total del presupuesto Autos
                    int_tarjetacredito = Integer.parseInt(obj.getTarjetaCredito());
                    int_prestamopersonal = Integer.parseInt(obj.getPrestamoPersonal());
                    int_totalnogarantia = Integer.parseInt(obj.getTotalSinGarantia());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!tarjetacredito.getText().toString().isEmpty()){
                        int_tarjetacredito=int_tarjetacredito-Integer.parseInt(tarjetacredito.getText().toString());
                        contador=contador+Integer.parseInt(tarjetacredito.getText().toString());
                        if (int_tarjetacredito<0){
                            valida=false;
                        }
                    }

                    if (!prestamopersonal.getText().toString().isEmpty()){
                        int_prestamopersonal=int_prestamopersonal-Integer.parseInt(prestamopersonal.getText().toString());
                        contador=contador+Integer.parseInt(prestamopersonal.getText().toString());
                        if (int_prestamopersonal<0){
                            valida=false;
                        }
                    }


                    //validación de campos llenos
                    if (tarjetacredito.getText().toString().isEmpty() && prestamopersonal.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Singarantia.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarTotalPasivos(contador);//método que resta al presupuesto globa
                        if(!tarjetacredito.getText().toString().isEmpty()){
                            int_totalnogarantia=int_totalnogarantia-Integer.parseInt(tarjetacredito.getText().toString());
                            mDatabase.child(clave).child("tsingarantia").setValue(String.valueOf(int_totalnogarantia));
                            mDatabase.child(clave).child("tarjeta_credito").setValue(String.valueOf(int_tarjetacredito));
                            tarjetacredito.setText("");
                        }

                        if(!prestamopersonal.getText().toString().isEmpty()){
                            int_totalnogarantia=int_totalnogarantia-Integer.parseInt(prestamopersonal.getText().toString());
                            mDatabase.child(clave).child("tsingarantia").setValue(String.valueOf(int_totalnogarantia));
                            mDatabase.child(clave).child("prestamo_personal").setValue(String.valueOf(int_prestamopersonal));
                            prestamopersonal.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Agarantia obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3 = datanapshot.getValue(Agarantia.class);
                                    vatarjetacredito.setText("$ " + separador(obj3.getTarjetaCredito()));
                                    vaprestamopersonal.setText("$ " + separador(obj3.getPrestamoPersonal()));
                                    totalnogarantia.setText("$ " + separador(obj3.getTotalSinGarantia()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Singarantia.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        //limpiar campos input
                        tarjetacredito.setText("");
                        prestamopersonal.setText("");

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //método para sumar los valores ingresados por cada item al presupuesto global
    public void restarTotalPasivos(final int n ){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Pasivo obj7 =datanapshot.getValue(Pasivo.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getPasivosTotal())-y;
                    //modificacion
                    tDatabase.child(clave).child("pasivo_total").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    //método para volver a activos
    public void iraMPasivos(View v){
        Intent irapasivos = new Intent(this, Pasivvos.class);
        startActivity(irapasivos);
    }



}
