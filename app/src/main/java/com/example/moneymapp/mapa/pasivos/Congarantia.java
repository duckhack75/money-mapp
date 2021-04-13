package com.example.moneymapp.mapa.pasivos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.pasivos.model.Garantia;
import com.example.moneymapp.mapa.pasivos.model.Pasivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Congarantia extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vachipotecario, vacautomotriz, vapgarantia,vaempeno, totalgarantia;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText chipotecario, cautomotriz, pgarantia, empeno;
    private Garantia nuevogarantia;
    private int c, int_chipotecario, int_cautomotriz, int_pgarantia,int_empeno,
            int_totalgarantia, total_final_garantia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congarantia);


        chipotecario = (EditText) findViewById(R.id.edtGcreditohipotecario);
        cautomotriz = (EditText) findViewById(R.id.edtGcreditoautomotriz);
        pgarantia = (EditText) findViewById(R.id.edtGprestamocongarantia);
        empeno = (EditText) findViewById(R.id.edtGempeno);
        totalgarantia = (TextView) findViewById(R.id.txvTotalGarantia);

        vachipotecario = (TextView) findViewById(R.id.txvVGcreditohipotecario);
        vacautomotriz = (TextView) findViewById(R.id.txvVGcreditoautomotriz);
        vapgarantia = (TextView) findViewById(R.id.txvVGprestamocongarantia);
        vaempeno = (TextView) findViewById(R.id.txvVGempeno);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario = user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Pasivoscongarantia");
        //acceso a la tabla Presupuesto activo glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Mpasivos");
        //muestra de valores actuales, usando where usuario
        Query query = mDatabase.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Garantia obj;
                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    obj = datanapshot.getValue(Garantia.class);
                    vachipotecario.setText("$ " + separador(obj.getCreditoHipotecario()));
                    vacautomotriz.setText("$ " + separador(obj.getCreditoAutomotriz()));
                    vapgarantia.setText("$ " + separador(obj.getPrestamoConGarantia()));
                    vaempeno.setText("$ " + separador(obj.getEmpeno()));
                    totalgarantia.setText("$ " + separador(obj.getTotalGarantia()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    //método que separa por miles los números
    public String separador(String num) {
        int x = Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal = str.replace(",", ".");
        return numfinal;
    }


    //método para ingresar presupuesto
    public void ingresarTotalGarantia(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q = mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c = 0;
                Garantia obj;

                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(Garantia.class);
                    //cálculo del total del presupuesto Autos
                    int_chipotecario = Integer.parseInt(obj.getCreditoHipotecario());
                    int_cautomotriz = Integer.parseInt(obj.getCreditoAutomotriz());
                    int_pgarantia = Integer.parseInt(obj.getPrestamoConGarantia());
                    int_empeno = Integer.parseInt(obj.getEmpeno());
                    int_totalgarantia = Integer.parseInt(obj.getTotalGarantia());


                    //obtención de la "primary key"
                    String clave = datanapshot.getKey();
                    total_final_garantia = 0;

                    if (!chipotecario.getText().toString().isEmpty()) {
                        int_chipotecario = int_chipotecario + Integer.parseInt(chipotecario.getText().toString());
                        total_final_garantia = total_final_garantia + Integer.parseInt(chipotecario.getText().toString());
                        mDatabase.child(clave).child("chipotecario").setValue(String.valueOf(int_chipotecario));
                        chipotecario.setText("");
                    }
                    if (!cautomotriz.getText().toString().isEmpty()) {
                        int_cautomotriz = int_cautomotriz + Integer.parseInt(cautomotriz.getText().toString());
                        total_final_garantia = total_final_garantia + Integer.parseInt(cautomotriz.getText().toString());
                        mDatabase.child(clave).child("cautomotriz").setValue(String.valueOf(int_cautomotriz));
                        cautomotriz.setText("");
                    }
                    if (!pgarantia.getText().toString().isEmpty()) {
                        int_pgarantia = int_pgarantia + Integer.parseInt(pgarantia.getText().toString());
                        total_final_garantia = total_final_garantia + Integer.parseInt(pgarantia.getText().toString());
                        mDatabase.child(clave).child("pgarantia").setValue(String.valueOf(int_pgarantia));
                        pgarantia.setText("");
                    }
                    if (!empeno.getText().toString().isEmpty()) {
                        int_empeno = int_empeno + Integer.parseInt(empeno.getText().toString());
                        total_final_garantia = total_final_garantia + Integer.parseInt(empeno.getText().toString());
                        mDatabase.child(clave).child("empeno").setValue(String.valueOf(int_empeno));
                        empeno.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalPasivos(total_final_garantia);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_garantia = total_final_garantia + int_totalgarantia;
                    mDatabase.child(clave).child("tgarantia").setValue(String.valueOf(total_final_garantia));

                    //se realiza otra consulta para actualizar los datos
                    Query q = mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Garantia obj3;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj3 = datanapshot.getValue(Garantia.class);
                                vachipotecario.setText("$ " + separador(obj3.getCreditoHipotecario()));
                                vacautomotriz.setText("$ " + separador(obj3.getCreditoAutomotriz()));
                                vapgarantia.setText("$ " + separador(obj3.getPrestamoConGarantia()));
                                vaempeno.setText("$ " + separador(obj3.getEmpeno()));
                                totalgarantia.setText("$ " + separador(obj3.getTotalGarantia()));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if (c != 0) {
                    //actualización del presupuesto del usuario


                } else {
                    //creo el auto
                    nuevogarantia = new Garantia();
                    nuevogarantia.setCorreo(usuario);

                    if (chipotecario.getText().toString().isEmpty()) {
                        nuevogarantia.setCreditoHipotecario("0");
                    } else {
                        nuevogarantia.setCreditoHipotecario(chipotecario.getText().toString());
                    }

                    if (cautomotriz.getText().toString().isEmpty()) {
                        nuevogarantia.setCreditoAutomotriz("0");
                    } else {
                        nuevogarantia.setCreditoAutomotriz(cautomotriz.getText().toString());
                    }

                    if (pgarantia.getText().toString().isEmpty()) {
                        nuevogarantia.setPrestamoConGarantia("0");
                    } else {
                        nuevogarantia.setPrestamoConGarantia(pgarantia.getText().toString());
                    }

                    if (empeno.getText().toString().isEmpty()) {
                        nuevogarantia.setEmpeno("0");
                    } else {
                        nuevogarantia.setPrestamoConGarantia(empeno.getText().toString());
                    }

                    //suma del total inicial
                    int_chipotecario = Integer.parseInt(nuevogarantia.getCreditoHipotecario());
                    int_cautomotriz = Integer.parseInt(nuevogarantia.getCreditoAutomotriz());
                    int_pgarantia = Integer.parseInt(nuevogarantia.getPrestamoConGarantia());
                    int_empeno = Integer.parseInt(nuevogarantia.getEmpeno());

                    total_final_garantia = 0;
                    total_final_garantia = int_totalgarantia + int_chipotecario + int_cautomotriz + int_pgarantia+int_empeno;
                    nuevogarantia.setTotalGarantia(String.valueOf(total_final_garantia));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalPasivos(total_final_garantia);
                    String clave = mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevogarantia);

                    //se realiza otra consulta para actualizar los datos
                    Query qu = mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Garantia obj4;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj4 = datanapshot.getValue(Garantia.class);
                                vachipotecario.setText("$ " + separador(obj4.getCreditoHipotecario()));
                                vacautomotriz.setText("$ " + separador(obj4.getCreditoAutomotriz()));
                                vapgarantia.setText("$ " + separador(obj4.getPrestamoConGarantia()));
                                vaempeno.setText("$ " + separador(obj4.getEmpeno()));
                                totalgarantia.setText("$ " + separador(obj4.getTotalGarantia()));

                            }
                            //limpiar campos input
                            chipotecario.setText("");
                            cautomotriz.setText("");
                            pgarantia.setText("");
                            empeno.setText("");
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
    public void sumarTotalPasivos(final int v){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Pasivo m =datanapshot.getValue(Pasivo.class);
                    String clave=datanapshot.getKey();
                    z=z+Integer.parseInt(m.getPasivosTotal());
                    //modificacion
                    tDatabase.child(clave).child("pasivo_total").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //método para actualizar datos presupuesto auto
    public void actualizarTotalGarantia(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Garantia obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(Garantia.class);
                    //cálculo del total del presupuesto Autos
                    int_chipotecario = Integer.parseInt(obj.getCreditoHipotecario());
                    int_cautomotriz = Integer.parseInt(obj.getCreditoAutomotriz());
                    int_pgarantia = Integer.parseInt(obj.getPrestamoConGarantia());
                    int_empeno = Integer.parseInt(obj.getEmpeno());
                    int_totalgarantia = Integer.parseInt(obj.getTotalGarantia());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!chipotecario.getText().toString().isEmpty()){
                        int_chipotecario=int_chipotecario-Integer.parseInt(chipotecario.getText().toString());
                        contador=contador+Integer.parseInt(chipotecario.getText().toString());
                        if (int_chipotecario<0){
                            valida=false;
                        }
                    }

                    if (!cautomotriz.getText().toString().isEmpty()){
                        int_cautomotriz=int_cautomotriz-Integer.parseInt(cautomotriz.getText().toString());
                        contador=contador+Integer.parseInt(cautomotriz.getText().toString());
                        if (int_cautomotriz<0){
                            valida=false;
                        }
                    }

                    if (!pgarantia.getText().toString().isEmpty()){
                        int_pgarantia=int_pgarantia-Integer.parseInt(pgarantia.getText().toString());
                        contador=contador+Integer.parseInt(pgarantia.getText().toString());
                        if (int_pgarantia<0){
                            valida=false;
                        }
                    }

                    if (!empeno.getText().toString().isEmpty()){
                        int_empeno=int_empeno-Integer.parseInt(empeno.getText().toString());
                        contador=contador+Integer.parseInt(empeno.getText().toString());
                        if (int_empeno<0){
                            valida=false;
                        }
                    }

                    //validación de campos llenos
                    if (chipotecario.getText().toString().isEmpty() && cautomotriz.getText().toString().isEmpty()
                            && pgarantia.getText().toString().isEmpty()&&empeno.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Congarantia.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarTotalPasivos(contador);//método que resta al presupuesto globa
                        if(!chipotecario.getText().toString().isEmpty()){
                            int_totalgarantia=int_totalgarantia-Integer.parseInt(chipotecario.getText().toString());
                            mDatabase.child(clave).child("tgarantia").setValue(String.valueOf(int_totalgarantia));
                            mDatabase.child(clave).child("chipotecario").setValue(String.valueOf(int_chipotecario));
                            chipotecario.setText("");
                        }

                        if(!cautomotriz.getText().toString().isEmpty()){
                            int_totalgarantia=int_totalgarantia-Integer.parseInt(cautomotriz.getText().toString());
                            mDatabase.child(clave).child("tgarantia").setValue(String.valueOf(int_totalgarantia));
                            mDatabase.child(clave).child("cautomotriz").setValue(String.valueOf(int_cautomotriz));
                            cautomotriz.setText("");
                        }


                        if(!pgarantia.getText().toString().isEmpty()){
                            int_totalgarantia=int_totalgarantia-Integer.parseInt(pgarantia.getText().toString());
                            mDatabase.child(clave).child("tgarantia").setValue(String.valueOf(int_totalgarantia));
                            mDatabase.child(clave).child("pgarantia").setValue(String.valueOf(int_pgarantia));
                            pgarantia.setText("");
                        }

                        if(!empeno.getText().toString().isEmpty()){
                            int_totalgarantia=int_totalgarantia-Integer.parseInt(empeno.getText().toString());
                            mDatabase.child(clave).child("tgarantia").setValue(String.valueOf(int_totalgarantia));
                            mDatabase.child(clave).child("empeno").setValue(String.valueOf(int_empeno));
                            empeno.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Garantia obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3 = datanapshot.getValue(Garantia.class);
                                    vachipotecario.setText("$ " + separador(obj3.getCreditoHipotecario()));
                                    vacautomotriz.setText("$ " + separador(obj3.getCreditoAutomotriz()));
                                    vapgarantia.setText("$ " + separador(obj3.getPrestamoConGarantia()));
                                    vaempeno.setText("$ " + separador(obj3.getEmpeno()));
                                    totalgarantia.setText("$ " + separador(obj3.getTotalGarantia()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Congarantia.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        //limpiar campos input
                        chipotecario.setText("");
                        cautomotriz.setText("");
                        pgarantia.setText("");
                        empeno.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //método para sumar los valores ingresados por cada item al presupuesto global
    public void restarTotalPasivos(final int n ){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Pasivo obj7 =datanapshot.getValue(Pasivo.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getPasivosTotal())-y;
                    //modificacion
                    tDatabase.child(clave).child("pasivo_total").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    //método para volver a activos
    public void iraMPasivos(View v){
        Intent irapasivos = new Intent(this, Pasivvos.class);
        startActivity(irapasivos);
    }


}
