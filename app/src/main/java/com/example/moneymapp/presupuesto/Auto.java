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
import com.example.moneymapp.presupuesto.model.Autos;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Auto extends AppCompatActivity {

    private FirebaseUser user;
    private TextView vamantenimientoauto, vaotrosauto, totalauto, vagasolina, valavado, vaestacionamiento;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText gasolina, lavado, mantenimientoauto, estacionamiento, otrosauto;
    private Autos nuevoauto;
    private int c,int_gasolina, int_lavado, int_mantenimientoauto, int_estacionamiento, int_otrosautos,
            int_totalauto, total_final_auto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);

        gasolina=(EditText)findViewById(R.id.edtGasolina);
        lavado=(EditText)findViewById(R.id.edtLavado);
        mantenimientoauto=(EditText)findViewById(R.id.edtMantenimientoAuto);
        estacionamiento=(EditText)findViewById(R.id.edtEstacionamiento);
        otrosauto=(EditText)findViewById(R.id.edtOtrosAuto);
        totalauto=(TextView)findViewById(R.id.txvTotalAuto);

        vagasolina=(TextView)findViewById(R.id.txvVagasolina);
        valavado=(TextView)findViewById(R.id.txvValavado);
        vamantenimientoauto=(TextView)findViewById(R.id.txvVamantenimientoAuto);
        vaestacionamiento=(TextView)findViewById(R.id.txvVaestacionamiento);
        vaotrosauto=(TextView)findViewById(R.id.txvVaotrosAuto);



        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Autos");
        tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Autos obj=new Autos();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Autos.class);
                    vagasolina.setText("$ "+separador(obj.getGasolina()));
                    valavado.setText("$ "+separador(obj.getLavado()));
                    vamantenimientoauto.setText("$ "+separador(obj.getMantenimiento()));
                    vaestacionamiento.setText("$ "+separador(obj.getEstacionamiento()));
                    vaotrosauto.setText("$ "+separador(obj.getOtros()));
                    totalauto.setText("$ "+separador(obj.getTotalAutos()));

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
    public void ingresarPresupuestoAuto(View view) {
        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Autos obj=new Autos();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Autos.class);
                    vagasolina.setText("$ "+separador(obj.getGasolina()));
                    valavado.setText("$ "+separador(obj.getLavado()));
                    vamantenimientoauto.setText("$ "+separador(obj.getMantenimiento()));
                    vaestacionamiento.setText("$ "+separador(obj.getEstacionamiento()));
                    vaotrosauto.setText("$ "+separador(obj.getOtros()));
                    totalauto.setText("$ "+separador(obj.getTotalAutos()));

                    //cálculo del total del presupuesto Autos
                    int_gasolina=Integer.parseInt(obj.getGasolina());
                    int_lavado=Integer.parseInt(obj.getLavado());
                    int_mantenimientoauto=Integer.parseInt(obj.getMantenimiento());
                    int_estacionamiento=Integer.parseInt(obj.getEstacionamiento());
                    int_otrosautos=Integer.parseInt(obj.getOtros());
                    int_totalauto=Integer.parseInt(obj.getTotalAutos());


                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_auto=0;

                    if(!gasolina.getText().toString().isEmpty()){
                        int_gasolina=int_gasolina+Integer.parseInt(gasolina.getText().toString());
                        total_final_auto=total_final_auto+Integer.parseInt(gasolina.getText().toString());
                        mDatabase.child(clave).child("gasolina").setValue(String.valueOf(int_gasolina));
                        gasolina.setText("");
                    }
                    if(!lavado.getText().toString().isEmpty()){
                        int_lavado=int_lavado+Integer.parseInt(lavado.getText().toString());
                        total_final_auto=total_final_auto+Integer.parseInt(lavado.getText().toString());
                        mDatabase.child(clave).child("lavado").setValue(String.valueOf(int_lavado));
                        lavado.setText("");
                    }
                    if(!mantenimientoauto.getText().toString().isEmpty()){
                        int_mantenimientoauto=int_mantenimientoauto+Integer.parseInt(mantenimientoauto.getText().toString());
                        total_final_auto=total_final_auto+Integer.parseInt(mantenimientoauto.getText().toString());
                        mDatabase.child(clave).child("mantenimiento").setValue(String.valueOf(int_mantenimientoauto));
                        mantenimientoauto.setText("");
                    }
                    if(!estacionamiento.getText().toString().isEmpty()){
                        int_estacionamiento=int_estacionamiento+Integer.parseInt(estacionamiento.getText().toString());
                        total_final_auto=total_final_auto+Integer.parseInt(estacionamiento.getText().toString());
                        mDatabase.child(clave).child("estacionamiento").setValue(String.valueOf(int_estacionamiento));
                        estacionamiento.setText("");
                    }
                    if(!otrosauto.getText().toString().isEmpty()){
                        int_otrosautos=int_otrosautos+Integer.parseInt(otrosauto.getText().toString());
                        total_final_auto=total_final_auto+Integer.parseInt(otrosauto.getText().toString());
                        mDatabase.child(clave).child("otros").setValue(String.valueOf(int_otrosautos));
                        otrosauto.setText("");
                    }

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_auto);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_auto=total_final_auto+int_totalauto;
                    mDatabase.child(clave).child("tautos").setValue(String.valueOf(total_final_auto));


                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Autos obj3=new Autos();
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Autos.class);
                                vagasolina.setText("$ "+separador(obj3.getGasolina()));
                                valavado.setText("$ "+separador(obj3.getLavado()));
                                vamantenimientoauto.setText("$ "+separador(obj3.getMantenimiento()));
                                vaestacionamiento.setText("$ "+separador(obj3.getEstacionamiento()));
                                vaotrosauto.setText("$ "+separador(obj3.getOtros()));
                                totalauto.setText("$ "+separador(obj3.getTotalAutos()));
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
                    nuevoauto= new Autos();
                    nuevoauto.setCorreo(usuario);

                    if (gasolina.getText().toString().isEmpty()){
                        nuevoauto.setGasolina("0");
                    }else {
                        nuevoauto.setGasolina(gasolina.getText().toString());
                    }

                    if (lavado.getText().toString().isEmpty()){
                        nuevoauto.setLavado("0");
                    }else {
                        nuevoauto.setLavado(lavado.getText().toString());
                    }

                    if (mantenimientoauto.getText().toString().isEmpty()){
                        nuevoauto.setMantenimiento("0");
                    }else {
                        nuevoauto.setMantenimiento(mantenimientoauto.getText().toString());
                    }

                    if (estacionamiento.getText().toString().isEmpty()){
                        nuevoauto.setEstacionamiento("0");
                    }else {
                        nuevoauto.setEstacionamiento(estacionamiento.getText().toString());
                    }

                    if (otrosauto.getText().toString().isEmpty()){
                        nuevoauto.setOtros("0");
                    }else {
                        nuevoauto.setOtros(otrosauto.getText().toString());
                    }

                    //suma del total inicial
                    int_gasolina=Integer.parseInt(nuevoauto.getGasolina());
                    int_lavado=Integer.parseInt(nuevoauto.getLavado());
                    int_mantenimientoauto=Integer.parseInt(nuevoauto.getMantenimiento());
                    int_estacionamiento=Integer.parseInt(nuevoauto.getEstacionamiento());
                    int_otrosautos=Integer.parseInt(nuevoauto.getOtros());

                    total_final_auto=0;
                    total_final_auto=int_totalauto+int_gasolina+int_lavado+int_mantenimientoauto+int_estacionamiento+
                            int_otrosautos;
                    nuevoauto.setTotalAutos(String.valueOf(total_final_auto));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_auto);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevoauto);

                    //se realiza otra consulta para actualizar los datos
                    Query qu=mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Autos obj4=new Autos();
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Autos.class);
                                vagasolina.setText("$ "+separador(obj4.getGasolina()));
                                valavado.setText("$ "+separador(obj4.getLavado()));
                                vamantenimientoauto.setText("$ "+separador(obj4.getMantenimiento()));
                                vaestacionamiento.setText("$ "+separador(obj4.getEstacionamiento()));
                                vaotrosauto.setText("$ "+separador(obj4.getOtros()));
                                totalauto.setText("$ "+separador(obj4.getTotalAutos()));

                            }
                            //limpiar campos input
                            gasolina.setText("");
                            lavado.setText("");
                            mantenimientoauto.setText("");
                            estacionamiento.setText("");
                            otrosauto.setText("");

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

    //método para actualizar datos presupuesto auto
    public void actualizarPresupuestoAuto(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Autos obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Autos.class);
                    //cálculo del total del presupuesto Autos
                    int_gasolina=Integer.parseInt(obj.getGasolina());
                    int_lavado=Integer.parseInt(obj.getLavado());
                    int_mantenimientoauto=Integer.parseInt(obj.getMantenimiento());
                    int_estacionamiento=Integer.parseInt(obj.getEstacionamiento());
                    int_otrosautos=Integer.parseInt(obj.getOtros());
                    int_totalauto=Integer.parseInt(obj.getTotalAutos());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;
                    if (!gasolina.getText().toString().isEmpty()){
                        int_gasolina=int_gasolina-Integer.parseInt(gasolina.getText().toString());
                        contador=contador+Integer.parseInt(gasolina.getText().toString());
                        if (int_gasolina<0){
                            valida=false;
                        }
                    }

                    if (!lavado.getText().toString().isEmpty()){
                        int_lavado=int_lavado-Integer.parseInt(lavado.getText().toString());
                        contador=contador+Integer.parseInt(lavado.getText().toString());
                        if (int_lavado<0){
                            valida=false;
                        }
                    }

                    if (!mantenimientoauto.getText().toString().isEmpty()){
                        int_mantenimientoauto=int_mantenimientoauto-Integer.parseInt(mantenimientoauto.getText().toString());
                        contador=contador+Integer.parseInt(mantenimientoauto.getText().toString());
                        if (int_mantenimientoauto<0){
                            valida=false;
                        }
                    }
                    if (!estacionamiento.getText().toString().isEmpty()){
                        int_estacionamiento=int_estacionamiento-Integer.parseInt(estacionamiento.getText().toString());
                        contador=contador+Integer.parseInt(estacionamiento.getText().toString());
                        if (int_estacionamiento<0){
                            valida=false;
                        }
                    }

                    if (!otrosauto.getText().toString().isEmpty()){
                        int_otrosautos=int_otrosautos-Integer.parseInt(otrosauto.getText().toString());
                        contador=contador+Integer.parseInt(otrosauto.getText().toString());
                        if (int_otrosautos<0){
                            valida=false;
                        }
                    }



                    //validación de campos llenos
                    if (gasolina.getText().toString().isEmpty() && lavado.getText().toString().isEmpty()
                            && mantenimientoauto.getText().toString().isEmpty()&&estacionamiento.getText().toString().isEmpty()&&
                            otrosauto.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Auto.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarPresupuestoGlobal(contador);
                        if(!gasolina.getText().toString().isEmpty()){
                            int_totalauto=int_totalauto-Integer.parseInt(gasolina.getText().toString());
                            mDatabase.child(clave).child("tautos").setValue(String.valueOf(int_totalauto));
                            mDatabase.child(clave).child("gasolina").setValue(String.valueOf(int_gasolina));
                            gasolina.setText("");
                        }

                        if(!lavado.getText().toString().isEmpty()){
                            int_totalauto=int_totalauto-Integer.parseInt(lavado.getText().toString());
                            mDatabase.child(clave).child("tautos").setValue(String.valueOf(int_totalauto));
                            mDatabase.child(clave).child("lavado").setValue(String.valueOf(int_lavado));
                            lavado.setText("");
                        }


                        if(!mantenimientoauto.getText().toString().isEmpty()){
                            int_totalauto=int_totalauto-Integer.parseInt(mantenimientoauto.getText().toString());
                            mDatabase.child(clave).child("tautos").setValue(String.valueOf(int_totalauto));
                            mDatabase.child(clave).child("mantenimiento").setValue(String.valueOf(int_mantenimientoauto));
                            mantenimientoauto.setText("");
                        }

                        if(!estacionamiento.getText().toString().isEmpty()){
                            int_totalauto=int_totalauto-Integer.parseInt(estacionamiento.getText().toString());
                            mDatabase.child(clave).child("tautos").setValue(String.valueOf(int_totalauto));
                            mDatabase.child(clave).child("estacionamiento").setValue(String.valueOf(int_estacionamiento));
                            estacionamiento.setText("");
                        }

                        if(!otrosauto.getText().toString().isEmpty()){
                            int_totalauto=int_totalauto-Integer.parseInt(otrosauto.getText().toString());
                            mDatabase.child(clave).child("tautos").setValue(String.valueOf(int_totalauto));
                            mDatabase.child(clave).child("otros").setValue(String.valueOf(int_otrosautos));
                            otrosauto.setText("");
                        }


                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Autos obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Autos.class);
                                    vagasolina.setText("$ "+separador(obj3.getGasolina()));
                                    valavado.setText("$ "+separador(obj3.getLavado()));
                                    vamantenimientoauto.setText("$ "+separador(obj3.getMantenimiento()));
                                    vaestacionamiento.setText("$ "+separador(obj3.getEstacionamiento()));
                                    vaotrosauto.setText("$ "+separador(obj3.getOtros()));
                                    totalauto.setText("$ "+separador(obj3.getTotalAutos()));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Auto.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        gasolina.setText("");
                        lavado.setText("");
                        vamantenimientoauto.setText("");
                        vaestacionamiento.setText("");
                        vaotrosauto.setText("");
                    }
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




}
