package com.example.moneymapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.instrucciones.Etapa;
import com.example.moneymapp.mapa.model.MapaModel;
import com.example.moneymapp.pasivos.model.TotalPasivo;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    private FirebaseUser user;
    private TextView txvNumeroRiqueza, txvActivos, txvPasivos, txvEntradas, txvSalidas,
                      txvRiquezaNeta, txvFlujoNeto, txvIngresosPasivos, etapa;
    private DatabaseReference mapDatabaseReference, etapasDatabaseRference,
            totalPasivosDatabaseReference, presupuestoGlobalDatabaseReference;
    private String usuario;
    private boolean validarEtapas, validarNumeroRiqueza;
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txvActivos =findViewById(R.id.txvActivosHome);
        txvPasivos =findViewById(R.id.txvPasivosHome);
        txvRiquezaNeta =findViewById(R.id.txvRiquezaNeta);
        txvEntradas =findViewById(R.id.txvEntradas);
        txvSalidas =findViewById(R.id.txvSalidas);
        txvFlujoNeto =findViewById(R.id.txvTotalflujoEfectivoNeto);
        txvIngresosPasivos =findViewById(R.id.txvIngresosPasivos);
        etapa=findViewById(R.id.txvEtapa);
        txvNumeroRiqueza =findViewById(R.id.txvNumeroRiqueza);


        //obtención del nombre de usuario de la aplicación
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();


        presupuestoGlobalDatabaseReference = FirebaseDatabase.getInstance().
                                             getReference("Presupuestoglobal");
        etapasDatabaseRference = FirebaseDatabase.getInstance().getReference("Etapas");

        totalPasivosDatabaseReference = FirebaseDatabase.getInstance().
                getReference("TotalPasivos");

        mapDatabaseReference = FirebaseDatabase.getInstance().getReference("Mapa");


        obtenerNumeroRiqueza();

        obtenerRiquezaNeta();

        mostrarGanaciasPasivos();

        verificaEtapa();
    }



    private void verificaEtapa() {
        Query query= etapasDatabaseRference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Etapa objEtapa;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    objEtapa=datanapshot.getValue(Etapa.class);

                    //para mostrar la etapa en pantalla
                    if (objEtapa.getNombreEtapa().equals("Punto de Partida")){
                        etapa.setText("1° Punto de Partida");
                        validarEtapas =true;
                    }else if(objEtapa.getNombreEtapa().equals("Optimizar")){
                        etapa.setText("2° Optimizar");
                        validarEtapas =true;
                    }else if (objEtapa.getNombreEtapa().equals("Escalar")){
                        etapa.setText("3° Escalar");
                        validarEtapas =true;
                    }else if (objEtapa.getNombreEtapa().equals("Acelerar")){
                        etapa.setText("4° Acelerar");
                        validarEtapas =true;
                    }else{
                        etapa.setText("5° Mantener");
                        validarEtapas =true;
                    }
                }
                if(validarEtapas =false){
                    etapa.setText("1° Punto de Partida");//si recién comienza
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mostrarGanaciasPasivos() {
        Query query= totalPasivosDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    TotalPasivo totalPasivos =datanapshot.getValue(TotalPasivo.class);
                    txvIngresosPasivos.setText("$"+separadorMiles(totalPasivos.
                            getGananciaTotalPasivos()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public String separadorMiles(String numero){
        int number=Integer.parseInt(numero);
        String str = String.format("%,d", number);
        String numerofinal=str.replace(",",".");
        return numerofinal;
    }

    public void obtenerRiquezaNeta(){
        Query query= mapDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    MapaModel mapa =datanapshot.getValue(MapaModel.class);

                    txvActivos.setText("$"+separadorMiles(mapa.getActivosTotal()));
                    txvPasivos.setText("$"+separadorMiles(mapa.getPasivosTotal()));
                    txvEntradas.setText("$"+separadorMiles(mapa.getEntradasTotal()));
                    txvSalidas.setText("$"+separadorMiles(mapa.getSalidasTotal()));

                    /*cambio de color dependiendo del valor del valor de la riqueza neta
                     y flujo activo*/
                    int riquezaNeta=Integer.parseInt(mapa.getRiquezaNeta());
                    if (riquezaNeta<0){
                        txvRiquezaNeta.setTextColor(Color.rgb(168,33,17));
                        txvRiquezaNeta.setText("$"+separadorMiles(mapa.getRiquezaNeta()));
                    }else {
                        txvRiquezaNeta.setTextColor(Color.rgb(74,95,5));
                        txvRiquezaNeta.setText("$"+separadorMiles(mapa.getRiquezaNeta()));
                    }
                    int flujoEfectivoNeto=Integer.parseInt(mapa.getFlujoEfectivoNeto());
                    if (flujoEfectivoNeto<0){
                        txvFlujoNeto.setTextColor(Color.rgb(168,33,17));
                        txvFlujoNeto.setText("$"+separadorMiles(mapa.getFlujoEfectivoNeto()));
                    }else {
                        txvFlujoNeto.setTextColor(Color.rgb(74,95,5));
                        txvFlujoNeto.setText("$"+separadorMiles(mapa.getFlujoEfectivoNeto()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void obtenerNumeroRiqueza() {
        Query query=presupuestoGlobalDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    validarNumeroRiqueza=true;
                    Presupuestoglobal presupuesto =datanapshot.getValue(Presupuestoglobal.class);
                    txvNumeroRiqueza.setText("$"+separadorMiles(presupuesto.getPresupuestoTotal()));
                }
                if(validarNumeroRiqueza=false){
                    txvNumeroRiqueza.setText("$ 0.00");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void irMenu(View view){
        Intent irMenuPrincipal= new Intent(this, MenuPrincipal.class);
        startActivity(irMenuPrincipal);
    }
}
