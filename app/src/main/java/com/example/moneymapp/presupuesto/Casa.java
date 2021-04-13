package com.example.moneymapp.presupuesto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.presupuesto.model.Casas;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Casa extends AppCompatActivity {
    private FirebaseUser user;
    private TextView varenta, vamantenimiento, valimpieza, vadecoracion, vareparaciones,vajardineria,
            totalcasa, vaotros;
    private Button ingresarPresupuestoCasa,restarpresupuesto;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario, correo;
    private EditText renta, mantenimiento, limpieza, decoracion, reparaciones, jardineria, otros;
    private Casas nuevacasa;
    private int c,int_renta, int_mantenimiento, int_limpieza, int_decoracion, int_reparaciones, int_jardineria, int_otros,
    int_totalcasa, total_final_casa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casa);

        renta=(EditText)findViewById(R.id.edtRenta);
        mantenimiento=(EditText)findViewById(R.id.edtMnto);
        limpieza=(EditText)findViewById(R.id.edtLimpieza);
        decoracion =(EditText)findViewById(R.id.edtEstacionamiento);
        reparaciones=(EditText)findViewById(R.id.edtReparaciones);
        jardineria=(EditText)findViewById(R.id.edtJardineria);
        otros=(EditText)findViewById(R.id.edtOthers);
        ingresarPresupuestoCasa=(Button)findViewById(R.id.btnIngresarDatos);
        restarpresupuesto=(Button)findViewById(R.id.btnRestarCasa) ;
        totalcasa=(TextView)findViewById(R.id.txvTotalCasa);

        varenta=(TextView)findViewById(R.id.txvVarenta);
        vamantenimiento=(TextView)findViewById(R.id.txvVamnto);
        valimpieza=(TextView)findViewById(R.id.txvValimpieza);
        vadecoracion=(TextView)findViewById(R.id.txvVaestacionamiento);
        vajardineria=(TextView)findViewById(R.id.txvVajardineria);
        vaotros=(TextView)findViewById(R.id.txvVaothers);
        vareparaciones=(TextView)findViewById(R.id.txvVareparaciones);


        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Casas de la bbdd
       mDatabase = FirebaseDatabase.getInstance().getReference("Casas");
        //acceso a la tabla Presupuesto glabal de la bbdd
       tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");

       //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Casas obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Casas.class);
                    varenta.setText("$ "+separador(obj.getRenta()));
                    vamantenimiento.setText("$ "+separador(obj.getMantenimiento()));
                    valimpieza.setText("$ "+separador(obj.getLimpieza()));
                    vadecoracion.setText("$ "+separador(obj.getDecoracion()));
                    vareparaciones.setText("$ "+separador(obj.getReparaciones()));
                    vajardineria.setText("$ "+separador(obj.getJardineria()));
                    vaotros.setText("$ "+separador(obj.getOtros()));
                    totalcasa.setText("$ "+separador(obj.getTotalCasa()));
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

    //volver a presupuesto
    public void irMenuPrincipal(View v){
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
    public void ingresarPresupuestoCasa(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Casas obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Casas.class);
                    varenta.setText("$ "+separador(obj.getRenta()));
                    vamantenimiento.setText("$ "+separador(obj.getMantenimiento()));
                    valimpieza.setText("$ "+separador(obj.getLimpieza()));
                    vadecoracion.setText("$ "+separador(obj.getDecoracion()));
                    vareparaciones.setText("$ "+separador(obj.getReparaciones()));
                    vajardineria.setText("$ "+separador(obj.getJardineria()));
                    vaotros.setText("$ "+separador(obj.getOtros()));
                    totalcasa.setText("$ "+separador(obj.getTotalCasa()));

                    //cálculo del total del presupuesto Casas
                    int_renta=Integer.parseInt(obj.getRenta());
                    int_mantenimiento=Integer.parseInt(obj.getMantenimiento());
                    int_limpieza=Integer.parseInt(obj.getLimpieza());
                    int_decoracion=Integer.parseInt(obj.getDecoracion());
                    int_reparaciones=Integer.parseInt(obj.getReparaciones());
                    int_jardineria=Integer.parseInt(obj.getJardineria());
                    int_otros=Integer.parseInt(obj.getOtros());
                    int_totalcasa=Integer.parseInt(obj.getTotalCasa());

                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_casa=0;

                    if(!renta.getText().toString().isEmpty()){
                        int_renta=int_renta+Integer.parseInt(renta.getText().toString());
                        total_final_casa=total_final_casa+Integer.parseInt(renta.getText().toString());
                        mDatabase.child(clave).child("renta").setValue(String.valueOf(int_renta));
                        renta.setText("");
                    }
                    if(!mantenimiento.getText().toString().isEmpty()){
                        int_mantenimiento=int_mantenimiento+Integer.parseInt(mantenimiento.getText().toString());
                        total_final_casa=total_final_casa+Integer.parseInt(mantenimiento.getText().toString());
                        mDatabase.child(clave).child("mantenimiento").setValue(String.valueOf(int_mantenimiento));
                        mantenimiento.setText("");
                    }
                    if(!limpieza.getText().toString().isEmpty()){
                        int_limpieza=int_limpieza+Integer.parseInt(limpieza.getText().toString());
                        total_final_casa=total_final_casa+Integer.parseInt(limpieza.getText().toString());
                        mDatabase.child(clave).child("limpieza").setValue(String.valueOf(int_limpieza));
                        limpieza.setText("");
                    }
                    if(!decoracion.getText().toString().isEmpty()){
                        int_decoracion=int_decoracion+Integer.parseInt(decoracion.getText().toString());
                        total_final_casa=total_final_casa+Integer.parseInt(decoracion.getText().toString());
                        mDatabase.child(clave).child("decoracion").setValue(String.valueOf(int_decoracion));
                        decoracion.setText("");
                    }
                    if(!reparaciones.getText().toString().isEmpty()){
                        int_reparaciones=int_reparaciones+Integer.parseInt(reparaciones.getText().toString());
                        total_final_casa=total_final_casa+Integer.parseInt(reparaciones.getText().toString());
                        mDatabase.child(clave).child("reparaciones").setValue(String.valueOf(int_reparaciones));
                        reparaciones.setText("");
                    }
                    if(!jardineria.getText().toString().isEmpty()){
                        int_jardineria=int_jardineria+Integer.parseInt(jardineria.getText().toString());
                        total_final_casa=total_final_casa+Integer.parseInt(jardineria.getText().toString());
                        mDatabase.child(clave).child("jardineria").setValue(String.valueOf(int_jardineria));
                        jardineria.setText("");
                    }
                    if(!otros.getText().toString().isEmpty()){
                        int_otros=int_otros+Integer.parseInt(otros.getText().toString());
                        total_final_casa=total_final_casa+Integer.parseInt(otros.getText().toString());
                        mDatabase.child(clave).child("otros").setValue(String.valueOf(int_otros));
                        otros.setText("");
                    }
                    //ingreso del total final a la bbdd
                    sumarPresupuestoGlobal(total_final_casa);
                    total_final_casa=total_final_casa+int_totalcasa;
                    mDatabase.child(clave).child("tcasa").setValue(String.valueOf(total_final_casa));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Casas obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Casas.class);
                                varenta.setText("$ "+separador(obj3.getRenta()));
                                vamantenimiento.setText("$ "+separador(obj3.getMantenimiento()));
                                valimpieza.setText("$ "+separador(obj3.getLimpieza()));
                                vadecoracion.setText("$ "+separador(obj3.getDecoracion()));
                                vareparaciones.setText("$ "+separador(obj3.getReparaciones()));
                                vajardineria.setText("$ "+separador(obj3.getJardineria()));
                                vaotros.setText("$ "+separador(obj3.getOtros()));
                                totalcasa.setText("$ "+separador(obj3.getTotalCasa()));

                            }
                            //limpiar campos input
                            renta.setText("");
                            mantenimiento.setText("");
                            limpieza.setText("");
                            decoracion.setText("");
                            reparaciones.setText("");
                            jardineria.setText("");
                            otros.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if(c!=0){
                    //actualización del presupuesto del usuario
                    int v=0;


                }else{
                    //creo la casa
                    nuevacasa= new Casas();
                    nuevacasa.setCorreo(usuario);

                    if (renta.getText().toString().isEmpty()){
                        nuevacasa.setRenta("0");
                    }else {
                        nuevacasa.setRenta(renta.getText().toString());
                    }

                    if (mantenimiento.getText().toString().isEmpty()){
                        nuevacasa.setMantenimiento("0");
                    }else {
                        nuevacasa.setMantenimiento(mantenimiento.getText().toString());
                    }

                    if (limpieza.getText().toString().isEmpty()){
                        nuevacasa.setLimpieza("0");
                    }else {
                        nuevacasa.setLimpieza(limpieza.getText().toString());
                    }

                    if (decoracion.getText().toString().isEmpty()){
                        nuevacasa.setDecoracion("0");
                    }else {
                        nuevacasa.setDecoracion(decoracion.getText().toString());
                    }

                    if (reparaciones.getText().toString().isEmpty()){
                        nuevacasa.setReparaciones("0");
                    }else {
                        nuevacasa.setReparaciones(reparaciones.getText().toString());
                    }

                    if (jardineria.getText().toString().isEmpty()){
                        nuevacasa.setJardineria("0");
                    }else {
                        nuevacasa.setJardineria(jardineria.getText().toString());
                    }

                    if (otros.getText().toString().isEmpty()){
                        nuevacasa.setOtros("0");
                    }else {
                        nuevacasa.setOtros(otros.getText().toString());
                    }


                    //suma del total inicial
                    int_renta=Integer.parseInt(nuevacasa.getRenta());
                    int_mantenimiento=Integer.parseInt(nuevacasa.getMantenimiento());
                    int_limpieza=Integer.parseInt(nuevacasa.getLimpieza());
                    int_decoracion=Integer.parseInt(nuevacasa.getDecoracion());
                    int_reparaciones=Integer.parseInt(nuevacasa.getReparaciones());
                    int_jardineria=Integer.parseInt(nuevacasa.getJardineria());
                    int_otros=Integer.parseInt(nuevacasa.getOtros());


                    total_final_casa=0;
                    total_final_casa=int_totalcasa+int_renta+int_mantenimiento+int_limpieza+int_decoracion+
                            int_reparaciones+int_jardineria+ int_otros;

                    nuevacasa.setTotalCasa(String.valueOf(total_final_casa));
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_casa);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevacasa);

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Casas obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Casas.class);
                                varenta.setText("$ "+separador(obj4.getRenta()));
                                vamantenimiento.setText("$ "+separador(obj4.getMantenimiento()));
                                valimpieza.setText("$ "+separador(obj4.getLimpieza()));
                                vadecoracion.setText("$ "+separador(obj4.getDecoracion()));
                                vareparaciones.setText("$ "+separador(obj4.getReparaciones()));
                                vajardineria.setText("$ "+separador(obj4.getJardineria()));
                                vaotros.setText("$ "+separador(obj4.getOtros()));
                                totalcasa.setText("$ "+separador(obj4.getTotalCasa()));


                            }
                            //limpiar campos input
                            renta.setText("");
                            mantenimiento.setText("");
                            limpieza.setText("");
                            decoracion.setText("");
                            reparaciones.setText("");
                            jardineria.setText("");
                            otros.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    renta.setText("");
                    mantenimiento.setText("");
                    limpieza.setText("");
                    decoracion.setText("");
                    reparaciones.setText("");
                    jardineria.setText("");
                    otros.setText("");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //método para sumar presupusto al presupuesto global
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

    //método para actualizar datos presupuesto casa
    public void actualizarPresupuestoCasa(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Casas obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Casas.class);

                    //cálculo del total del presupuesto Casas
                    int_renta=Integer.parseInt(obj.getRenta());
                    int_mantenimiento=Integer.parseInt(obj.getMantenimiento());
                    int_limpieza=Integer.parseInt(obj.getLimpieza());
                    int_decoracion=Integer.parseInt(obj.getDecoracion());
                    int_reparaciones=Integer.parseInt(obj.getReparaciones());
                    int_jardineria=Integer.parseInt(obj.getJardineria());
                    int_otros=Integer.parseInt(obj.getOtros());
                    int_totalcasa=Integer.parseInt(obj.getTotalCasa());




                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para actualizar el presupuesto global
                    if (!renta.getText().toString().isEmpty()){
                        int_renta=int_renta-Integer.parseInt(renta.getText().toString());
                        contador=contador+Integer.parseInt(renta.getText().toString());
                        if (int_renta<0){
                            valida=false;
                        }
                    }

                    if (!mantenimiento.getText().toString().isEmpty()){
                        int_mantenimiento=int_mantenimiento-Integer.parseInt(mantenimiento.getText().toString());
                        contador=contador+Integer.parseInt(mantenimiento.getText().toString());
                        if (int_mantenimiento<0){
                            valida=false;
                        }
                    }

                    if (!limpieza.getText().toString().isEmpty()){
                        int_limpieza=int_limpieza-Integer.parseInt(limpieza.getText().toString());
                        contador=contador+Integer.parseInt(limpieza.getText().toString());
                        if (int_limpieza<0){
                            valida=false;
                        }
                    }


                    if (!decoracion.getText().toString().isEmpty()){
                        int_decoracion=int_decoracion-Integer.parseInt(decoracion.getText().toString());
                        contador=contador+Integer.parseInt(decoracion.getText().toString());
                        if (int_decoracion<0){
                            valida=false;
                        }
                    }

                    if (!reparaciones.getText().toString().isEmpty()){
                        int_reparaciones=int_reparaciones-Integer.parseInt(reparaciones.getText().toString());
                        contador=contador+Integer.parseInt(reparaciones.getText().toString());
                        if (int_reparaciones<0){
                            valida=false;
                        }
                    }

                    if (!jardineria.getText().toString().isEmpty()){
                        int_jardineria=int_jardineria-Integer.parseInt(jardineria.getText().toString());
                        contador=contador+Integer.parseInt(jardineria.getText().toString());
                        if (int_jardineria<0){
                            valida=false;
                        }
                    }

                    if (!otros.getText().toString().isEmpty()){
                        int_otros=int_otros-Integer.parseInt(otros.getText().toString());
                        contador=contador+Integer.parseInt(otros.getText().toString());
                        if (int_otros<0){
                            valida=false;
                        }
                    }

                    //validación de campos llenos
                    if (renta.getText().toString().isEmpty() && mantenimiento.getText().toString().isEmpty()
                    && limpieza.getText().toString().isEmpty() && decoracion.getText().toString().isEmpty()
                    && reparaciones.getText().toString().isEmpty() && jardineria.getText().toString().isEmpty()
                    && otros.getText().toString().isEmpty()){
                        valida2=false;
                    }


                    //cálculo del total del presupuesto Casas
                    if (valida2==false){
                        Toast.makeText(Casa.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){


                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarPresupuestoGlobal(contador);//método para restar el presupuesto global
                        if(!renta.getText().toString().isEmpty()){
                            int_totalcasa=int_totalcasa-Integer.parseInt(renta.getText().toString());
                            mDatabase.child(clave).child("renta").setValue(String.valueOf(int_renta));
                            mDatabase.child(clave).child("tcasa").setValue(String.valueOf(int_totalcasa));
                            renta.setText("");
                        }

                        if(!mantenimiento.getText().toString().isEmpty()){
                            int_totalcasa=int_totalcasa-Integer.parseInt(mantenimiento.getText().toString());
                            mDatabase.child(clave).child("mantenimiento").setValue(String.valueOf(int_mantenimiento));
                            mDatabase.child(clave).child("tcasa").setValue(String.valueOf(int_totalcasa));
                            mantenimiento.setText("");
                        }

                        if(!limpieza.getText().toString().isEmpty()){
                            int_totalcasa=int_totalcasa-Integer.parseInt(limpieza.getText().toString());
                            mDatabase.child(clave).child("limpieza").setValue(String.valueOf(int_limpieza));
                            mDatabase.child(clave).child("tcasa").setValue(String.valueOf(int_totalcasa));
                            limpieza.setText("");
                        }

                        if(!decoracion.getText().toString().isEmpty()){
                            int_totalcasa=int_totalcasa-Integer.parseInt(decoracion.getText().toString());
                            mDatabase.child(clave).child("decoracion").setValue(String.valueOf(int_decoracion));
                            mDatabase.child(clave).child("tcasa").setValue(String.valueOf(int_totalcasa));
                            decoracion.setText("");
                        }

                        if(!reparaciones.getText().toString().isEmpty()){
                            int_totalcasa=int_totalcasa-Integer.parseInt(reparaciones.getText().toString());
                            mDatabase.child(clave).child("reparaciones").setValue(String.valueOf(int_reparaciones));
                            mDatabase.child(clave).child("tcasa").setValue(String.valueOf(int_totalcasa));
                            reparaciones.setText("");
                        }

                        if(!jardineria.getText().toString().isEmpty()){
                            int_totalcasa=int_totalcasa-Integer.parseInt(jardineria.getText().toString());
                            mDatabase.child(clave).child("jardineria").setValue(String.valueOf(int_jardineria));
                            mDatabase.child(clave).child("tcasa").setValue(String.valueOf(int_totalcasa));
                            jardineria.setText("");
                        }

                        if(!otros.getText().toString().isEmpty()){
                            int_totalcasa=int_totalcasa-Integer.parseInt(otros.getText().toString());
                            mDatabase.child(clave).child("otros").setValue(String.valueOf(int_otros));
                            mDatabase.child(clave).child("tcasa").setValue(String.valueOf(int_totalcasa));
                            otros.setText("");
                        }

                        //else if para seleccion de que servicio modificar
                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Casas obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Casas.class);
                                    varenta.setText("$ "+separador(obj3.getRenta()));
                                    vamantenimiento.setText("$ "+separador(obj3.getMantenimiento()));
                                    valimpieza.setText("$ "+separador(obj3.getLimpieza()));
                                    vadecoracion.setText("$ "+separador(obj3.getDecoracion()));
                                    vareparaciones.setText("$ "+separador(obj3.getReparaciones()));
                                    vajardineria.setText("$ "+separador(obj3.getJardineria()));
                                    vaotros.setText("$ "+separador(obj3.getOtros()));
                                    totalcasa.setText("$ "+separador(obj3.getTotalCasa()));

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Casa.this,"error! ingresar valor menor o igual al actual!",Toast.LENGTH_LONG).show();
                        renta.setText("");
                        mantenimiento.setText("");
                        limpieza.setText("");
                        decoracion.setText("");
                        reparaciones.setText("");
                        jardineria.setText("");
                        otros.setText("");
                    }

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

}
