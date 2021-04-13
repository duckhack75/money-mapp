package com.example.moneymapp.presupuesto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentorapp.R;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.example.moneymapp.presupuesto.model.Viaje;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Viajes extends AppCompatActivity {

    private FirebaseUser user;
    private TextView vahotel, vaotrosviajes, totalpviajes, vatransporte, vacomidas, vaentretenimiento;
    private Button ingresarPresupuestoViajes;
    private Boolean comprobar;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario, correo;
    private EditText hotel, transporte, comidas, entretenimiento, otrosviajes;
    private Viaje nuevopviajes;
    private int c,int_photel, int_ptransporte, int_pcomidas, int_pentretenimiento, int_potrosviajes,
            int_totalpviajes, total_final_pviajes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes);

        hotel=(EditText)findViewById(R.id.edtHotel);
        transporte=(EditText)findViewById(R.id.edtTransporte);
        comidas=(EditText)findViewById(R.id.edtComidas);
        entretenimiento=(EditText)findViewById(R.id.edtEntretenimiento);
        otrosviajes=(EditText)findViewById(R.id.edtOtrospviajes);
        totalpviajes=(TextView)findViewById(R.id.txvTotalViajes);

        vahotel=(TextView)findViewById(R.id.txvVahotel);
        vatransporte=(TextView)findViewById(R.id.txvVatransporte);
        vacomidas=(TextView)findViewById(R.id.txvVacomidas);
        vaentretenimiento=(TextView)findViewById(R.id.txvVaentretenimiento);
        vaotrosviajes=(TextView)findViewById(R.id.txvVapotrosviajes);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Viajes");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Viaje obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Viaje.class);
                    vahotel.setText("$ "+separador(obj.getHotel()));
                    vatransporte.setText("$ "+separador(obj.getTransporte()));
                    vacomidas.setText("$ "+separador(obj.getComidas()));
                    vaentretenimiento.setText("$ "+separador(obj.getEntretenimiento()));
                    vaotrosviajes.setText("$ "+separador(obj.getOtrosGastosViajes()));
                    totalpviajes.setText("$ "+separador(obj.getTotalViajes()));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //volver a presupuesto
    public void irPresupuesto(View v){
        Intent irapresupuesto = new Intent(this, Presupuesto.class);
        startActivity(irapresupuesto);
    }


    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }

    //método para ingresar presupuesto
    public void ingresarPresupuestoViajes(View view) {
        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Viaje obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Viaje.class);
                    vahotel.setText("$ "+separador(obj.getHotel()));
                    vatransporte.setText("$ "+separador(obj.getTransporte()));
                    vacomidas.setText("$ "+separador(obj.getComidas()));
                    vaentretenimiento.setText("$ "+separador(obj.getEntretenimiento()));
                    vaotrosviajes.setText("$ "+separador(obj.getOtrosGastosViajes()));
                    totalpviajes.setText("$ "+separador(obj.getTotalViajes()));

                    //cálculo del total del presupuesto Viajes
                    int_photel=Integer.parseInt(obj.getHotel());
                    int_ptransporte=Integer.parseInt(obj.getTransporte());
                    int_pcomidas=Integer.parseInt(obj.getComidas());
                    int_pentretenimiento=Integer.parseInt(obj.getEntretenimiento());
                    int_potrosviajes=Integer.parseInt(obj.getOtrosGastosViajes());
                    int_totalpviajes=Integer.parseInt(obj.getTotalViajes());


                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_pviajes=0;

                    if(!hotel.getText().toString().isEmpty()){
                        int_photel=int_photel+Integer.parseInt(hotel.getText().toString());
                        total_final_pviajes=total_final_pviajes+Integer.parseInt(hotel.getText().toString());
                        mDatabase.child(clave).child("hotel").setValue(String.valueOf(int_photel));
                        hotel.setText("");
                    }
                    if(!transporte.getText().toString().isEmpty()){
                        int_ptransporte=int_ptransporte+Integer.parseInt(transporte.getText().toString());
                        total_final_pviajes=total_final_pviajes+Integer.parseInt(transporte.getText().toString());
                        mDatabase.child(clave).child("transporte").setValue(String.valueOf(int_ptransporte));
                        transporte.setText("");
                    }
                    if(!comidas.getText().toString().isEmpty()){
                        int_pcomidas=int_pcomidas+Integer.parseInt(comidas.getText().toString());
                        total_final_pviajes=total_final_pviajes+Integer.parseInt(comidas.getText().toString());
                        mDatabase.child(clave).child("comidas").setValue(String.valueOf(int_pcomidas));
                        comidas.setText("");
                    }
                    if(!entretenimiento.getText().toString().isEmpty()){
                        int_pentretenimiento=int_pentretenimiento+Integer.parseInt(entretenimiento.getText().toString());
                        total_final_pviajes=total_final_pviajes+Integer.parseInt(entretenimiento.getText().toString());
                        mDatabase.child(clave).child("entretenimiento").setValue(String.valueOf(int_pentretenimiento));
                        entretenimiento.setText("");
                    }
                    if(!otrosviajes.getText().toString().isEmpty()){
                        int_potrosviajes=int_potrosviajes+Integer.parseInt(otrosviajes.getText().toString());
                        total_final_pviajes=total_final_pviajes+Integer.parseInt(otrosviajes.getText().toString());
                        mDatabase.child(clave).child("otrospviajes").setValue(String.valueOf(int_potrosviajes));
                        otrosviajes.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_pviajes);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_pviajes=total_final_pviajes+int_totalpviajes;
                    mDatabase.child(clave).child("tviajes").setValue(String.valueOf(total_final_pviajes));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Viaje obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Viaje.class);
                                vahotel.setText("$ "+separador(obj3.getHotel()));
                                vatransporte.setText("$ "+separador(obj3.getTransporte()));
                                vacomidas.setText("$ "+separador(obj3.getComidas()));
                                vaentretenimiento.setText("$ "+separador(obj3.getEntretenimiento()));
                                vaotrosviajes.setText("$ "+separador(obj3.getOtrosGastosViajes()));
                                totalpviajes.setText("$ "+separador(obj3.getTotalViajes()));
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
                    nuevopviajes= new Viaje();
                    nuevopviajes.setCorreo(usuario);

                    if (hotel.getText().toString().isEmpty()){
                        nuevopviajes.setHotel("0");
                    }else {
                        nuevopviajes.setHotel(hotel.getText().toString());
                    }

                    if (transporte.getText().toString().isEmpty()){
                        nuevopviajes.setTransporte("0");
                    }else {
                        nuevopviajes.setTransporte(transporte.getText().toString());
                    }

                    if (comidas.getText().toString().isEmpty()){
                        nuevopviajes.setComidas("0");
                    }else {
                        nuevopviajes.setComidas(comidas.getText().toString());
                    }

                    if (entretenimiento.getText().toString().isEmpty()){
                        nuevopviajes.setEntretenimiento("0");
                    }else {
                        nuevopviajes.setEntretenimiento(entretenimiento.getText().toString());
                    }

                    if (otrosviajes.getText().toString().isEmpty()){
                        nuevopviajes.setOtrosGastosViajes("0");
                    }else {
                        nuevopviajes.setOtrosGastosViajes(otrosviajes.getText().toString());
                    }

                    //suma del total inicial
                    int_photel=Integer.parseInt(nuevopviajes.getHotel());
                    int_ptransporte=Integer.parseInt(nuevopviajes.getTransporte());
                    int_pcomidas=Integer.parseInt(nuevopviajes.getComidas());
                    int_pentretenimiento=Integer.parseInt(nuevopviajes.getEntretenimiento());
                    int_potrosviajes=Integer.parseInt(nuevopviajes.getOtrosGastosViajes());

                    total_final_pviajes=0;
                    total_final_pviajes=int_totalpviajes+int_photel+int_ptransporte+int_pcomidas+
                            int_pentretenimiento+ int_potrosviajes;
                    nuevopviajes.setTotalViajes(String.valueOf(total_final_pviajes));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_pviajes);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevopviajes);

                    //se realiza otra consulta para actualizar los datos
                    Query qu=mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Viaje obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Viaje.class);
                                vahotel.setText("$ "+separador(obj4.getHotel()));
                                vatransporte.setText("$ "+separador(obj4.getTransporte()));
                                vacomidas.setText("$ "+separador(obj4.getComidas()));
                                vaentretenimiento.setText("$ "+separador(obj4.getEntretenimiento()));
                                vaotrosviajes.setText("$ "+separador(obj4.getOtrosGastosViajes()));
                                totalpviajes.setText("$ "+separador(obj4.getTotalViajes()));

                            }
                            //limpiar campos input
                            hotel.setText("");
                            transporte.setText("");
                            comidas.setText("");
                            entretenimiento.setText("");
                            otrosviajes.setText("");
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
    public void sumarPresupuestoGlobal(final int v){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Presupuestoglobal m =datanapshot.getValue(Presupuestoglobal.class);
                    String clave=datanapshot.getKey();
                    z=z+Integer.parseInt(m.getPresupuestoTotal());
                    //modificacion
                    tDatabase.child(clave).child("presupuesto_total").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para sumar los valores ingresados por cada item al presupuesto global
    public void restarPresupuestoGlobal(final int n ){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Presupuestoglobal obj7 =datanapshot.getValue(Presupuestoglobal.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getPresupuestoTotal())-y;
                    //modificacion
                    tDatabase.child(clave).child("presupuesto_total").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para actualizar datos presupuesto auto
    public void actualizarPresupuestoViajes(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Viaje obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Viaje.class);
                    //cálculo del total del presupuesto Viajes
                    int_photel=Integer.parseInt(obj.getHotel());
                    int_ptransporte=Integer.parseInt(obj.getTransporte());
                    int_pcomidas=Integer.parseInt(obj.getComidas());
                    int_pentretenimiento=Integer.parseInt(obj.getEntretenimiento());
                    int_potrosviajes=Integer.parseInt(obj.getOtrosGastosViajes());
                    int_totalpviajes=Integer.parseInt(obj.getTotalViajes());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!hotel.getText().toString().isEmpty()){
                        int_photel=int_photel-Integer.parseInt(hotel.getText().toString());
                        contador=contador+Integer.parseInt(hotel.getText().toString());
                        if (int_photel<0){
                            valida=false;
                        }
                    }

                    if (!transporte.getText().toString().isEmpty()){
                        int_ptransporte=int_ptransporte-Integer.parseInt(transporte.getText().toString());
                        contador=contador+Integer.parseInt(transporte.getText().toString());
                        if (int_ptransporte<0){
                            valida=false;
                        }
                    }

                    if (!comidas.getText().toString().isEmpty()){
                        int_pcomidas=int_pcomidas-Integer.parseInt(comidas.getText().toString());
                        contador=contador+Integer.parseInt(comidas.getText().toString());
                        if (int_pcomidas<0){
                            valida=false;
                        }
                    }

                    if (!entretenimiento.getText().toString().isEmpty()){
                        int_pentretenimiento=int_pentretenimiento-Integer.parseInt(entretenimiento.getText().toString());
                        contador=contador+Integer.parseInt(entretenimiento.getText().toString());
                        if (int_pentretenimiento<0){
                            valida=false;
                        }
                    }

                    if (!otrosviajes.getText().toString().isEmpty() ){
                        int_potrosviajes=int_potrosviajes-Integer.parseInt(otrosviajes.getText().toString());
                        contador=contador+Integer.parseInt(otrosviajes.getText().toString());
                        if (int_potrosviajes<0){
                            valida=false;
                        }
                    }

                    //validación de campos llenos
                    if (hotel.getText().toString().isEmpty() && transporte.getText().toString().isEmpty()
                            && comidas.getText().toString().isEmpty() && entretenimiento.getText().toString().isEmpty()
                            && otrosviajes.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Viajes.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarPresupuestoGlobal(contador);//método que resta al presupuesto globa
                        if(!hotel.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("hotel").setValue(String.valueOf(int_photel));
                            int_totalpviajes=int_totalpviajes-Integer.parseInt(hotel.getText().toString());
                            mDatabase.child(clave).child("tviajes").setValue(String.valueOf(int_totalpviajes));
                            hotel.setText("");
                        }

                        if(!transporte.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("transporte").setValue(String.valueOf(int_ptransporte));
                            int_totalpviajes=int_totalpviajes-Integer.parseInt(transporte.getText().toString());
                            mDatabase.child(clave).child("tviajes").setValue(String.valueOf(int_totalpviajes));
                            transporte.setText("");
                        }

                        if(!comidas.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("comidas").setValue(String.valueOf(int_pcomidas));
                            int_totalpviajes=int_totalpviajes-Integer.parseInt(comidas.getText().toString());
                            mDatabase.child(clave).child("tviajes").setValue(String.valueOf(int_totalpviajes));
                            comidas.setText("");
                        }

                        if(!entretenimiento.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("entretenimiento").setValue(String.valueOf(int_pentretenimiento));
                            int_totalpviajes=int_totalpviajes-Integer.parseInt(entretenimiento.getText().toString());
                            mDatabase.child(clave).child("tviajes").setValue(String.valueOf(int_totalpviajes));
                            entretenimiento.setText("");
                        }

                        if(!otrosviajes.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("otrospviajes").setValue(String.valueOf(int_potrosviajes));
                            int_totalpviajes=int_totalpviajes-Integer.parseInt(otrosviajes.getText().toString());
                            mDatabase.child(clave).child("tviajes").setValue(String.valueOf(int_totalpviajes));
                            otrosviajes.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Viaje obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Viaje.class);
                                    vahotel.setText("$ "+separador(obj3.getHotel()));
                                    vatransporte.setText("$ "+separador(obj3.getTransporte()));
                                    vacomidas.setText("$ "+separador(obj3.getComidas()));
                                    vaentretenimiento.setText("$ "+separador(obj3.getEntretenimiento()));
                                    vaotrosviajes.setText("$ "+separador(obj3.getOtrosGastosViajes()));
                                    totalpviajes.setText("$ "+separador(obj3.getTotalViajes()));


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Viajes.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        //limpiar campos input
                        hotel.setText("");
                        transporte.setText("");
                        comidas.setText("");
                        entretenimiento.setText("");
                        otrosviajes.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
