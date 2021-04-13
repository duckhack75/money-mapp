package com.example.moneymapp.presupuesto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentorapp.R;
import com.example.moneymapp.presupuesto.model.Hijo;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Hijos extends AppCompatActivity {

    private FirebaseUser user;
    private TextView vaescuela, vadineroextra, totalhijos, vacelular, vanana, vautiles, vaclases,
                    vajuguetes, vaotroshijos;
    private Boolean comprobar;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario, correo;
    private EditText pescuela, pdineroextra, pcelular, pnana, putiles, pclases, pjuguetes, potroshijos;
    private Hijo nuevophijo;
    private int c,int_pescuela, int_pdineroextra, int_pcelular, int_pnana, int_putiles,int_pclases,
                int_pjuguetes, int_potroshijos, int_totalhijos, total_final_hijos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hijos);

        pescuela=(EditText)findViewById(R.id.edtEscuela);
        pdineroextra=(EditText)findViewById(R.id.edtDineroextra);
        pcelular=(EditText)findViewById(R.id.edtCelular);
        pnana=(EditText)findViewById(R.id.edtNana);
        putiles=(EditText)findViewById(R.id.edtUtiles);
        pclases=(EditText)findViewById(R.id.edtClases);
        pjuguetes=(EditText)findViewById(R.id.edtJuguetes);
        potroshijos=(EditText)findViewById(R.id.edtOtroshijos);
        totalhijos=(TextView)findViewById(R.id.txvTotalhijos);

        vaescuela=(TextView)findViewById(R.id.txvVaescuela);
        vadineroextra=(TextView)findViewById(R.id.txvVadineroextra);
        vacelular=(TextView)findViewById(R.id.txvVacelular);
        vanana=(TextView)findViewById(R.id.txvVanana);
        vautiles=(TextView)findViewById(R.id.txvVautiles);
        vaclases=(TextView)findViewById(R.id.txvVaclases);
        vajuguetes=(TextView)findViewById(R.id.txvVajuguetes);
        vaotroshijos=(TextView)findViewById(R.id.txvVaotroshijos);


        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Casas de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Hijos");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Hijo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Hijo.class);
                    vaescuela.setText("$ "+separador(obj.getEscuela()));
                    vadineroextra.setText("$ "+separador(obj.getDineroExtra()));
                    vacelular.setText("$ "+separador(obj.getCelular()));
                    vanana.setText("$ "+separador(obj.getNana()));
                    vautiles.setText("$ "+separador(obj.getUtiles()));
                    vaclases.setText("$ "+separador(obj.getClases()));
                    vajuguetes.setText("$ "+separador(obj.getJuguetes()));
                    vaotroshijos.setText("$ "+separador(obj.getOtrosGastosHijos()));
                    totalhijos.setText("$ "+separador(obj.getTotalHijos()));

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
    public void ingresarPresupuestoHijos(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Hijo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Hijo.class);
                    vaescuela.setText("$ "+separador(obj.getEscuela()));
                    vadineroextra.setText("$ "+separador(obj.getDineroExtra()));
                    vacelular.setText("$ "+separador(obj.getCelular()));
                    vanana.setText("$ "+separador(obj.getNana()));
                    vautiles.setText("$ "+separador(obj.getUtiles()));
                    vaclases.setText("$ "+separador(obj.getClases()));
                    vajuguetes.setText("$ "+separador(obj.getJuguetes()));
                    vaotroshijos.setText("$ "+separador(obj.getOtrosGastosHijos()));
                    totalhijos.setText("$ "+separador(obj.getTotalHijos()));

                    //cálculo del total del presupuesto Casas
                    int_pescuela=Integer.parseInt(obj.getEscuela());
                    int_pdineroextra=Integer.parseInt(obj.getDineroExtra());
                    int_pcelular=Integer.parseInt(obj.getCelular());
                    int_pnana=Integer.parseInt(obj.getNana());
                    int_putiles=Integer.parseInt(obj.getUtiles());
                    int_pclases=Integer.parseInt(obj.getClases());
                    int_pjuguetes=Integer.parseInt(obj.getJuguetes());
                    int_potroshijos=Integer.parseInt(obj.getOtrosGastosHijos());
                    int_totalhijos=Integer.parseInt(obj.getTotalHijos());

                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_hijos=0;

                    if(!pescuela.getText().toString().isEmpty()){
                        int_pescuela=int_pescuela+Integer.parseInt(pescuela.getText().toString());
                        mDatabase.child(clave).child("escuela").setValue(String.valueOf(int_pescuela));
                        total_final_hijos=total_final_hijos+Integer.parseInt(pescuela.getText().toString());
                        pescuela.setText("");
                    }
                    if(!pdineroextra.getText().toString().isEmpty()){
                        int_pdineroextra=int_pdineroextra+Integer.parseInt(pdineroextra.getText().toString());
                        mDatabase.child(clave).child("dinero_extra").setValue(String.valueOf(int_pdineroextra));
                        total_final_hijos=total_final_hijos+Integer.parseInt(pdineroextra.getText().toString());
                        pdineroextra.setText("");
                    }
                    if(!pcelular.getText().toString().isEmpty()){
                        int_pcelular=int_pcelular+Integer.parseInt(pcelular.getText().toString());
                        mDatabase.child(clave).child("celular").setValue(String.valueOf(int_pcelular));
                        total_final_hijos=total_final_hijos+Integer.parseInt(pcelular.getText().toString());
                        pcelular.setText("");
                    }
                    if(!pnana.getText().toString().isEmpty()){
                        int_pnana=int_pnana+Integer.parseInt(pnana.getText().toString());
                        mDatabase.child(clave).child("nana").setValue(String.valueOf(int_pnana));
                        total_final_hijos=total_final_hijos+Integer.parseInt(pnana.getText().toString());
                        pnana.setText("");
                    }
                    if(!putiles.getText().toString().isEmpty()){
                        int_putiles=int_putiles+Integer.parseInt(putiles.getText().toString());
                        mDatabase.child(clave).child("utiles").setValue(String.valueOf(int_putiles));
                        total_final_hijos=total_final_hijos+Integer.parseInt(putiles.getText().toString());
                        putiles.setText("");
                    }
                    if(!pclases.getText().toString().isEmpty()){
                        int_pclases=int_pclases+Integer.parseInt(pclases.getText().toString());
                        mDatabase.child(clave).child("clases").setValue(String.valueOf(int_pclases));
                        total_final_hijos=total_final_hijos+Integer.parseInt(pclases.getText().toString());
                        pclases.setText("");
                    }
                    if(!pjuguetes.getText().toString().isEmpty()){
                        int_pjuguetes=int_pjuguetes+Integer.parseInt(pjuguetes.getText().toString());
                        mDatabase.child(clave).child("juguetes").setValue(String.valueOf(int_pjuguetes));
                        total_final_hijos=total_final_hijos+Integer.parseInt(pjuguetes.getText().toString());
                        pjuguetes.setText("");
                    }

                    if(!potroshijos.getText().toString().isEmpty()){
                        int_potroshijos=int_potroshijos+Integer.parseInt(potroshijos.getText().toString());
                        mDatabase.child(clave).child("otros_phijos").setValue(String.valueOf(int_potroshijos));
                        total_final_hijos=total_final_hijos+Integer.parseInt(potroshijos.getText().toString());
                        potroshijos.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_hijos);
                    //ingreso del total final a la bbdd
                    total_final_hijos=total_final_hijos+int_totalhijos;
                    mDatabase.child(clave).child("thijos").setValue(String.valueOf(total_final_hijos));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Hijo obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Hijo.class);
                                vaescuela.setText("$ "+separador(obj3.getEscuela()));
                                vadineroextra.setText("$ "+separador(obj3.getDineroExtra()));
                                vacelular.setText("$ "+separador(obj3.getCelular()));
                                vanana.setText("$ "+separador(obj3.getNana()));
                                vautiles.setText("$ "+separador(obj3.getUtiles()));
                                vaclases.setText("$ "+separador(obj3.getClases()));
                                vajuguetes.setText("$ "+separador(obj3.getJuguetes()));
                                vaotroshijos.setText("$ "+separador(obj3.getOtrosGastosHijos()));
                                totalhijos.setText("$ "+separador(obj3.getTotalHijos()));

                            }
                            //limpiar campos input
                            pescuela.setText("");
                            pdineroextra.setText("");
                            pcelular.setText("");
                            pnana.setText("");
                            putiles.setText("");
                            pclases.setText("");
                            pjuguetes.setText("");
                            potroshijos.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if(c!=0){
                    //actualización del presupuesto del usuario



                }else{
                    //creo presupuesto hijos
                    nuevophijo= new Hijo();
                    nuevophijo.setCorreo(usuario);

                    if (pescuela.getText().toString().isEmpty()){
                        nuevophijo.setEscuela("0");
                    }else {
                        nuevophijo.setEscuela(pescuela.getText().toString());
                    }

                    if (pdineroextra.getText().toString().isEmpty()){
                        nuevophijo.setDineroExtra("0");
                    }else {
                        nuevophijo.setDineroExtra(pdineroextra.getText().toString());
                    }

                    if (pcelular.getText().toString().isEmpty()){
                        nuevophijo.setCelular("0");
                    }else {
                        nuevophijo.setCelular(pcelular.getText().toString());
                    }

                    if (pnana.getText().toString().isEmpty()){
                        nuevophijo.setNana("0");
                    }else {
                        nuevophijo.setNana(pnana.getText().toString());
                    }

                    if (putiles.getText().toString().isEmpty()){
                        nuevophijo.setUtiles("0");
                    }else {
                        nuevophijo.setUtiles(putiles.getText().toString());
                    }

                    if (pclases.getText().toString().isEmpty()){
                        nuevophijo.setClases("0");
                    }else {
                        nuevophijo.setClases(pclases.getText().toString());
                    }

                    if (pjuguetes.getText().toString().isEmpty()){
                        nuevophijo.setJuguetes("0");
                    }else {
                        nuevophijo.setJuguetes(pjuguetes.getText().toString());
                    }

                    if (potroshijos.getText().toString().isEmpty()){
                        nuevophijo.setOtrosGastosHijos("0");
                    }else {
                        nuevophijo.setOtrosGastosHijos(potroshijos.getText().toString());
                    }


                    //suma del total inicial
                    int_pescuela=Integer.parseInt(nuevophijo.getEscuela());
                    int_pdineroextra=Integer.parseInt(nuevophijo.getDineroExtra());
                    int_pcelular=Integer.parseInt(nuevophijo.getCelular());
                    int_pnana=Integer.parseInt(nuevophijo.getNana());
                    int_putiles=Integer.parseInt(nuevophijo.getUtiles());
                    int_pclases=Integer.parseInt(nuevophijo.getClases());
                    int_pjuguetes=Integer.parseInt(nuevophijo.getJuguetes());
                    int_potroshijos=Integer.parseInt(nuevophijo.getOtrosGastosHijos());


                    total_final_hijos=0;
                    total_final_hijos=int_totalhijos+int_pescuela+int_pdineroextra+int_pcelular+int_pnana+
                            int_putiles+int_pclases+int_pjuguetes+ int_potroshijos;

                    nuevophijo.setTotalHijos(String.valueOf(total_final_hijos));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_hijos);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevophijo);

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Hijo obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Hijo.class);
                                vaescuela.setText("$ "+separador(obj4.getEscuela()));
                                vadineroextra.setText("$ "+separador(obj4.getDineroExtra()));
                                vacelular.setText("$ "+separador(obj4.getCelular()));
                                vanana.setText("$ "+separador(obj4.getNana()));
                                vautiles.setText("$ "+separador(obj4.getUtiles()));
                                vaclases.setText("$ "+separador(obj4.getClases()));
                                vajuguetes.setText("$ "+separador(obj4.getJuguetes()));
                                vaotroshijos.setText("$ "+separador(obj4.getOtrosGastosHijos()));
                                totalhijos.setText("$ "+separador(obj4.getTotalHijos()));


                            }
                            //limpiar campos input
                            pescuela.setText("");
                            pdineroextra.setText("");
                            pcelular.setText("");
                            pnana.setText("");
                            putiles.setText("");
                            pclases.setText("");
                            pjuguetes.setText("");
                            potroshijos.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    pescuela.setText("");
                    pdineroextra.setText("");
                    pcelular.setText("");
                    pnana.setText("");
                    putiles.setText("");
                    pclases.setText("");
                    pjuguetes.setText("");
                    potroshijos.setText("");
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

    //método para actualizar datos presupuesto hijos
    public void actualizarPresupuestoHijos(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Hijo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Hijo.class);

                    //cálculo del total del presupuesto hijos
                    int_pescuela=Integer.parseInt(obj.getEscuela());
                    int_pdineroextra=Integer.parseInt(obj.getDineroExtra());
                    int_pcelular=Integer.parseInt(obj.getCelular());
                    int_pnana=Integer.parseInt(obj.getNana());
                    int_putiles=Integer.parseInt(obj.getUtiles());
                    int_pclases=Integer.parseInt(obj.getClases());
                    int_pjuguetes=Integer.parseInt(obj.getJuguetes());
                    int_potroshijos=Integer.parseInt(obj.getOtrosGastosHijos());
                    int_totalhijos=Integer.parseInt(obj.getTotalHijos());




                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!pescuela.getText().toString().isEmpty()){
                        int_pescuela=int_pescuela-Integer.parseInt(pescuela.getText().toString());
                        contador=contador+Integer.parseInt(pescuela.getText().toString());
                        if (int_pescuela<0){
                            valida=false;
                        }
                    }

                    if (!pdineroextra.getText().toString().isEmpty()){
                        int_pdineroextra=int_pdineroextra-Integer.parseInt(pdineroextra.getText().toString());
                        contador=contador+Integer.parseInt(pdineroextra.getText().toString());
                        if (int_pdineroextra<0){
                            valida=false;
                        }
                    }

                    if (!pcelular.getText().toString().isEmpty()){
                        int_pcelular=int_pcelular-Integer.parseInt(pcelular.getText().toString());
                        contador=contador+Integer.parseInt(pcelular.getText().toString());
                        if (int_pcelular<0){
                            valida=false;
                        }
                    }


                    if (!pnana.getText().toString().isEmpty()){
                        int_pnana=int_pnana-Integer.parseInt(pnana.getText().toString());
                        contador=contador+Integer.parseInt(pnana.getText().toString());
                        if (int_pnana<0){
                            valida=false;
                        }
                    }

                    if (!putiles.getText().toString().isEmpty()){
                        int_putiles=int_putiles-Integer.parseInt(putiles.getText().toString());
                        contador=contador+Integer.parseInt(putiles.getText().toString());
                        if (int_putiles<0){
                            valida=false;
                        }
                    }

                    if (!pclases.getText().toString().isEmpty()){
                        int_pclases=int_pclases-Integer.parseInt(pclases.getText().toString());
                        contador=contador+Integer.parseInt(pclases.getText().toString());
                        if (int_pclases<0){
                            valida=false;
                        }
                    }

                    if (!pjuguetes.getText().toString().isEmpty()){
                        int_pjuguetes=int_pjuguetes-Integer.parseInt(pjuguetes.getText().toString());
                        contador=contador+Integer.parseInt(pjuguetes.getText().toString());
                        if (int_pjuguetes<0){
                            valida=false;
                        }
                    }

                    if (!potroshijos.getText().toString().isEmpty()){
                        int_potroshijos=int_potroshijos-Integer.parseInt(potroshijos.getText().toString());
                        contador=contador+Integer.parseInt(potroshijos.getText().toString());
                        if (int_potroshijos<0){
                            valida=false;
                        }
                    }

                    //validación de campos llenos
                    if (pescuela.getText().toString().isEmpty() && pdineroextra.getText().toString().isEmpty()
                            && pcelular.getText().toString().isEmpty() && pnana.getText().toString().isEmpty()
                            && putiles.getText().toString().isEmpty() && pclases.getText().toString().isEmpty()
                            && pjuguetes.getText().toString().isEmpty()&& potroshijos.getText().toString().isEmpty()){
                        valida2=false;
                    }


                    //cálculo del total del presupuesto Casas
                    if (valida2==false){
                        Toast.makeText(Hijos.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){


                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarPresupuestoGlobal(contador);//método que resta al presupuesto globa
                        if(!pescuela.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(pescuela.getText().toString());
                            mDatabase.child(clave).child("escuela").setValue(String.valueOf(int_pescuela));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            pescuela.setText("");
                        }

                        if(!pdineroextra.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(pdineroextra.getText().toString());
                            mDatabase.child(clave).child("dinero_extra").setValue(String.valueOf(int_pdineroextra));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            pdineroextra.setText("");
                        }

                        if(!pcelular.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(pcelular.getText().toString());
                            mDatabase.child(clave).child("celular").setValue(String.valueOf(int_pcelular));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            pcelular.setText("");
                        }

                        if(!pnana.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(pnana.getText().toString());
                            mDatabase.child(clave).child("nana").setValue(String.valueOf(int_pnana));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            pnana.setText("");
                        }

                        if(!putiles.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(putiles.getText().toString());
                            mDatabase.child(clave).child("utiles").setValue(String.valueOf(int_putiles));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            putiles.setText("");
                        }

                        if(!pclases.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(pclases.getText().toString());
                            mDatabase.child(clave).child("clases").setValue(String.valueOf(int_pclases));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            pclases.setText("");
                        }

                        if(!pjuguetes.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(pjuguetes.getText().toString());
                            mDatabase.child(clave).child("juguetes").setValue(String.valueOf(int_pjuguetes));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            pjuguetes.setText("");
                        }

                        if(!potroshijos.getText().toString().isEmpty()){
                            int_totalhijos=int_totalhijos-Integer.parseInt(potroshijos.getText().toString());
                            mDatabase.child(clave).child("otros_phijos").setValue(String.valueOf(int_potroshijos));
                            mDatabase.child(clave).child("thijos").setValue(String.valueOf(int_totalhijos));
                            potroshijos.setText("");
                        }



                        //else if para seleccion de que servicio modificar
                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Hijo obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Hijo.class);
                                    vaescuela.setText("$ "+separador(obj3.getEscuela()));
                                    vadineroextra.setText("$ "+separador(obj3.getDineroExtra()));
                                    vacelular.setText("$ "+separador(obj3.getCelular()));
                                    vanana.setText("$ "+separador(obj3.getNana()));
                                    vautiles.setText("$ "+separador(obj3.getUtiles()));
                                    vaclases.setText("$ "+separador(obj3.getClases()));
                                    vajuguetes.setText("$ "+separador(obj3.getJuguetes()));
                                    vaotroshijos.setText("$ "+separador(obj3.getOtrosGastosHijos()));
                                    totalhijos.setText("$ "+separador(obj3.getTotalHijos()));

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Hijos.this,"error! ingresar valor menor o igual al actual!",Toast.LENGTH_LONG).show();
                        pescuela.setText("");
                        pdineroextra.setText("");
                        pcelular.setText("");
                        pnana.setText("");
                        putiles.setText("");
                        pclases.setText("");
                        pjuguetes.setText("");
                        potroshijos.setText("");
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
