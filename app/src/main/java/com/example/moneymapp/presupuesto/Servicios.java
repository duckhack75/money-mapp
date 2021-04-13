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
import com.example.moneymapp.presupuesto.model.Servicio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Servicios extends AppCompatActivity {

    private FirebaseUser user;
    private TextView vagas, valuz, vaagua, vatelefono, vainternet,vacable, valavanderia, vaotrosservicios, totalservicios;
    private Button ingresarPresupuestoServicios, restarPresupuestoServicios;
    private Boolean comprobar;
    private DatabaseReference mDatabase,tDatabase;
    private String usuario, correo;
    private EditText gas, luz, agua, telefono, internet, cable, lavanderia, otrosservicios;
    private Servicio nuevoservicio;
    private int c,int_gas, int_luz, int_agua, int_telefono, int_internet, int_cable, int_lavanderia, int_otrosservicios,
            int_totalservicios, total_final_servicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        gas=(EditText)findViewById(R.id.edtGas);
        luz=(EditText)findViewById(R.id.edtLuz);
        agua=(EditText)findViewById(R.id.edtAgua);
        telefono=(EditText)findViewById(R.id.edtTelefono);
        internet=(EditText)findViewById(R.id.edtInternet);
        cable=(EditText)findViewById(R.id.edtCable);
        lavanderia=(EditText)findViewById(R.id.edtLavanderia);
        otrosservicios=(EditText)findViewById(R.id.edtOthersservicios);
        ingresarPresupuestoServicios=(Button)findViewById(R.id.btnIngresarDatosServicios);
        ingresarPresupuestoServicios=(Button)findViewById(R.id.btnRestarPresupuestoServicios);

        vagas=(TextView)findViewById(R.id.txvVagas);
        valuz=(TextView)findViewById(R.id.txvValuz);
        vaagua=(TextView)findViewById(R.id.txvVaagua);
        vatelefono=(TextView)findViewById(R.id.txvVatelefono);
        vainternet=(TextView)findViewById(R.id.txvVainternet);
        vacable=(TextView)findViewById(R.id.txvVacable);
        valavanderia=(TextView)findViewById(R.id.txvValavanderia);
        vaotrosservicios=(TextView)findViewById(R.id.txvVaotrosservicios);
        totalservicios=(TextView)findViewById(R.id.txvTotalServicios);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Casas de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Servicios");

        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Servicio obj=new Servicio();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Servicio.class);
                    vagas.setText("$ "+separador(obj.getGas()));
                    valuz.setText("$ "+separador(obj.getLuz()));
                    vaagua.setText("$ "+separador(obj.getAgua()));
                    vatelefono.setText("$ "+separador(obj.getTelefono()));
                    vainternet.setText("$ "+separador(obj.getInternet()));
                    vacable.setText("$ "+separador(obj.getCable()));
                    valavanderia.setText("$ "+separador(obj.getLavanderia()));
                    vaotrosservicios.setText("$ "+separador(obj.getOtrosServicios()));
                    totalservicios.setText("$ "+separador(obj.getTotalServicios()));
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


    //método para ingresar presupuesto servicios
    public void ingresarPresupuestoServicios(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Servicio obj=new Servicio();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros,
                    // solo si el usuario ya tiene info ingresada
                    obj=datanapshot.getValue(Servicio.class);
                    vagas.setText("$ "+separador(obj.getGas()));
                    valuz.setText("$ "+separador(obj.getLuz()));
                    vaagua.setText("$ "+separador(obj.getAgua()));
                    vatelefono.setText("$ "+separador(obj.getTelefono()));
                    vainternet.setText("$ "+separador(obj.getInternet()));
                    vacable.setText("$ "+separador(obj.getCable()));
                    valavanderia.setText("$ "+separador(obj.getLavanderia()));
                    vaotrosservicios.setText("$ "+separador(obj.getOtrosServicios()));
                    totalservicios.setText("$ "+separador(obj.getTotalServicios()));

                    //cálculo del total del presupuesto Casas
                    int_gas=Integer.parseInt(obj.getGas());
                    int_luz=Integer.parseInt(obj.getLuz());
                    int_agua=Integer.parseInt(obj.getAgua());
                    int_telefono=Integer.parseInt(obj.getTelefono());
                    int_internet=Integer.parseInt(obj.getInternet());
                    int_cable=Integer.parseInt(obj.getCable());
                    int_lavanderia=Integer.parseInt(obj.getLavanderia());
                    int_otrosservicios=Integer.parseInt(obj.getOtrosServicios());
                    int_totalservicios=Integer.parseInt(obj.getTotalServicios());

                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_servicios=0;
                    if(!gas.getText().toString().isEmpty()){
                        int_gas=int_gas+Integer.parseInt(gas.getText().toString());
                        total_final_servicios=total_final_servicios+Integer.parseInt(gas.getText().toString());
                        mDatabase.child(clave).child("gas").setValue(String.valueOf(int_gas));
                        gas.setText("");
                    }
                    if(!luz.getText().toString().isEmpty()){
                        int_luz=int_luz+Integer.parseInt(luz.getText().toString());
                        total_final_servicios=total_final_servicios+Integer.parseInt(luz.getText().toString());
                        mDatabase.child(clave).child("luz").setValue(String.valueOf(int_luz));
                        luz.setText("");
                    }
                    if(!agua.getText().toString().isEmpty()){
                        int_agua=int_agua+Integer.parseInt(agua.getText().toString());
                        total_final_servicios=total_final_servicios+Integer.parseInt(agua.getText().toString());
                        mDatabase.child(clave).child("agua").setValue(String.valueOf(int_agua));
                        agua.setText("");
                    }
                    if(!telefono.getText().toString().isEmpty()){
                        int_telefono=int_telefono+Integer.parseInt(telefono.getText().toString());
                       total_final_servicios=total_final_servicios+Integer.parseInt(telefono.getText().toString());
                        mDatabase.child(clave).child("telefono").setValue(String.valueOf(int_telefono));
                        telefono.setText("");
                    }
                    if(!internet.getText().toString().isEmpty()){
                        int_internet=int_internet+Integer.parseInt(internet.getText().toString());
                        total_final_servicios=total_final_servicios+Integer.parseInt(internet.getText().toString());
                        mDatabase.child(clave).child("internet").setValue(String.valueOf(int_internet));
                        internet.setText("");
                    }
                    if(!cable.getText().toString().isEmpty()){
                        int_cable=int_cable+Integer.parseInt(cable.getText().toString());
                        total_final_servicios=total_final_servicios+Integer.parseInt(cable.getText().toString());
                        mDatabase.child(clave).child("cable").setValue(String.valueOf(int_cable));
                        cable.setText("");
                    }
                    if(!lavanderia.getText().toString().isEmpty()){
                        int_lavanderia=int_lavanderia+Integer.parseInt(lavanderia.getText().toString());
                        total_final_servicios=total_final_servicios+Integer.parseInt(lavanderia.getText().toString());
                        mDatabase.child(clave).child("lavanderia").setValue(String.valueOf(int_lavanderia));
                        lavanderia.setText("");
                    }
                    if(!otrosservicios.getText().toString().isEmpty()){
                        int_otrosservicios=int_otrosservicios+Integer.parseInt(otrosservicios.getText().toString());
                        total_final_servicios=total_final_servicios+Integer.parseInt(otrosservicios.getText().toString());
                        mDatabase.child(clave).child("otrosservicios").setValue(String.valueOf(int_otrosservicios));
                        otrosservicios.setText("");
                    }

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_servicios);
                    total_final_servicios=total_final_servicios+int_totalservicios;
                    //se suman todos los servicios y se ingresan al total
                   /* total_final_servicios=0;
                    total_final_servicios=int_totalservicios+int_gas+int_luz+int_agua+int_telefono+int_internet+int_cable+
                                        int_lavanderia+int_otrosservicios;*/
                    mDatabase.child(clave).child("tservicios").setValue(String.valueOf(total_final_servicios));



                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Servicio obj3=new Servicio();
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Servicio.class);
                                vagas.setText("$ "+separador(obj3.getGas()));
                                valuz.setText("$ "+separador(obj3.getLuz()));
                                vaagua.setText("$ "+separador(obj3.getAgua()));
                                vatelefono.setText("$ "+separador(obj3.getTelefono()));
                                vainternet.setText("$ "+separador(obj3.getInternet()));
                                vacable.setText("$ "+separador(obj3.getCable()));
                                valavanderia.setText("$ "+separador(obj3.getLavanderia()));
                                vaotrosservicios.setText("$ "+separador(obj3.getOtrosServicios()));
                                totalservicios.setText("$ "+separador(obj3.getTotalServicios()));


                            }
                            //limpiar campos input
                            gas.setText("");
                            luz.setText("");
                            agua.setText("");
                            telefono.setText("");
                            internet.setText("");
                            cable.setText("");
                            lavanderia.setText("");
                            otrosservicios.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if(c!=0){
                    //actualización del presupuesto del usuario


                }else{
                    //creo servicios
                    nuevoservicio= new Servicio();
                    nuevoservicio.setCorreo(usuario);
                    if (gas.getText().toString().isEmpty()){
                        nuevoservicio.setGas("0");
                    }else {
                        nuevoservicio.setGas(gas.getText().toString());
                    }

                    if (luz.getText().toString().isEmpty()){
                        nuevoservicio.setLuz("0");
                    }else {
                        nuevoservicio.setLuz(luz.getText().toString());
                    }

                    if (agua.getText().toString().isEmpty()){
                        nuevoservicio.setAgua("0");
                    }else {
                        nuevoservicio.setAgua(agua.getText().toString());
                    }

                    if (telefono.getText().toString().isEmpty()){
                        nuevoservicio.setTelefono("0");
                    }else {
                        nuevoservicio.setTelefono(telefono.getText().toString());
                    }

                    if (internet.getText().toString().isEmpty()){
                        nuevoservicio.setInternet("0");
                    }else {
                        nuevoservicio.setInternet(internet.getText().toString());
                    }

                    if (cable.getText().toString().isEmpty()){
                        nuevoservicio.setCable("0");
                    }else {
                        nuevoservicio.setCable(cable.getText().toString());
                    }

                    if (lavanderia.getText().toString().isEmpty()){
                        nuevoservicio.setLavanderia("0");
                    }else {
                        nuevoservicio.setLavanderia(lavanderia.getText().toString());
                    }

                    if (otrosservicios.getText().toString().isEmpty()){
                        nuevoservicio.setOtrosServicios("0");
                    }else {
                        nuevoservicio.setOtrosServicios(otrosservicios.getText().toString());
                    }


                    //suma del total inicial
                    int_gas=Integer.parseInt(nuevoservicio.getGas());
                    int_luz=Integer.parseInt(nuevoservicio.getLuz());
                    int_agua=Integer.parseInt(nuevoservicio.getAgua());
                    int_telefono=Integer.parseInt(nuevoservicio.getTelefono());
                    int_internet=Integer.parseInt(nuevoservicio.getInternet());
                    int_cable=Integer.parseInt(nuevoservicio.getCable());
                    int_lavanderia=Integer.parseInt(nuevoservicio.getLavanderia());
                    int_otrosservicios=Integer.parseInt(nuevoservicio.getOtrosServicios());


                    total_final_servicios=0;
                    total_final_servicios=int_totalservicios+int_gas+int_luz+int_agua+int_telefono+int_internet+int_cable+
                            int_lavanderia+int_otrosservicios;


                    nuevoservicio.setTotalServicios(String.valueOf(total_final_servicios));


                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_servicios);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevoservicio);


                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Servicio obj4=new Servicio();
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Servicio.class);
                                vagas.setText("$ "+separador(obj4.getGas()));
                                valuz.setText("$ "+separador(obj4.getLuz()));
                                vaagua.setText("$ "+separador(obj4.getAgua()));
                                vatelefono.setText("$ "+separador(obj4.getTelefono()));
                                vainternet.setText("$ "+separador(obj4.getInternet()));
                                vacable.setText("$ "+separador(obj4.getCable()));
                                valavanderia.setText("$ "+separador(obj4.getLavanderia()));
                                vaotrosservicios.setText("$ "+separador(obj4.getOtrosServicios()));
                                totalservicios.setText("$ "+separador(obj4.getTotalServicios()));

                            }
                            //limpiar campos input
                            gas.setText("");
                            luz.setText("");
                            agua.setText("");
                            telefono.setText("");
                            internet.setText("");
                            cable.setText("");
                            lavanderia.setText("");
                            otrosservicios.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                    gas.setText("");
                    luz.setText("");
                    agua.setText("");
                    telefono.setText("");
                    internet.setText("");
                    cable.setText("");
                    lavanderia.setText("");
                    otrosservicios.setText("");
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



    //método para actualizar datos presupuesto servicios
    public void actualizarPresupuestoServicios(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Servicio obj=new Servicio();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Servicio.class);
                    //cálculo del total del presupuesto Servicios
                    int_gas=Integer.parseInt(obj.getGas());
                    int_luz=Integer.parseInt(obj.getLuz());
                    int_agua=Integer.parseInt(obj.getAgua());
                    int_telefono=Integer.parseInt(obj.getTelefono());
                    int_internet=Integer.parseInt(obj.getInternet());
                    int_cable=Integer.parseInt(obj.getCable());
                    int_lavanderia=Integer.parseInt(obj.getLavanderia());
                    int_otrosservicios=Integer.parseInt(obj.getOtrosServicios());
                    int_totalservicios=Integer.parseInt(obj.getTotalServicios());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;
                    if (!gas.getText().toString().isEmpty()){
                        int_gas=int_gas-Integer.parseInt(gas.getText().toString());
                        contador=contador+Integer.parseInt(gas.getText().toString());
                        if (int_gas<0){
                            valida=false;
                        }
                    }

                    if (!luz.getText().toString().isEmpty()){
                        int_luz=int_luz-Integer.parseInt(luz.getText().toString());
                        contador=contador+Integer.parseInt(luz.getText().toString());
                        if (int_luz<0){
                            valida=false;
                        }
                    }

                    if (!agua.getText().toString().isEmpty()){
                        int_agua=int_agua-Integer.parseInt(agua.getText().toString());
                        contador=contador+Integer.parseInt(agua.getText().toString());
                        if (int_agua<0){
                            valida=false;
                        }
                    }

                    if (!telefono.getText().toString().isEmpty()){
                        int_telefono=int_telefono-Integer.parseInt(telefono.getText().toString());
                        contador=contador+Integer.parseInt(telefono.getText().toString());
                        if (int_telefono<0){
                            valida=false;
                        }
                    }

                    if (!internet.getText().toString().isEmpty()){
                        int_internet=int_internet-Integer.parseInt(internet.getText().toString());
                        contador=contador+Integer.parseInt(internet.getText().toString());
                        if (int_internet<0){
                            valida=false;
                        }
                    }

                    if (!cable.getText().toString().isEmpty()){
                        int_cable=int_cable-Integer.parseInt(cable.getText().toString());
                        contador=contador+Integer.parseInt(cable.getText().toString());
                        if (int_cable<0){
                            valida=false;
                        }
                    }

                    if (!lavanderia.getText().toString().isEmpty()){
                        int_lavanderia=int_lavanderia-Integer.parseInt(lavanderia.getText().toString());
                        contador=contador+Integer.parseInt(lavanderia.getText().toString());
                        if (int_lavanderia<0){
                            valida=false;
                        }
                    }

                    if (!otrosservicios.getText().toString().isEmpty()){
                        int_otrosservicios=int_otrosservicios-Integer.parseInt(otrosservicios.getText().toString());
                        contador=contador+Integer.parseInt(otrosservicios.getText().toString());
                        if (int_otrosservicios<0){
                            valida=false;
                        }
                    }

                    //validación de campos llenos
                    if (gas.getText().toString().isEmpty() && luz.getText().toString().isEmpty()
                            && agua.getText().toString().isEmpty() && telefono.getText().toString().isEmpty()
                            && internet.getText().toString().isEmpty() && cable.getText().toString().isEmpty()
                            && lavanderia.getText().toString().isEmpty() && otrosservicios.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas


                    if (valida2==false){
                        Toast.makeText(Servicios.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();

                        //else if para seleccion de que servicio modificar
                        restarPresupuestoGlobal(contador);
                            if(!gas.getText().toString().isEmpty()){
                                int_totalservicios=int_totalservicios-Integer.parseInt(gas.getText().toString());
                                mDatabase.child(clave).child("gas").setValue(String.valueOf(int_gas));
                                mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                                gas.setText("");
                            }

                            if(!luz.getText().toString().isEmpty()){
                                int_totalservicios=int_totalservicios-Integer.parseInt(luz.getText().toString());
                                mDatabase.child(clave).child("luz").setValue(String.valueOf(int_luz));
                                mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                                luz.setText("");
                            }

                            if(!agua.getText().toString().isEmpty()){
                                int_totalservicios=int_totalservicios-Integer.parseInt(agua.getText().toString());
                                mDatabase.child(clave).child("agua").setValue(String.valueOf(int_agua));
                                mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                                agua.setText("");
                            }

                            if(!telefono.getText().toString().isEmpty()){
                                int_totalservicios=int_totalservicios-Integer.parseInt(telefono.getText().toString());
                                mDatabase.child(clave).child("telefono").setValue(String.valueOf(int_telefono));
                                mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                                telefono.setText("");
                            }

                            if(!internet.getText().toString().isEmpty()){
                                int_totalservicios=int_totalservicios-Integer.parseInt(internet.getText().toString());
                                mDatabase.child(clave).child("internet").setValue(String.valueOf(int_internet));
                                mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                                internet.setText("");
                            }

                            if(!cable.getText().toString().isEmpty()){
                                int_totalservicios=int_totalservicios-Integer.parseInt(cable.getText().toString());
                                mDatabase.child(clave).child("cable").setValue(String.valueOf(int_cable));
                                mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                                cable.setText("");
                            }

                            if(!lavanderia.getText().toString().isEmpty()){
                                int_totalservicios=int_totalservicios-Integer.parseInt(lavanderia.getText().toString());
                                mDatabase.child(clave).child("lavanderia").setValue(String.valueOf(int_lavanderia));
                                mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                                lavanderia.setText("");
                            }


                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Servicio obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Servicio.class);

                                    vagas.setText("$ "+separador(obj3.getGas()));
                                    valuz.setText("$ "+separador(obj3.getLuz()));
                                    vaagua.setText("$ "+separador(obj3.getAgua()));
                                    vatelefono.setText("$ "+separador(obj3.getTelefono()));
                                    vainternet.setText("$ "+separador(obj3.getInternet()));
                                    vacable.setText("$ "+separador(obj3.getCable()));
                                    valavanderia.setText("$ "+separador(obj3.getLavanderia()));
                                    vaotrosservicios.setText("$ "+separador(obj3.getOtrosServicios()));
                                    totalservicios.setText("$ "+separador(obj3.getTotalServicios()));

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        Toast.makeText(Servicios.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        gas.setText("");
                        luz.setText("");
                        agua.setText("");
                        telefono.setText("");
                        internet.setText("");
                        cable.setText("");
                        lavanderia.setText("");
                        otrosservicios.setText("");
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

/*
    //método para eliminar datos presupuesto casa
    public void eliminarPresupuestoServicios(final View view){
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Servicio obj2=new Servicio();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj2=datanapshot.getValue(Servicio.class);

                    //obtengo los valores de la nube
                    //cálculo del total del presupuesto Servicios
                    int_gas=Integer.parseInt(obj2.getGas());
                    int_luz=Integer.parseInt(obj2.getLuz());
                    int_agua=Integer.parseInt(obj2.getAgua());
                    int_telefono=Integer.parseInt(obj2.getTelefono());
                    int_internet=Integer.parseInt(obj2.getInternet());
                    int_cable=Integer.parseInt(obj2.getCable());
                    int_lavanderia=Integer.parseInt(obj2.getLavanderia());
                    int_otrosservicios=Integer.parseInt(obj2.getOtrosservicios());
                    int_totalservicios=Integer.parseInt(obj2.getTservicios());

                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    //else if para seleccion de que servicio modificar



                    if (view.getId() == R.id.imgvEliminarGas){
                        mDatabase.child(clave).child("gas").setValue("0");
                        int_totalservicios=int_totalservicios-int_gas;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        gas.setText("");
                    }else if (view.getId() == R.id.imgvEliminarLuz){
                        mDatabase.child(clave).child("luz").setValue("0");
                        int_totalservicios=int_totalservicios-int_luz;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        luz.setText("");
                    }else if (view.getId() == R.id.imgvEliminarAgua){
                        mDatabase.child(clave).child("agua").setValue("0");
                        int_totalservicios=int_totalservicios-int_agua;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        agua.setText("");
                    }else if (view.getId() == R.id.imgvEliminarTelefono){
                        mDatabase.child(clave).child("telefono").setValue("0");
                        int_totalservicios=int_totalservicios-int_telefono;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        telefono.setText("");
                    }else if (view.getId() == R.id.imgvEliminarInternet){
                        mDatabase.child(clave).child("internet").setValue("0");
                        int_totalservicios=int_totalservicios-int_internet;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        internet.setText("");
                    }else if (view.getId() == R.id.imgvEliminarCable){
                        mDatabase.child(clave).child("cable").setValue("0");
                        int_totalservicios=int_totalservicios-int_cable;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        cable.setText("");
                    }else if (view.getId() == R.id.imgvEliminarLavanderia){
                        mDatabase.child(clave).child("lavanderia").setValue("0");
                        int_totalservicios=int_totalservicios-int_lavanderia;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        lavanderia.setText("");
                    }else if(view.getId() == R.id.imgvEliminarOthersservicios){
                        mDatabase.child(clave).child("otrosservicios").setValue("0");
                        int_totalservicios=int_totalservicios-int_otrosservicios;
                        mDatabase.child(clave).child("tservicios").setValue(String.valueOf(int_totalservicios));
                        otrosservicios.setText("");
                    }else {
                    }

                    //nueva consulta para mostrar los valores actuales después de eliminar valores
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Servicio obj3=new Servicio();
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Servicio.class);
                                vagas.setText("$ "+obj3.getGas());
                                valuz.setText("$ "+obj3.getLuz());
                                vaagua.setText("$ "+obj3.getAgua());
                                vatelefono.setText("$ "+obj3.getTelefono());
                                vainternet.setText("$ "+obj3.getInternet());
                                vacable.setText("$ "+obj3.getCable());
                                valavanderia.setText("$ "+obj3.getLavanderia());
                                vaotrosservicios.setText("$ "+obj3.getOtrosservicios());
                                totalservicios.setText("$ "+obj3.getTservicios());
                            }
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
*/




}
