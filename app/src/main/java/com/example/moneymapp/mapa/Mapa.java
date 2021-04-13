package com.example.moneymapp.mapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymapp.MenuPrincipal;
import com.example.mentorapp.R;
import com.example.moneymapp.mapa.activos.Activos;
import com.example.moneymapp.mapa.activos.model.Activo;
import com.example.moneymapp.mapa.entradas.Entradas;
import com.example.moneymapp.mapa.entradas.model.Entrada;
import com.example.moneymapp.mapa.model.MapaModel;
import com.example.moneymapp.mapa.pasivos.Pasivvos;
import com.example.moneymapp.mapa.pasivos.model.Pasivo;
import com.example.moneymapp.mapa.salidas.Salidas;
import com.example.moneymapp.mapa.salidas.model.Salida;
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

public class Mapa extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference pDatabase, aDatabase, gDatabase, eDatabase, sDatabase, mDatabase;
    private FirebaseDatabase database;
    private String usuario;
    private MapaModel nuevomapa;
    public int c, e, s, n, v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //creacion de muchas instancias para modificar la riqueza neta, flujo efectivo neto
        database=FirebaseDatabase.getInstance();

        aDatabase = FirebaseDatabase.getInstance().getReference("Activos");
        gDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");
        eDatabase = FirebaseDatabase.getInstance().getReference("Entradas");
        sDatabase = FirebaseDatabase.getInstance().getReference("Salidas");
        pDatabase = FirebaseDatabase.getInstance().getReference("Mpasivos");
        mDatabase = FirebaseDatabase.getInstance().getReference("Mapa");

        verificaUsuario(mDatabase);//metodo que comprueba si existe un mapa, sino existe lo crea
        //ingresaMapa(aDatabase);//método que ingresa valores al mapa



    }

    //método que modifica los datos del mapa
    private void ingresaMapa(DatabaseReference aDatabase) {
        //accedo al monto de activos
        Query q3=aDatabase.orderByChild("correo").equalTo(usuario);
        q3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo m =datanapshot.getValue(Activo.class);
                   String valor=m.getActivosTotal();
                   modificaMapa(valor, "activos_total");//método que ingresa el valor de activos
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //accedo al monto de pasivos
        Query q4=pDatabase.orderByChild("correo").equalTo(usuario);
        q4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Pasivo m =datanapshot.getValue(Pasivo.class);
                    String valor=m.getPasivosTotal();
                    modificaMapa(valor, "pasivos_total");//método que ingresa el valor de activos
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //acceso al monto de entradas
        Query q5=eDatabase.orderByChild("correo").equalTo(usuario);
        q5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Entrada m =datanapshot.getValue(Entrada.class);
                    String valor=m.getEntradasTotal();
                    modificaMapa(valor, "entradas_total");//método que ingresa el valor de activos
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //acceso al monto de salidas
        Query q6=sDatabase.orderByChild("correo").equalTo(usuario);
        q6.addValueEventListener(new ValueEventListener() {
            String valor;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Salida m =datanapshot.getValue(Salida.class);
                    valor=m.getSalidaTotal();
                   //método que ingresa el valor de activos
                }
                //aquí añado el presupuesto
                Query q9=gDatabase.orderByChild("correo").equalTo(usuario);
                q9.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot datanapshot: dataSnapshot.getChildren()) {
                            Presupuestoglobal obj6 =datanapshot.getValue(Presupuestoglobal.class);
                            int d=Integer.parseInt(valor)+Integer.parseInt(obj6.getPresupuestoTotal());
                            modificaMapa(String.valueOf(d), "salidas_total");
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //obtengo el resultado de la riqueza neta
        //obtención de la "primary key"
        Query q=aDatabase.orderByChild("correo").equalTo(usuario);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo obj7 =datanapshot.getValue(Activo.class);
                    c=Integer.parseInt(obj7.getActivosTotal());
                }
                Query q=pDatabase.orderByChild("correo").equalTo(usuario);
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot datanapshot: dataSnapshot.getChildren()) {
                            Pasivo obj6 =datanapshot.getValue(Pasivo.class);
                            int d=c-Integer.parseInt(obj6.getPasivosTotal());
                            modificaMapa(String.valueOf(d),"riqueza_neta");
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //obtengo el resultado de la riqueza neta
        //obtención de la "primary key"
        Query f=eDatabase.orderByChild("correo").equalTo(usuario);

        f.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                e=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Entrada obj7 =datanapshot.getValue(Entrada.class);
                    e=Integer.parseInt(obj7.getEntradasTotal());
                }
                Query q=sDatabase.orderByChild("correo").equalTo(usuario);
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        n=0;
                        for (DataSnapshot datanapshot: dataSnapshot.getChildren()) {
                            Salida obj6 =datanapshot.getValue(Salida.class);
                            n=e-Integer.parseInt(obj6.getSalidaTotal());
                            modificaMapa(String.valueOf(s),"flujo_neto_activo");
                        }
                        Query q=gDatabase.orderByChild("correo").equalTo(usuario);
                        q.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                v=0;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()) {
                                    Presupuestoglobal obj6 =datanapshot.getValue(Presupuestoglobal.class);
                                    v=n-Integer.parseInt(obj6.getPresupuestoTotal());
                                    modificaMapa(String.valueOf(v),"flujo_neto_activo");
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para modificar el MAPA
    public void modificaMapa(final String num, final String num2){
        Query q3=mDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String z=num;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    MapaModel m =datanapshot.getValue(MapaModel.class);
                    //modificacion
                    String clave=datanapshot.getKey();
                    mDatabase.child(clave).child(num2).setValue(z);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    private void verificaUsuario(final DatabaseReference mDatabase2) {
        //se consulta a la bbdd para validar si existe ya el usuario y si no existe crear otro
        Query q2=mDatabase2.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //espacio para enviar toda la info a un registro en la bbdd para crear informes
                }

                if(c!=0){

                }else{

                    //aquí creo un mapa nuevo con la fecha de inicio y valores de 0
                    Calendar fecha= Calendar.getInstance();
                    int anio_inicio= fecha.get(Calendar.YEAR);
                    int mes_inicio= fecha.get(Calendar.MONTH)+1;
                    int dia_inicio= fecha.get(Calendar.DAY_OF_MONTH);

                    int z=dia_inicio-1;
                    String x= String.valueOf(z);

                    nuevomapa= new MapaModel();
                    nuevomapa.setCorreo(usuario);
                    nuevomapa.setDiaInicio(x);
                    nuevomapa.setMesInicio(String.valueOf(mes_inicio));
                    nuevomapa.setAnioInicio(String.valueOf(anio_inicio));
                    nuevomapa.setActivosTotal("0");
                    nuevomapa.setPasivosTotal("0");
                    nuevomapa.setEntradasTotal("0");
                    nuevomapa.setSalidasTotal("0");
                    nuevomapa.setRiquezaNeta("0");
                    nuevomapa.setFlujoEfectivoNeto("0");

                    String clave= mDatabase2.push().getKey();
                    mDatabase2.child(clave).setValue(nuevomapa);
                    ingresaMapa(aDatabase);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    //ir a fuentes de ingreso
    public void irActivos(View v){
        Intent iraactivos = new Intent(this, Activos.class);
        startActivity(iraactivos);
    }

    //volver al menú principal
    public void irMenuPrincipal(View v){
        Intent iramenuprincipal = new Intent(this, MenuPrincipal.class);
        startActivity(iramenuprincipal);
    }

    //ir a fuentes de ingreso
    public void iraPasivos(View v){
        Intent irapasivos = new Intent(this, Pasivvos.class);
        startActivity(irapasivos);
    }

    //ir a fuentes de ingreso
    public void iraEntradas(View v){
        Intent irapasivos = new Intent(this, Entradas.class);
        startActivity(irapasivos);
    }


    //ir a fuentes de ingreso
    public void iraSalidas(View v){
        Intent irapasivos = new Intent(this, Salidas.class);
        startActivity(irapasivos);
    }
}
