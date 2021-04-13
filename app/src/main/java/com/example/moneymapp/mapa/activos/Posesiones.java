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
import com.example.moneymapp.mapa.activos.model.Posesion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Posesiones extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vavivienda, vaautomovil,vamuebles, vaarte, vajoyas,vaseguros,vaotrosposesiones,totalposesiones;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText vivienda, automovil,muebles, arte, joyas, seguros, otrosposesiones;
    private Posesion nuevaposesion;
    private int c,int_vivienda, int_automovil, int_muebles,int_arte, int_joyas, int_seguros, int_otrosposesiones,
            int_totalposesiones, total_final_posesiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posesiones);

        vivienda=(EditText)findViewById(R.id.edtPvivienda);
        automovil=(EditText)findViewById(R.id.edtPautomovil);
        muebles=(EditText)findViewById(R.id.edtPmuebleelectro);
        arte=(EditText)findViewById(R.id.edtParte);
        joyas=(EditText)findViewById(R.id.edtPjoyas);
        seguros=(EditText)findViewById(R.id.edtPseguros);
        otrosposesiones=(EditText)findViewById(R.id.edtPotrosposesiones);
        totalposesiones=(TextView)findViewById(R.id.txvTotalposesiones);


        vavivienda=(TextView)findViewById(R.id.txvVPvivienda);
        vaautomovil=(TextView)findViewById(R.id.txvVPautomovil);
        vamuebles=(TextView)findViewById(R.id.txvVPmuebleselectro);
        vaarte=(TextView)findViewById(R.id.txvVParte);
        vajoyas=(TextView)findViewById(R.id.txvVPjoyas);
        vaseguros=(TextView)findViewById(R.id.txvVPseguros);
        vaotrosposesiones=(TextView)findViewById(R.id.txvVPotrosposesiones);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Casas de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("posesiones");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("activos");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Posesion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Posesion.class);
                    vavivienda.setText("$ "+separador(obj.getVivienda()));
                    vaautomovil.setText("$ "+separador(obj.getAutomovil()));
                    vamuebles.setText("$ "+separador(obj.getMuebles()));
                    vaarte.setText("$ "+separador(obj.getArte()));
                    vajoyas.setText("$ "+separador(obj.getJoyas()));
                    vaseguros.setText("$ "+separador(obj.getSeguros()));
                    vaotrosposesiones.setText("$ "+separador(obj.getOtrasPosesiones()));
                    totalposesiones.setText("$ "+separador(obj.getTotalPosesiones()));
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
    public void ingresarTotalPosesiones(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Posesion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Posesion.class);
                    vavivienda.setText("$ "+separador(obj.getVivienda()));
                    vaautomovil.setText("$ "+separador(obj.getAutomovil()));
                    vamuebles.setText("$ "+separador(obj.getMuebles()));
                    vaarte.setText("$ "+separador(obj.getArte()));
                    vajoyas.setText("$ "+separador(obj.getJoyas()));
                    vaseguros.setText("$ "+separador(obj.getSeguros()));
                    vaotrosposesiones.setText("$ "+separador(obj.getOtrasPosesiones()));
                    totalposesiones.setText("$ "+separador(obj.getTotalPosesiones()));

                    //cálculo del total del presupuesto Casas
                    int_vivienda=Integer.parseInt(obj.getVivienda());
                    int_automovil=Integer.parseInt(obj.getAutomovil());
                    int_muebles=Integer.parseInt(obj.getMuebles());
                    int_arte=Integer.parseInt(obj.getArte());
                    int_joyas=Integer.parseInt(obj.getJoyas());
                    int_seguros=Integer.parseInt(obj.getSeguros());
                    int_otrosposesiones=Integer.parseInt(obj.getOtrasPosesiones());
                    int_totalposesiones=Integer.parseInt(obj.getTotalPosesiones());

                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_posesiones=0;

                    if(!vivienda.getText().toString().isEmpty()){
                        int_vivienda=int_vivienda+Integer.parseInt(vivienda.getText().toString());
                        total_final_posesiones=total_final_posesiones+Integer.parseInt(vivienda.getText().toString());
                        mDatabase.child(clave).child("vivienda").setValue(String.valueOf(int_vivienda));
                        vivienda.setText("");
                    }
                    if(!automovil.getText().toString().isEmpty()){
                        int_automovil=int_automovil+Integer.parseInt(automovil.getText().toString());
                        total_final_posesiones=total_final_posesiones+Integer.parseInt(automovil.getText().toString());
                        mDatabase.child(clave).child("automovil").setValue(String.valueOf(int_automovil));
                        automovil.setText("");
                    }
                    if(!muebles.getText().toString().isEmpty()){
                        int_muebles=int_muebles+Integer.parseInt(muebles.getText().toString());
                        total_final_posesiones=total_final_posesiones+Integer.parseInt(muebles.getText().toString());
                        mDatabase.child(clave).child("muebles").setValue(String.valueOf(int_muebles));
                        muebles.setText("");
                    }
                    if(!arte.getText().toString().isEmpty()){
                        int_arte=int_arte+Integer.parseInt(arte.getText().toString());
                        total_final_posesiones=total_final_posesiones+Integer.parseInt(arte.getText().toString());
                        mDatabase.child(clave).child("arte").setValue(String.valueOf(int_arte));
                        arte.setText("");
                    }
                    if(!joyas.getText().toString().isEmpty()){
                        int_joyas=int_joyas+Integer.parseInt(joyas.getText().toString());
                        total_final_posesiones=total_final_posesiones+Integer.parseInt(joyas.getText().toString());
                        mDatabase.child(clave).child("joyas").setValue(String.valueOf(int_joyas));
                        joyas.setText("");
                    }
                    if(!seguros.getText().toString().isEmpty()){
                        int_seguros=int_seguros+Integer.parseInt(seguros.getText().toString());
                        total_final_posesiones=total_final_posesiones+Integer.parseInt(seguros.getText().toString());
                        mDatabase.child(clave).child("seguros").setValue(String.valueOf(int_seguros));
                        seguros.setText("");
                    }
                    if(!otrosposesiones.getText().toString().isEmpty()){
                        int_otrosposesiones=int_otrosposesiones+Integer.parseInt(otrosposesiones.getText().toString());
                        total_final_posesiones=total_final_posesiones+Integer.parseInt(otrosposesiones.getText().toString());
                        mDatabase.child(clave).child("otrasPosesiones").setValue(String.valueOf(int_otrosposesiones));
                        otrosposesiones.setText("");
                    }
                    //ingreso del total final a la bbdd
                    sumarTotalActivos(total_final_posesiones);
                    total_final_posesiones=total_final_posesiones+int_totalposesiones;
                    mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(total_final_posesiones));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Posesion obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Posesion.class);
                                vavivienda.setText("$ "+separador(obj3.getVivienda()));
                                vaautomovil.setText("$ "+separador(obj3.getAutomovil()));
                                vamuebles.setText("$ "+separador(obj3.getMuebles()));
                                vaarte.setText("$ "+separador(obj3.getArte()));
                                vajoyas.setText("$ "+separador(obj3.getJoyas()));
                                vaseguros.setText("$ "+separador(obj3.getSeguros()));
                                vaotrosposesiones.setText("$ "+separador(obj3.getOtrasPosesiones()));
                                totalposesiones.setText("$ "+separador(obj3.getTotalPosesiones()));

                            }
                            //limpiar campos input
                            vivienda.setText("");
                            automovil.setText("");
                            muebles.setText("");
                            arte.setText("");
                            joyas.setText("");
                            seguros.setText("");
                            otrosposesiones.setText("");

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
                    nuevaposesion= new Posesion();
                    nuevaposesion.setCorreo(usuario);

                    if (vivienda.getText().toString().isEmpty()){
                        nuevaposesion.setVivienda("0");
                    }else {
                        nuevaposesion.setVivienda(vivienda.getText().toString());
                    }

                    if (automovil.getText().toString().isEmpty()){
                        nuevaposesion.setAutomovil("0");
                    }else {
                        nuevaposesion.setAutomovil(automovil.getText().toString());
                    }

                    if (muebles.getText().toString().isEmpty()){
                        nuevaposesion.setMuebles("0");
                    }else {
                        nuevaposesion.setMuebles(muebles.getText().toString());
                    }

                    if (arte.getText().toString().isEmpty()){
                        nuevaposesion.setArte("0");
                    }else {
                        nuevaposesion.setArte(arte.getText().toString());
                    }

                    if (joyas.getText().toString().isEmpty()){
                        nuevaposesion.setJoyas("0");
                    }else {
                        nuevaposesion.setJoyas(joyas.getText().toString());
                    }

                    if (seguros.getText().toString().isEmpty()){
                        nuevaposesion.setSeguros("0");
                    }else {
                        nuevaposesion.setSeguros(seguros.getText().toString());
                    }

                    if (otrosposesiones.getText().toString().isEmpty()){
                        nuevaposesion.setOtrasPosesiones("0");
                    }else {
                        nuevaposesion.setOtrasPosesiones(otrosposesiones.getText().toString());
                    }


                    //suma del total inicial
                    int_vivienda=Integer.parseInt(nuevaposesion.getVivienda());
                    int_automovil=Integer.parseInt(nuevaposesion.getAutomovil());
                    int_muebles=Integer.parseInt(nuevaposesion.getMuebles());
                    int_arte=Integer.parseInt(nuevaposesion.getArte());
                   int_joyas=Integer.parseInt(nuevaposesion.getJoyas());
                   int_seguros=Integer.parseInt(nuevaposesion.getSeguros());
                    int_otrosposesiones=Integer.parseInt(nuevaposesion.getOtrasPosesiones());


                    total_final_posesiones=0;
                    total_final_posesiones=int_totalposesiones+int_vivienda+int_automovil+int_muebles+int_arte+
                            int_joyas+int_seguros+ int_otrosposesiones;

                    nuevaposesion.setTotalPosesiones(String.valueOf(total_final_posesiones));
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalActivos(total_final_posesiones);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevaposesion);

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Posesion obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Posesion.class);
                                vavivienda.setText("$ "+separador(obj4.getVivienda()));
                                vaautomovil.setText("$ "+separador(obj4.getAutomovil()));
                               vamuebles.setText("$ "+separador(obj4.getMuebles()));
                                vaarte.setText("$ "+separador(obj4.getArte()));
                                vajoyas.setText("$ "+separador(obj4.getJoyas()));
                                vaseguros.setText("$ "+separador(obj4.getSeguros()));
                                vaotrosposesiones.setText("$ "+separador(obj4.getOtrasPosesiones()));
                                totalposesiones.setText("$ "+separador(obj4.getTotalPosesiones()));


                            }
                            //limpiar campos input
                            vivienda.setText("");
                            automovil.setText("");
                            muebles.setText("");
                            arte.setText("");
                            joyas.setText("");
                            seguros.setText("");
                            otrosposesiones.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    vivienda.setText("");
                    automovil.setText("");
                    muebles.setText("");
                    arte.setText("");
                    joyas.setText("");
                    seguros.setText("");
                    otrosposesiones.setText("");
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
    public void actualizarTotalPosesiones(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Posesion obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Posesion.class);

                    //cálculo del total del total inversiones
                    int_vivienda=Integer.parseInt(obj.getVivienda());
                    int_automovil=Integer.parseInt(obj.getAutomovil());
                    int_muebles=Integer.parseInt(obj.getMuebles());
                    int_arte=Integer.parseInt(obj.getArte());
                    int_joyas=Integer.parseInt(obj.getJoyas());
                    int_seguros=Integer.parseInt(obj.getSeguros());
                    int_otrosposesiones=Integer.parseInt(obj.getOtrasPosesiones());
                    int_totalposesiones=Integer.parseInt(obj.getTotalPosesiones());




                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para actualizar el presupuesto global
                    if (!vivienda.getText().toString().isEmpty()){
                        int_vivienda=int_vivienda-Integer.parseInt(vivienda.getText().toString());
                        contador=contador+Integer.parseInt(vivienda.getText().toString());
                        if (int_vivienda<0){
                            valida=false;
                        }
                    }

                    if (!automovil.getText().toString().isEmpty()){
                        int_automovil=int_automovil-Integer.parseInt(automovil.getText().toString());
                        contador=contador+Integer.parseInt(automovil.getText().toString());
                        if (int_automovil<0){
                            valida=false;
                        }
                    }

                    if (!muebles.getText().toString().isEmpty()){
                        int_muebles=int_muebles-Integer.parseInt(muebles.getText().toString());
                        contador=contador+Integer.parseInt(muebles.getText().toString());
                        if (int_muebles<0){
                            valida=false;
                        }
                    }


                    if (!arte.getText().toString().isEmpty()){
                        int_arte=int_arte-Integer.parseInt(arte.getText().toString());
                        contador=contador+Integer.parseInt(arte.getText().toString());
                        if (int_arte<0){
                            valida=false;
                        }
                    }

                    if (!joyas.getText().toString().isEmpty()){
                        int_joyas=int_joyas-Integer.parseInt(joyas.getText().toString());
                        contador=contador+Integer.parseInt(joyas.getText().toString());
                        if (int_joyas<0){
                            valida=false;
                        }
                    }

                    if (!seguros.getText().toString().isEmpty()){
                        int_seguros=int_seguros-Integer.parseInt(seguros.getText().toString());
                        contador=contador+Integer.parseInt(seguros.getText().toString());
                        if (int_seguros<0){
                            valida=false;
                        }
                    }

                    if (!otrosposesiones.getText().toString().isEmpty()){
                        int_otrosposesiones=int_otrosposesiones-Integer.parseInt(otrosposesiones.getText().toString());
                        contador=contador+Integer.parseInt(otrosposesiones.getText().toString());
                        if (int_otrosposesiones<0){
                            valida=false;
                        }
                    }

                    //validación de campos llenos
                    if (vivienda.getText().toString().isEmpty() && automovil.getText().toString().isEmpty()
                            && muebles.getText().toString().isEmpty() && arte.getText().toString().isEmpty()
                            && joyas.getText().toString().isEmpty() && seguros.getText().toString().isEmpty()
                            && otrosposesiones.getText().toString().isEmpty()){
                        valida2=false;
                    }


                    //cálculo del total del presupuesto Casas
                    if (valida2==false){
                        Toast.makeText(Posesiones.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){


                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarTotalActivos(contador);//método para restar el presupuesto global
                        if(!vivienda.getText().toString().isEmpty()){
                            int_totalposesiones=int_totalposesiones-Integer.parseInt(vivienda.getText().toString());
                            mDatabase.child(clave).child("vivienda").setValue(String.valueOf(int_vivienda));
                            mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(int_totalposesiones));
                            vivienda.setText("");
                        }

                        if(!automovil.getText().toString().isEmpty()){
                            int_totalposesiones=int_totalposesiones-Integer.parseInt(automovil.getText().toString());
                            mDatabase.child(clave).child("automovil").setValue(String.valueOf(int_automovil));
                            mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(int_totalposesiones));
                            automovil.setText("");
                        }

                        if(!muebles.getText().toString().isEmpty()){
                            int_totalposesiones=int_totalposesiones-Integer.parseInt(muebles.getText().toString());
                            mDatabase.child(clave).child("muebles").setValue(String.valueOf(int_muebles));
                            mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(int_totalposesiones));
                            muebles.setText("");
                        }

                        if(!arte.getText().toString().isEmpty()){
                            int_totalposesiones=int_totalposesiones-Integer.parseInt(arte.getText().toString());
                            mDatabase.child(clave).child("arte").setValue(String.valueOf(int_arte));
                            mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(int_totalposesiones));
                            arte.setText("");
                        }

                        if(!joyas.getText().toString().isEmpty()){
                            int_totalposesiones=int_totalposesiones-Integer.parseInt(joyas.getText().toString());
                            mDatabase.child(clave).child("joyas").setValue(String.valueOf(int_joyas));
                            mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(int_totalposesiones));
                            joyas.setText("");
                        }

                        if(!seguros.getText().toString().isEmpty()){
                            int_totalposesiones=int_totalposesiones-Integer.parseInt(seguros.getText().toString());
                            mDatabase.child(clave).child("seguros").setValue(String.valueOf(int_seguros));
                            mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(int_totalposesiones));
                            seguros.setText("");
                        }

                        if(!otrosposesiones.getText().toString().isEmpty()){
                            int_totalposesiones=int_totalposesiones-Integer.parseInt(otrosposesiones.getText().toString());
                            mDatabase.child(clave).child("otrasPosesiones").setValue(String.valueOf(int_otrosposesiones));
                            mDatabase.child(clave).child("totalPosesiones").setValue(String.valueOf(int_totalposesiones));
                            otrosposesiones.setText("");
                        }

                        //else if para seleccion de que servicio modificar
                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Posesion obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Posesion.class);
                                    vavivienda.setText("$ "+separador(obj3.getVivienda()));
                                    vaautomovil.setText("$ "+separador(obj3.getAutomovil()));
                                    vamuebles.setText("$ "+separador(obj3.getMuebles()));
                                    vaarte.setText("$ "+separador(obj3.getArte()));
                                    vajoyas.setText("$ "+separador(obj3.getJoyas()));
                                    vaseguros.setText("$ "+separador(obj3.getSeguros()));
                                    vaotrosposesiones.setText("$ "+separador(obj3.getOtrasPosesiones()));
                                    totalposesiones.setText("$ "+separador(obj3.getTotalPosesiones()));

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Posesiones.this,"error! ingresar valor menor o igual al actual!",Toast.LENGTH_LONG).show();

                        //limpiar campos input
                        vivienda.setText("");
                        automovil.setText("");
                        muebles.setText("");
                        arte.setText("");
                        joyas.setText("");
                        seguros.setText("");
                        otrosposesiones.setText("");
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
