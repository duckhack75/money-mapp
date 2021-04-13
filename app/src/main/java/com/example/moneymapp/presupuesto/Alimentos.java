package com.example.moneymapp.presupuesto;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.presupuesto.model.Alimento;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Alimentos extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private TextView vasupermercado, vacomidafuera, totalalimentos, vaotrosalimentos, pruabavalor;
    private EditText supermercado, comidafuera, otrosalimentos;
    private Alimento nuevoalimento;
    private int c,int_supermercado, int_comidafuera, int_otrosalimentos,
            int_totalalimentos, total_final_alimentos;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentos);

        supermercado=(EditText)findViewById(R.id.edtSupermercado);
        comidafuera=(EditText)findViewById(R.id.edtComidaafuera);
        otrosalimentos=(EditText)findViewById(R.id.edtOtrosalimentos);
        totalalimentos=(TextView)findViewById(R.id.txvTotalAlimentos);


        //inicializamos el obj de separador de miles
       /*String mostrar=separador2("5.000");
        prueba.setText(mostrar);*/



        vasupermercado=(TextView)findViewById(R.id.txvVasupermercado);
        vacomidafuera=(TextView)findViewById(R.id.txvVacomidafuera);
        vaotrosalimentos=(TextView)findViewById(R.id.txvVaotrosalimentos);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Alimento");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");
        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Alimento obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Alimento.class);
                    vasupermercado.setText("$ "+separador(obj.getSupermercado()));
                    vacomidafuera.setText("$ "+separador(obj.getComidaFuera()));
                    vaotrosalimentos.setText("$ "+separador(obj.getOtrosAlimentos()));
                    totalalimentos.setText("$ "+separador(obj.getTotalAlimentos()));
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

    //método que quita los separadores de miles
    public String separador2(String num){
        String numfinal=num.replace(".","");
        int x=Integer.parseInt(numfinal);
        return numfinal;
    }




    //método para ingresar presupuesto
    public void ingresarPresupuestoAlimentos(View view){
        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;

                Alimento obj;

                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Alimento.class);
                    /*vasupermercado.setText("$ "+obj.getSupermercado());
                    vacomidafuera.setText("$ "+obj.getComidafuera());
                    vaotrosalimentos.setText("$ "+obj.getOtrosalimentos());
                    totalalimentos.setText("$"+obj.getTalimentos()); */

                    //cálculo del total del presupuesto Autos
                    int_supermercado=Integer.parseInt(obj.getSupermercado());
                    int_comidafuera=Integer.parseInt(obj.getComidaFuera());
                    int_otrosalimentos=Integer.parseInt(obj.getOtrosAlimentos());
                    int_totalalimentos=Integer.parseInt(obj.getTotalAlimentos());


                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_alimentos=0;

                    if(!supermercado.getText().toString().isEmpty()){
                        int_supermercado=int_supermercado+Integer.parseInt(supermercado.getText().toString());
                        total_final_alimentos=total_final_alimentos+Integer.parseInt(supermercado.getText().toString());
                        mDatabase.child(clave).child("supermercado").setValue(String.valueOf(int_supermercado));
                        supermercado.setText("");
                    }
                    if(!comidafuera.getText().toString().isEmpty()){
                        int_comidafuera=int_comidafuera+Integer.parseInt(comidafuera.getText().toString());
                        total_final_alimentos=total_final_alimentos+Integer.parseInt(comidafuera.getText().toString());
                        mDatabase.child(clave).child("comidafuera").setValue(String.valueOf(int_comidafuera));
                        comidafuera.setText("");
                    }
                    if(!otrosalimentos.getText().toString().isEmpty()){
                        int_otrosalimentos=int_otrosalimentos+Integer.parseInt(otrosalimentos.getText().toString());
                        total_final_alimentos=total_final_alimentos+Integer.parseInt(otrosalimentos.getText().toString());
                        mDatabase.child(clave).child("otrosalimentos").setValue(String.valueOf(int_otrosalimentos));
                        otrosalimentos.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_alimentos);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_alimentos=total_final_alimentos+int_totalalimentos;
                    mDatabase.child(clave).child("talimentos").setValue(String.valueOf(total_final_alimentos));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Alimento obj3=new Alimento();
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Alimento.class);
                                vasupermercado.setText("$ "+separador(obj3.getSupermercado()));
                                vacomidafuera.setText("$ "+separador(obj3.getComidaFuera()));
                                vaotrosalimentos.setText("$ "+separador(obj3.getOtrosAlimentos()));
                                totalalimentos.setText("$ "+separador(obj3.getTotalAlimentos()));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if(c!=0){

                }else{
                    //creo el auto
                    nuevoalimento= new Alimento();
                    nuevoalimento.setCorreo(usuario);

                    if (supermercado.getText().toString().isEmpty()){
                        nuevoalimento.setSupermercado("0");
                    }else {
                        nuevoalimento.setSupermercado(supermercado.getText().toString());
                    }

                    if (comidafuera.getText().toString().isEmpty()){
                        nuevoalimento.setComidaFuera("0");
                    }else {
                        nuevoalimento.setComidaFuera(comidafuera.getText().toString());
                    }

                    if (otrosalimentos.getText().toString().isEmpty()){
                        nuevoalimento.setOtrosAlimentos("0");
                    }else {
                        nuevoalimento.setOtrosAlimentos(otrosalimentos.getText().toString());
                    }


                    //suma del total inicial
                    int_supermercado=Integer.parseInt(nuevoalimento.getSupermercado());
                    int_comidafuera=Integer.parseInt(nuevoalimento.getComidaFuera());
                    int_otrosalimentos=Integer.parseInt(nuevoalimento.getOtrosAlimentos());

                    total_final_alimentos=0;
                    total_final_alimentos=int_totalalimentos+int_supermercado+int_comidafuera+int_otrosalimentos;
                    nuevoalimento.setTotalAlimentos(String.valueOf(total_final_alimentos));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_alimentos);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevoalimento);

                    //se realiza otra consulta para actualizar los datos
                    Query qu=mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Alimento obj4=new Alimento();
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Alimento.class);
                                vasupermercado.setText("$ "+separador(obj4.getSupermercado()));
                                vacomidafuera.setText("$ "+separador(obj4.getComidaFuera()));
                                vaotrosalimentos.setText("$ "+separador(obj4.getOtrosAlimentos()));
                                totalalimentos.setText("$ "+separador(obj4.getTotalAlimentos()));

                            }
                            //limpiar campos input
                            supermercado.setText("");
                            comidafuera.setText("");
                            otrosalimentos.setText("");
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
    public void actualizarPresupuestoAlimentos(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Alimento obj=new Alimento();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Alimento.class);
                    //cálculo del total del presupuesto Autos
                    int_supermercado=Integer.parseInt(obj.getSupermercado());
                    int_comidafuera=Integer.parseInt(obj.getComidaFuera());
                    int_otrosalimentos=Integer.parseInt(obj.getOtrosAlimentos());
                    int_totalalimentos=Integer.parseInt(obj.getTotalAlimentos());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!supermercado.getText().toString().isEmpty()){
                        int_supermercado=int_supermercado-Integer.parseInt(supermercado.getText().toString());
                        contador=contador+Integer.parseInt(supermercado.getText().toString());
                        if (int_supermercado<0){
                            valida=false;
                        }
                    }

                    if (!comidafuera.getText().toString().isEmpty()){
                        int_comidafuera=int_comidafuera-Integer.parseInt(comidafuera.getText().toString());
                        contador=contador+Integer.parseInt(comidafuera.getText().toString());
                        if (int_comidafuera<0){
                            valida=false;
                        }
                    }

                    if (!otrosalimentos.getText().toString().isEmpty()){
                        int_otrosalimentos=int_otrosalimentos-Integer.parseInt(otrosalimentos.getText().toString());
                        contador=contador+Integer.parseInt(otrosalimentos.getText().toString());
                        if (int_otrosalimentos<0){
                            valida=false;
                        }
                    }



                    //validación de campos llenos
                    if (supermercado.getText().toString().isEmpty() && comidafuera.getText().toString().isEmpty()
                            && otrosalimentos.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Alimentos.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarPresupuestoGlobal(contador);//método que resta al presupuesto globa
                        if(!supermercado.getText().toString().isEmpty()){
                            int_totalalimentos=int_totalalimentos-Integer.parseInt(supermercado.getText().toString());
                            mDatabase.child(clave).child("talimentos").setValue(String.valueOf(int_totalalimentos));
                            mDatabase.child(clave).child("supermercado").setValue(String.valueOf(int_supermercado));
                            supermercado.setText("");
                        }

                        if(!comidafuera.getText().toString().isEmpty()){
                            int_totalalimentos=int_totalalimentos-Integer.parseInt(comidafuera.getText().toString());
                            mDatabase.child(clave).child("talimentos").setValue(String.valueOf(int_totalalimentos));
                            mDatabase.child(clave).child("comidafuera").setValue(String.valueOf(int_comidafuera));
                            comidafuera.setText("");
                        }


                        if(!otrosalimentos.getText().toString().isEmpty()){
                            int_totalalimentos=int_totalalimentos-Integer.parseInt(otrosalimentos.getText().toString());
                            mDatabase.child(clave).child("talimentos").setValue(String.valueOf(int_totalalimentos));
                            mDatabase.child(clave).child("otrosalimentos").setValue(String.valueOf(int_otrosalimentos));
                            otrosalimentos.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Alimento obj3=new Alimento();
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Alimento.class);
                                    vasupermercado.setText("$ "+separador(obj3.getSupermercado()));
                                    vacomidafuera.setText("$ "+separador(obj3.getComidaFuera()));
                                    vaotrosalimentos.setText("$ "+separador(obj3.getOtrosAlimentos()));
                                    totalalimentos.setText("$ "+separador(obj3.getTotalAlimentos()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Alimentos.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        supermercado.setText("");
                        comidafuera.setText("");
                        otrosalimentos.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
