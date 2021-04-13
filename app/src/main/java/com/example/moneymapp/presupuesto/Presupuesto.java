package com.example.moneymapp.presupuesto;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymapp.MenuPrincipal;
import com.example.mentorapp.R;
import com.example.moneymapp.mapa.salidas.model.Salida;
import com.example.moneymapp.presupuesto.model.Autos;
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

import java.util.Calendar;

public class Presupuesto extends AppCompatActivity {
    private FirebaseUser user;
    private TextView txvpresupuestofinal, reset;
    private DatabaseReference mDatabase, mDatabase2, cDatabase, autoDatabase,servDatabase, alimDatabase,
                            presDatabase,seguDatabase, hijoDatabase, viaDatabase, sDatabase;
    private FirebaseDatabase database;
    private String usuario;
    private Presupuestoglobal nuevopresupuesto;
    public int int_total,c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        txvpresupuestofinal=findViewById(R.id.txvPresupuestoFinal);


        reset =findViewById(R.id.txvReset);
        reset.setPaintFlags(reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        reset.setText("reset");


        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //creacion de muchas instancias para ser eliminadas
        database=FirebaseDatabase.getInstance();

        cDatabase = FirebaseDatabase.getInstance().getReference("Casas");
        autoDatabase = FirebaseDatabase.getInstance().getReference("Autos");
        servDatabase = FirebaseDatabase.getInstance().getReference("Servicios");
        alimDatabase = FirebaseDatabase.getInstance().getReference("Alimento");
        presDatabase=FirebaseDatabase.getInstance().getReference("Prestamos");
        seguDatabase=FirebaseDatabase.getInstance().getReference("Seguros");
        hijoDatabase=FirebaseDatabase.getInstance().getReference("Hijos");
        viaDatabase=FirebaseDatabase.getInstance().getReference("Viajes");

        mDatabase2 = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");
        mDatabase = FirebaseDatabase.getInstance().getReference("Autos");

        sDatabase = FirebaseDatabase.getInstance().getReference("Salidas");

       verificaUsuario(mDatabase2);
        getPresupuesto(mDatabase2);


    }

    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }

    private void verificaUsuario(final DatabaseReference mDatabase2) {
        //se consulta a la bbdd para validar si existe ya el usuario y si no existe crear otro
        Query q2=mDatabase2.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Presupuestoglobal presupuesto =datanapshot.getValue(Presupuestoglobal.class);
                    String clave=datanapshot.getKey();

                    //aquí donde saco la fecha de la BBDD
                    int anio_inicio=Integer.parseInt(presupuesto.getAñoInicio());
                    int mes_inicio=Integer.parseInt(presupuesto.getMesInicio());
                    int dia_inicio=Integer.parseInt(presupuesto.getDiaInicio());

                    Calendar inicio=Calendar.getInstance();
                    inicio.set(anio_inicio,mes_inicio-1,dia_inicio);
                    inicio.set(Calendar.HOUR,0);
                    inicio.set(Calendar.HOUR_OF_DAY,0);
                    inicio.set(Calendar.MINUTE,0);
                    inicio.set(Calendar.SECOND,0);



                    //aquí saco la fecha actual
                    Calendar actual=Calendar.getInstance();
                    actual.set(Calendar.HOUR,0);
                    actual.set(Calendar.HOUR_OF_DAY,0);
                    actual.set(Calendar.MINUTE,0);
                    actual.set(Calendar.SECOND,0);

                    long finMS=actual.getTimeInMillis();
                    long inicioMS=inicio.getTimeInMillis();

                    //aquí obtengo el número de días totales
                    int dias=(int)(Math.abs(finMS-inicioMS)/(1000*60*60*24));
                    //cuando cuenta 31 dias ahi se cierra junto con el estado abierto
                    if(presupuesto.getEstadoPresupuesto().equals("abierto")){
                        //aquí cambio el estado al presupuesto para poder darle la señal al Home
                        mDatabase2.child(clave).child("estado_presupuesto").setValue("cerrado");

                        //aqui ingreso el presupuesto a las salidas mediante un método
                        String valor=presupuesto.getPresupuestoTotal();
                        sumaSalidas(Integer.parseInt(valor));


                    }else{
                        //se hará una pruba para ver si funciona
                        //aquí puedo colocar los días del presupuesto mensual para que se oriente el usuario

                    }
                    c++;
                }

                if(c!=0){
                    //ya existe el usuario registrado y se cuentan los días de el presupuesto mensual, si son 30 se cierra el estado,
                    //guardaría la fecha inicial para que si son m´s de 30 días no active nada , se guardaría día, mes y año



                }else{

                    //aquí le creo un estado abierto, ingreso la fecha inicial, y comienza el contador, osea se cambia a estado abierto
                    Calendar fecha= Calendar.getInstance();
                    int anio_inicio= fecha.get(Calendar.YEAR);
                    int mes_inicio= fecha.get(Calendar.MONTH)+1;
                    int dia_inicio= fecha.get(Calendar.DAY_OF_MONTH);

                    int z=dia_inicio-1;
                    String x= String.valueOf(z);

                    nuevopresupuesto= new Presupuestoglobal();
                    nuevopresupuesto.setCorreo(usuario);
                    nuevopresupuesto.setDiaInicio(x);
                    nuevopresupuesto.setMesInicio(String.valueOf(mes_inicio));
                    nuevopresupuesto.setAñoInicio(String.valueOf(anio_inicio));
                    nuevopresupuesto.setEstadoPresupuesto("abierto");
                    nuevopresupuesto.setPresupuestoTotal("0");

                    String clave= mDatabase2.push().getKey();
                    mDatabase2.child(clave).setValue(nuevopresupuesto);

                    //aquí comienza a crear y registrar la fecha de inicio
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //muestra el presupuesto en pantalla
    public void getPresupuesto(DatabaseReference mDatabase2){
        //obtención de la "primary key"
        Query q=mDatabase2.orderByChild("correo").equalTo(usuario);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Presupuestoglobal x =datanapshot.getValue(Presupuestoglobal.class);
                    String clave=datanapshot.getKey();
                    txvpresupuestofinal.setText(separador(x.getPresupuestoTotal()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //método para agregar a salidas el presupuesto mensual
    public void sumaSalidas(final int v){
        //obtención de la "primary key"
        Query q=sDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Salida m =datanapshot.getValue(Salida.class);
                    String clave=datanapshot.getKey();
                    int x=Integer.parseInt(m.getSalidaTotal())-Integer.parseInt(m.getAlterna());
                    x=x+z;
                    sDatabase.child(clave).child("salidas_total").setValue(String.valueOf(x));
                    sDatabase.child(clave).child("alterna").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    //volver al menú principal
    public void irMenuPrincipal(View v){
        Intent iramenuprincipal = new Intent(this, MenuPrincipal.class);
        startActivity(iramenuprincipal);
    }

    //método para ir a casa
    public void iraCasa(View v){
        Intent iracasa = new Intent(this,Casa.class);
        startActivity(iracasa);
    }

    //método para ir a Auto
    public void iraAuto(View v){
        Intent iraauto = new Intent(this,Auto.class);
        startActivity(iraauto);
    }

    //método para ir a Servicios
    public void iraServicio(View v){
        Intent iraservicio = new Intent(this,Servicios.class);
        startActivity(iraservicio);
    }

    //método para ir a alimentos
    public void iraAlimentos(View v){
        Intent iraalimentos = new Intent(this,Alimentos.class);
        startActivity(iraalimentos);
    }

    //método para ir a préstamos
    public void iraPrestamos(View v){
        Intent iraprestamos = new Intent(this,Prestamos.class);
        startActivity(iraprestamos);
    }

    //método para ir a seguros
    public void iraSeguros(View v){
        Intent iraseguros = new Intent(this,Seguros.class);
        startActivity(iraseguros);
    }

    //método para ir a presupuesto hijos
    public void irapHijos(View v){
        Intent iraphijos = new Intent(this,Hijos.class);
        startActivity(iraphijos);
    }

    //método para ir a presupuesto viajes
    public void irapViajes(View v){
        Intent irapviajes = new Intent(this,Viajes.class);
        startActivity(irapviajes);
    }

    //metodo para resetear el presupuesto completo
    public void resetPresupuesto(View view){

        //eliminar la instancia de casa
        Query query=cDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Casas obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Casas.class);
                    String clave=datanapshot.getKey();
                    cDatabase=database.getReference("Casas").child(clave);
                    cDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //eliminar el registro de presupues de auto
        Query q=autoDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Autos obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Autos.class);
                    String clave=datanapshot.getKey();
                    autoDatabase=database.getReference("Autos").child(clave);
                    autoDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //eliminar el registro de presupuesto de servicios
        Query s=servDatabase.orderByChild("correo").equalTo(usuario);

        s.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    String clave=datanapshot.getKey();
                    servDatabase=database.getReference("Servicios").child(clave);
                    servDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //eliminar el registro de presupuesto de alimento
        Query a=alimDatabase.orderByChild("correo").equalTo(usuario);

        a.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    String clave=datanapshot.getKey();
                    alimDatabase=database.getReference("Alimento").child(clave);
                    alimDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //eliminar el registro de presupuesto de préstamos
        Query p=presDatabase.orderByChild("correo").equalTo(usuario);

        p.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    String clave=datanapshot.getKey();
                    presDatabase=database.getReference("Prestamos").child(clave);
                    presDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //eliminar el registro de presupuesto de seguros
        Query sg=seguDatabase.orderByChild("correo").equalTo(usuario);

        sg.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    String clave=datanapshot.getKey();
                    seguDatabase=database.getReference("Seguros").child(clave);
                    seguDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //eliminar el registro de presupuesto de hijos
        Query h=hijoDatabase.orderByChild("correo").equalTo(usuario);

        h.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    String clave=datanapshot.getKey();
                    hijoDatabase=database.getReference("Hijos").child(clave);
                    hijoDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //eliminar el registro de presupuesto de hijos
        Query v=viaDatabase.orderByChild("correo").equalTo(usuario);

        v.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    String clave=datanapshot.getKey();
                    viaDatabase=database.getReference("Viajes").child(clave);
                    viaDatabase.removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //reinicio del presupuesto
        Query np=mDatabase2.orderByChild("correo").equalTo(usuario);

        np.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Presupuestoglobal nuevo;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    nuevo= new Presupuestoglobal();

                    //aquí le creo un estado abierto, ingreso la fecha inicial, y comienza el contador, osea se cambia a estado abierto
                    Calendar fecha= Calendar.getInstance();
                    int anio_inicio= fecha.get(Calendar.YEAR);
                    int mes_inicio= fecha.get(Calendar.MONTH)+1;
                    int dia_inicio= fecha.get(Calendar.DAY_OF_MONTH);

                    int dia=dia_inicio-1;
                    String x=String.valueOf(dia);

                    String clave=datanapshot.getKey();
                    mDatabase2.child(clave).child("anio_inicio").setValue(String.valueOf(anio_inicio));
                    mDatabase2.child(clave).child("dia_inicio").setValue(x);
                    mDatabase2.child(clave).child("mes_inicio").setValue(String.valueOf(mes_inicio));
                    mDatabase2.child(clave).child("estado_presupuesto").setValue("abierto");
                    mDatabase2.child(clave).child("presupuesto_total").setValue("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });






    }





}



