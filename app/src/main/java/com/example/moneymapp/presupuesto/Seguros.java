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
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.example.moneymapp.presupuesto.model.Seguro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Seguros extends AppCompatActivity {

    private FirebaseUser user;
    private TextView vaseguroauto, vasegurovivienda, totalseguros, vasegurovida, vaseguromedico, vaotrosseguro;
    private Boolean comprobar;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario, correo;
    private EditText sauto, svivienda, svida, smedico, sotros;
    private Seguro nuevoseguro;
    private int c,int_sauto, int_svivienda, int_svida, int_smedico, int_otrosseguros,
            int_totalseguro, total_final_seguros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguros);

        sauto=findViewById(R.id.edtSeguroauto);
        svivienda=findViewById(R.id.edtSegurovivienda);
        svida=findViewById(R.id.edtSegurovida);
        smedico=findViewById(R.id.edtSeguromedico);
        sotros=findViewById(R.id.edtOtrosseguros);
        totalseguros=findViewById(R.id.txvTotalseguros);

        vaseguroauto=findViewById(R.id.txvVaseguroauto);
        vasegurovivienda=findViewById(R.id.txvVasegurovivienda);
        vasegurovida=findViewById(R.id.txvVasegurovida);
        vaseguromedico=findViewById(R.id.txvVaseguromedico);
        vaotrosseguro=findViewById(R.id.txvVaotrosseguros);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Seguros");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Seguro obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Seguro.class);
                    vaseguroauto.setText("$ "+separador(obj.getSeguroAuto()));
                    vasegurovivienda.setText("$ "+separador(obj.getSeguroVivienda()));
                    vasegurovida.setText("$ "+separador(obj.getSeguroVida()));
                    vaseguromedico.setText("$ "+separador(obj.getSeguroMedico()));
                    vaotrosseguro.setText("$ "+separador(obj.getOtrosSeguros()));
                    totalseguros.setText("$ "+separador(obj.getTotalSeguros()));
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
    public void ingresarPresupuestoSeguros(View view) {
        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Seguro obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Seguro.class);
                    vaseguroauto.setText("$ "+separador(obj.getSeguroAuto()));
                    vasegurovivienda.setText("$ "+separador(obj.getSeguroVivienda()));
                    vasegurovida.setText("$ "+separador(obj.getSeguroVida()));
                    vaseguromedico.setText("$ "+separador(obj.getSeguroMedico()));
                    vaotrosseguro.setText("$ "+separador(obj.getOtrosSeguros()));
                    totalseguros.setText("$ "+separador(obj.getTotalSeguros()));

                    //cálculo del total del presupuesto Seguros
                    int_sauto=Integer.parseInt(obj.getSeguroAuto());
                    int_svivienda=Integer.parseInt(obj.getSeguroVivienda());
                    int_svida=Integer.parseInt(obj.getSeguroVida());
                    int_smedico=Integer.parseInt(obj.getSeguroMedico());
                    int_otrosseguros=Integer.parseInt(obj.getOtrosSeguros());
                    int_totalseguro=Integer.parseInt(obj.getTotalSeguros());


                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_seguros=0;

                    if(!sauto.getText().toString().isEmpty()){
                        int_sauto=int_sauto+Integer.parseInt(sauto.getText().toString());
                        total_final_seguros=total_final_seguros+Integer.parseInt(sauto.getText().toString());
                        mDatabase.child(clave).child("seguro_auto").setValue(String.valueOf(int_sauto));
                        sauto.setText("");
                    }
                    if(!svivienda.getText().toString().isEmpty()){
                        int_svivienda=int_svivienda+Integer.parseInt(svivienda.getText().toString());
                        total_final_seguros=total_final_seguros+Integer.parseInt(svivienda.getText().toString());
                        mDatabase.child(clave).child("seguro_vivienda").setValue(String.valueOf(int_svivienda));
                        svivienda.setText("");
                    }
                    if(!svida.getText().toString().isEmpty()){
                        int_svida=int_svida+Integer.parseInt(svida.getText().toString());
                        total_final_seguros=total_final_seguros+Integer.parseInt(svida.getText().toString());
                        mDatabase.child(clave).child("seguro_vida").setValue(String.valueOf(int_svida));
                        svida.setText("");
                    }
                    if(!smedico.getText().toString().isEmpty()){
                        int_smedico=int_smedico+Integer.parseInt(smedico.getText().toString());
                        total_final_seguros=total_final_seguros+Integer.parseInt(smedico.getText().toString());
                        mDatabase.child(clave).child("seguro_medico").setValue(String.valueOf(int_smedico));
                        smedico.setText("");
                    }
                    if(!sotros.getText().toString().isEmpty()){
                        int_otrosseguros=int_otrosseguros+Integer.parseInt(sotros.getText().toString());
                        total_final_seguros=total_final_seguros+Integer.parseInt(sotros.getText().toString());
                        mDatabase.child(clave).child("otros_seguros").setValue(String.valueOf(int_otrosseguros));
                        sotros.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_seguros);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_seguros=total_final_seguros+int_totalseguro;
                    mDatabase.child(clave).child("tseguros").setValue(String.valueOf(total_final_seguros));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Seguro obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Seguro.class);
                                vaseguroauto.setText("$ "+separador(obj3.getSeguroAuto()));
                                vasegurovivienda.setText("$ "+separador(obj3.getSeguroVivienda()));
                                vasegurovida.setText("$ "+separador(obj3.getSeguroVida()));
                                vaseguromedico.setText("$ "+separador(obj3.getSeguroMedico()));
                                vaotrosseguro.setText("$ "+separador(obj3.getOtrosSeguros()));
                                totalseguros.setText("$ "+separador(obj3.getTotalSeguros()));

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
                    nuevoseguro= new Seguro();
                    nuevoseguro.setCorreo(usuario);

                    if (sauto.getText().toString().isEmpty()){
                        nuevoseguro.setSeguroAuto("0");
                    }else {
                        nuevoseguro.setSeguroAuto(sauto.getText().toString());
                    }

                    if (svivienda.getText().toString().isEmpty()){
                        nuevoseguro.setSeguroVivienda("0");
                    }else {
                        nuevoseguro.setSeguroVivienda(svivienda.getText().toString());
                    }

                    if (svida.getText().toString().isEmpty()){
                        nuevoseguro.setSeguroVida("0");
                    }else {
                        nuevoseguro.setSeguroVida(svida.getText().toString());
                    }

                    if (smedico.getText().toString().isEmpty()){
                        nuevoseguro.setSeguroMedico("0");
                    }else {
                        nuevoseguro.setSeguroMedico(smedico.getText().toString());
                    }

                    if (sotros.getText().toString().isEmpty()){
                        nuevoseguro.setOtrosSeguros("0");
                    }else {
                        nuevoseguro.setOtrosSeguros(sotros.getText().toString());
                    }

                    //suma del total inicial
                    int_sauto=Integer.parseInt(nuevoseguro.getSeguroAuto());
                    int_svivienda=Integer.parseInt(nuevoseguro.getSeguroVivienda());
                    int_svida=Integer.parseInt(nuevoseguro.getSeguroVida());
                    int_smedico=Integer.parseInt(nuevoseguro.getSeguroMedico());
                    int_otrosseguros=Integer.parseInt(nuevoseguro.getOtrosSeguros());

                    total_final_seguros=0;
                    total_final_seguros=int_totalseguro+int_sauto+int_svivienda+int_svida
                            +int_smedico+ int_otrosseguros;
                    nuevoseguro.setTotalSeguros(String.valueOf(total_final_seguros));


                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_seguros);
                    // se agrega el nuevo objeto seguro a la bbdd
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevoseguro);

                    //se realiza otra consulta para actualizar los datos
                    Query qu=mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Seguro obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Seguro.class);
                                vaseguroauto.setText("$ "+separador(obj4.getSeguroAuto()));
                                vasegurovivienda.setText("$ "+separador(obj4.getSeguroVivienda()));
                                vasegurovida.setText("$ "+separador(obj4.getSeguroVida()));
                                vaseguromedico.setText("$ "+separador(obj4.getSeguroMedico()));
                                vaotrosseguro.setText("$ "+separador(obj4.getOtrosSeguros()));
                                totalseguros.setText("$ "+separador(obj4.getTotalSeguros()));

                            }
                            //limpiar campos input
                            sauto.setText("");
                            svivienda.setText("");
                            svida.setText("");
                            smedico.setText("");
                            sotros.setText("");

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




    //método para actualizar datos presupuesto casa
    public void actualizarPresupuestoSeguros(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Seguro obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Seguro.class);

                    //cálculo del total del presupuesto Casas
                    int_sauto=Integer.parseInt(obj.getSeguroAuto());
                    int_svivienda=Integer.parseInt(obj.getSeguroVivienda());
                    int_svida=Integer.parseInt(obj.getSeguroVida());
                    int_smedico=Integer.parseInt(obj.getSeguroMedico());
                    int_otrosseguros=Integer.parseInt(obj.getOtrosSeguros());
                    int_totalseguro=Integer.parseInt(obj.getTotalSeguros());




                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!sauto.getText().toString().isEmpty()){
                        int_sauto=int_sauto-Integer.parseInt(sauto.getText().toString());
                        contador=contador+Integer.parseInt(sauto.getText().toString());
                        if (int_sauto<0){
                            valida=false;
                        }
                    }

                    if (!svivienda.getText().toString().isEmpty()){
                        int_svivienda=int_svivienda-Integer.parseInt(svivienda.getText().toString());
                        contador=contador+Integer.parseInt(svivienda.getText().toString());
                        if (int_svivienda<0){
                            valida=false;
                        }
                    }

                    if (!svida.getText().toString().isEmpty()){
                        int_svida=int_svida-Integer.parseInt(svida.getText().toString());
                        contador=contador+Integer.parseInt(svida.getText().toString());
                        if (int_svida<0){
                            valida=false;
                        }
                    }


                    if (!smedico.getText().toString().isEmpty()){
                        int_smedico=int_smedico-Integer.parseInt(smedico.getText().toString());
                        contador=contador+Integer.parseInt(smedico.getText().toString());
                        if (int_smedico<0){
                            valida=false;
                        }
                    }

                    if (!sotros.getText().toString().isEmpty()){
                        int_otrosseguros=int_otrosseguros-Integer.parseInt(sotros.getText().toString());
                        contador=contador+Integer.parseInt(sotros.getText().toString());
                        if (int_otrosseguros<0){
                            valida=false;
                        }
                    }


                    //validación de campos llenos
                    if (sauto.getText().toString().isEmpty() && svivienda.getText().toString().isEmpty()
                            && svida.getText().toString().isEmpty() && smedico.getText().toString().isEmpty()
                            && sotros.getText().toString().isEmpty()){
                        valida2=false;
                    }


                    //cálculo del total del presupuesto Casas
                    if (valida2==false){
                        Toast.makeText(Seguros.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){


                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarPresupuestoGlobal(contador);//método que resta al presupuesto globa
                        if(!sauto.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("seguro_auto").setValue(String.valueOf(int_sauto));
                            int_totalseguro=int_totalseguro-Integer.parseInt(sauto.getText().toString());
                            mDatabase.child(clave).child("tseguros").setValue(String.valueOf(int_totalseguro));
                            sauto.setText("");
                        }

                        if(!svivienda.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("seguro_vivienda").setValue(String.valueOf(int_svivienda));
                            int_totalseguro=int_totalseguro-Integer.parseInt(svivienda.getText().toString());
                            mDatabase.child(clave).child("tseguros").setValue(String.valueOf(int_totalseguro));
                            svivienda.setText("");
                        }

                        if(!svida.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("seguro_vida").setValue(String.valueOf(int_svida));
                            int_totalseguro=int_totalseguro-Integer.parseInt(svida.getText().toString());
                            mDatabase.child(clave).child("tseguros").setValue(String.valueOf(int_totalseguro));
                            svida.setText("");
                        }

                        if(!smedico.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("seguro_medico").setValue(String.valueOf(int_smedico));
                            int_totalseguro=int_totalseguro-Integer.parseInt(smedico.getText().toString());
                            mDatabase.child(clave).child("tseguros").setValue(String.valueOf(int_totalseguro));
                            smedico.setText("");
                        }

                        if(!sotros.getText().toString().isEmpty()){
                            mDatabase.child(clave).child("otros_seguros").setValue(String.valueOf(int_otrosseguros));
                            int_totalseguro=int_totalseguro-Integer.parseInt(sotros.getText().toString());
                            mDatabase.child(clave).child("tseguros").setValue(String.valueOf(int_totalseguro));
                            sotros.setText("");
                        }


                        //else if para seleccion de que servicio modificar
                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               Seguro obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Seguro.class);
                                    vaseguroauto.setText("$ "+separador(obj3.getSeguroAuto()));
                                    vasegurovivienda.setText("$ "+separador(obj3.getSeguroVivienda()));
                                    vasegurovida.setText("$ "+separador(obj3.getSeguroVida()));
                                    vaseguromedico.setText("$ "+separador(obj3.getSeguroMedico()));
                                    vaotrosseguro.setText("$ "+separador(obj3.getOtrosSeguros()));
                                    totalseguros.setText("$ "+separador(obj3.getTotalSeguros()));

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Seguros.this,"error! ingresar valor menor o igual al actual!",Toast.LENGTH_LONG).show();
                        sauto.setText("");
                        svivienda.setText("");
                        svida.setText("");
                        smedico.setText("");
                        sotros.setText("");
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
