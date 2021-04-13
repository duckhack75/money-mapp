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
import com.example.moneymapp.presupuesto.model.Prestamo;
import com.example.moneymapp.presupuesto.model.Presupuestoglobal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Prestamos extends AppCompatActivity {

    private FirebaseUser user;
    private TextView vahipoteca, vamensualidadaduto, totalprestamos, vatarjetadecredito, vaprestamo, vaotrosprestamos;
    private Boolean comprobar;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario, correo;
    private EditText hipoteca, mensualidadauto, tarjetadecredito, prestamo, otrosprestamos;
    private Prestamo nuevoprestamo;
    private int c,int_hipoteca, int_mensualidadauto, int_tarjetadecredito, int_prestamos, int_otrosprestamos,
            int_totalprestamos, total_final_prestamos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamos);

        hipoteca=(EditText)findViewById(R.id.edtHipoteca);
        mensualidadauto=(EditText)findViewById(R.id.edtMensualidadauto);
        tarjetadecredito=(EditText)findViewById(R.id.edtTarjetadecredito);
        prestamo=(EditText)findViewById(R.id.edtPrestamo);
        otrosprestamos=(EditText)findViewById(R.id.edtOtrosprestamos);
        totalprestamos=(TextView)findViewById(R.id.txvTotalprestamos);

        vahipoteca=(TextView)findViewById(R.id.txvVahipoteca);
        vamensualidadaduto=(TextView)findViewById(R.id.txvVamensualidadauto);
        vatarjetadecredito=(TextView)findViewById(R.id.txvVatarjetadecredito);
        vaprestamo=(TextView)findViewById(R.id.txvVaprestamo);
        vaotrosprestamos=(TextView)findViewById(R.id.txvVaotrosprestamos);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Prestamos");
        //acceso a la tabla Presupuesto glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("Presupuestoglobal");

        //muestra de valores actuales, usando where usuario
        Query query=mDatabase.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Prestamo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Prestamo.class);
                    vahipoteca.setText("$ "+separador(obj.getHipoteca()));
                    vamensualidadaduto.setText("$ "+separador(obj.getMensualidadAuto()));
                    vatarjetadecredito.setText("$ "+separador(obj.getTarjetaCredito()));
                    vaprestamo.setText("$ "+separador(obj.getPrestamo()));
                    vaotrosprestamos.setText("$ "+separador(obj.getOtrosPrestamos()));
                    totalprestamos.setText("$ "+separador(obj.getTotalPrestamos()));

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
    public void ingresarPresupuestoPrestamos(View view) {
        //se consulta a la bbdd para validar si existe ya el usuario
        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Prestamo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Prestamo.class);
                    vahipoteca.setText("$ "+separador(obj.getHipoteca()));
                    vamensualidadaduto.setText("$ "+separador(obj.getMensualidadAuto()));
                    vatarjetadecredito.setText("$ "+separador(obj.getTarjetaCredito()));
                    vaprestamo.setText("$ "+separador(obj.getPrestamo()));
                    vaotrosprestamos.setText("$ "+separador(obj.getOtrosPrestamos()));
                    totalprestamos.setText("$ "+separador(obj.getTotalPrestamos()));

                    //cálculo del total del presupuesto Autos
                    int_hipoteca=Integer.parseInt(obj.getHipoteca());
                    int_mensualidadauto=Integer.parseInt(obj.getMensualidadAuto());
                    int_tarjetadecredito=Integer.parseInt(obj.getTarjetaCredito());
                    int_prestamos=Integer.parseInt(obj.getPrestamo());
                    int_otrosprestamos=Integer.parseInt(obj.getOtrosPrestamos());
                    int_totalprestamos=Integer.parseInt(obj.getTotalPrestamos());


                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    total_final_prestamos=0;

                    if(!hipoteca.getText().toString().isEmpty()){
                        int_hipoteca=int_hipoteca+Integer.parseInt(hipoteca.getText().toString());
                        total_final_prestamos=total_final_prestamos+Integer.parseInt(hipoteca.getText().toString());
                        mDatabase.child(clave).child("hipoteca").setValue(String.valueOf(int_hipoteca));
                        hipoteca.setText("");
                    }
                    if(!mensualidadauto.getText().toString().isEmpty()){
                        int_mensualidadauto=int_mensualidadauto+Integer.parseInt(mensualidadauto.getText().toString());
                        total_final_prestamos=total_final_prestamos+Integer.parseInt(mensualidadauto.getText().toString());
                        mDatabase.child(clave).child("mensualidad_auto").setValue(String.valueOf(int_mensualidadauto));
                        mensualidadauto.setText("");
                    }
                    if(!tarjetadecredito.getText().toString().isEmpty()){
                        int_tarjetadecredito=int_tarjetadecredito+Integer.parseInt(tarjetadecredito.getText().toString());
                        total_final_prestamos=total_final_prestamos+Integer.parseInt(tarjetadecredito.getText().toString());
                        mDatabase.child(clave).child("tarjeta_de_credito").setValue(String.valueOf(int_tarjetadecredito));
                        tarjetadecredito.setText("");
                    }
                    if(!prestamo.getText().toString().isEmpty()){
                        int_prestamos=int_prestamos+Integer.parseInt(prestamo.getText().toString());
                        total_final_prestamos=total_final_prestamos+Integer.parseInt(prestamo.getText().toString());
                        mDatabase.child(clave).child("prestamo").setValue(String.valueOf(int_prestamos));
                        prestamo.setText("");
                    }
                    if(!otrosprestamos.getText().toString().isEmpty()){
                        int_otrosprestamos=int_otrosprestamos+Integer.parseInt(otrosprestamos.getText().toString());
                        total_final_prestamos=total_final_prestamos+Integer.parseInt(otrosprestamos.getText().toString());
                        mDatabase.child(clave).child("otros_prestamo").setValue(String.valueOf(int_otrosprestamos));
                        otrosprestamos.setText("");
                    }

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_prestamos);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_prestamos=total_final_prestamos+int_totalprestamos;
                    mDatabase.child(clave).child("tprestamos").setValue(String.valueOf(total_final_prestamos));

                    //se realiza otra consulta para actualizar los datos
                    Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Prestamo obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(Prestamo.class);
                                vahipoteca.setText("$ "+separador(obj3.getHipoteca()));
                                vamensualidadaduto.setText("$ "+separador(obj3.getMensualidadAuto()));
                                vatarjetadecredito.setText("$ "+separador(obj3.getTarjetaCredito()));
                                vaprestamo.setText("$ "+separador(obj3.getPrestamo()));
                                vaotrosprestamos.setText("$ "+separador(obj3.getOtrosPrestamos()));
                                totalprestamos.setText("$ "+separador(obj3.getTotalPrestamos()));

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
                    nuevoprestamo= new Prestamo();
                    nuevoprestamo.setCorreo(usuario);

                    if (hipoteca.getText().toString().isEmpty()){
                        nuevoprestamo.setHipoteca("0");
                    }else {
                        nuevoprestamo.setHipoteca(hipoteca.getText().toString());
                    }

                    if (mensualidadauto.getText().toString().isEmpty()){
                        nuevoprestamo.setMensualidadAuto("0");
                    }else {
                        nuevoprestamo.setMensualidadAuto(mensualidadauto.getText().toString());
                    }

                    if (tarjetadecredito.getText().toString().isEmpty()){
                        nuevoprestamo.setTarjetaCredito("0");
                    }else {
                        nuevoprestamo.setTarjetaCredito(tarjetadecredito.getText().toString());
                    }

                    if (prestamo.getText().toString().isEmpty()){
                        nuevoprestamo.setPrestamo("0");
                    }else {
                        nuevoprestamo.setPrestamo(prestamo.getText().toString());
                    }

                    if (otrosprestamos.getText().toString().isEmpty()){
                        nuevoprestamo.setOtrosPrestamos("0");
                    }else {
                        nuevoprestamo.setOtrosPrestamos(otrosprestamos.getText().toString());
                    }

                    //suma del total inicial
                    int_hipoteca=Integer.parseInt(nuevoprestamo.getHipoteca());
                    int_mensualidadauto=Integer.parseInt(nuevoprestamo.getMensualidadAuto());
                    int_tarjetadecredito=Integer.parseInt(nuevoprestamo.getTarjetaCredito());
                    int_prestamos=Integer.parseInt(nuevoprestamo.getPrestamo());
                    int_otrosprestamos=Integer.parseInt(nuevoprestamo.getOtrosPrestamos());

                    total_final_prestamos=0;
                    total_final_prestamos=int_totalprestamos+int_hipoteca+int_mensualidadauto+int_tarjetadecredito
                            +int_prestamos+ int_otrosprestamos;
                    nuevoprestamo.setTotalPrestamos(String.valueOf(total_final_prestamos));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarPresupuestoGlobal(total_final_prestamos);
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevoprestamo);

                    //se realiza otra consulta para actualizar los datos
                    Query qu=mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Prestamo obj4;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj4=datanapshot.getValue(Prestamo.class);
                                vahipoteca.setText("$ "+separador(obj4.getHipoteca()));
                                vamensualidadaduto.setText("$ "+separador(obj4.getMensualidadAuto()));
                                vatarjetadecredito.setText("$ "+separador(obj4.getTarjetaCredito()));
                                vaprestamo.setText("$ "+separador(obj4.getPrestamo()));
                                vaotrosprestamos.setText("$ "+separador(obj4.getOtrosPrestamos()));
                                totalprestamos.setText("$ "+separador(obj4.getTotalPrestamos()));

                            }
                            //limpiar campos input
                            hipoteca.setText("");
                            mensualidadauto.setText("");
                            tarjetadecredito.setText("");
                            prestamo.setText("");
                            otrosprestamos.setText("");

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


    //método para actualizar datos presupuesto auto
    public void actualizarPresupuestoPrestamos(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Prestamo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Prestamo.class);
                    //cálculo del total del presupuesto Autos

                    int_hipoteca=Integer.parseInt(obj.getHipoteca());
                    int_mensualidadauto=Integer.parseInt(obj.getMensualidadAuto());
                    int_tarjetadecredito=Integer.parseInt(obj.getTarjetaCredito());
                    int_prestamos=Integer.parseInt(obj.getPrestamo());
                    int_otrosprestamos=Integer.parseInt(obj.getOtrosPrestamos());
                    int_totalprestamos=Integer.parseInt(obj.getTotalPrestamos());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!hipoteca.getText().toString().isEmpty()){
                        int_hipoteca=int_hipoteca-Integer.parseInt(hipoteca.getText().toString());
                        contador=contador+Integer.parseInt(hipoteca.getText().toString());
                        if (int_hipoteca<0){
                            valida=false;
                        }
                    }

                    if (!mensualidadauto.getText().toString().isEmpty()){
                        int_mensualidadauto=int_mensualidadauto-Integer.parseInt(mensualidadauto.getText().toString());
                        contador=contador+Integer.parseInt(mensualidadauto.getText().toString());
                        if (int_mensualidadauto<0){
                            valida=false;
                        }
                    }

                    if (!tarjetadecredito.getText().toString().isEmpty()){
                        int_tarjetadecredito=int_tarjetadecredito-Integer.parseInt(tarjetadecredito.getText().toString());
                        contador=contador+Integer.parseInt(tarjetadecredito.getText().toString());
                        if (int_tarjetadecredito<0){
                            valida=false;
                        }
                    }

                    if (!prestamo.getText().toString().isEmpty()){
                        int_prestamos=int_prestamos-Integer.parseInt(prestamo.getText().toString());
                        contador=contador+Integer.parseInt(prestamo.getText().toString());
                        if (int_prestamos<0){
                            valida=false;
                        }
                    }

                    if (!otrosprestamos.getText().toString().isEmpty() ){
                        int_otrosprestamos=int_otrosprestamos-Integer.parseInt(otrosprestamos.getText().toString());
                        contador=contador+Integer.parseInt(otrosprestamos.getText().toString());
                        if (int_otrosprestamos<0){
                            valida=false;
                        }
                    }

                    //validación de campos llenos
                    if (hipoteca.getText().toString().isEmpty() && mensualidadauto.getText().toString().isEmpty()
                            && tarjetadecredito.getText().toString().isEmpty() && prestamo.getText().toString().isEmpty()
                            && otrosprestamos.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Prestamos.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarPresupuestoGlobal(contador);//método que resta al presupuesto global
                        if(!hipoteca.getText().toString().isEmpty()){
                            int_totalprestamos=int_totalprestamos-Integer.parseInt(hipoteca.getText().toString());
                            mDatabase.child(clave).child("tprestamos").setValue(String.valueOf(int_totalprestamos));
                            mDatabase.child(clave).child("hipoteca").setValue(String.valueOf(int_hipoteca));
                            hipoteca.setText("");
                        }

                        if(!mensualidadauto.getText().toString().isEmpty()){
                            int_totalprestamos=int_totalprestamos-Integer.parseInt(mensualidadauto.getText().toString());
                            mDatabase.child(clave).child("tprestamos").setValue(String.valueOf(int_totalprestamos));
                            mDatabase.child(clave).child("mensualidad_auto").setValue(String.valueOf(int_mensualidadauto));
                            mensualidadauto.setText("");
                        }

                        if(!tarjetadecredito.getText().toString().isEmpty()){
                            int_totalprestamos=int_totalprestamos-Integer.parseInt(tarjetadecredito.getText().toString());
                            mDatabase.child(clave).child("tprestamos").setValue(String.valueOf(int_totalprestamos));
                            mDatabase.child(clave).child("tarjeta_de_credito").setValue(String.valueOf(int_tarjetadecredito));
                            tarjetadecredito.setText("");
                        }

                        if(!prestamo.getText().toString().isEmpty()){
                            int_totalprestamos=int_totalprestamos-Integer.parseInt(prestamo.getText().toString());
                            mDatabase.child(clave).child("tprestamos").setValue(String.valueOf(int_totalprestamos));
                            mDatabase.child(clave).child("prestamo").setValue(String.valueOf(int_prestamos));
                            prestamo.setText("");
                        }

                        if(!otrosprestamos.getText().toString().isEmpty()){
                            int_totalprestamos=int_totalprestamos-Integer.parseInt(otrosprestamos.getText().toString());
                            mDatabase.child(clave).child("tprestamos").setValue(String.valueOf(int_totalprestamos));
                            mDatabase.child(clave).child("otros_prestamo").setValue(String.valueOf(int_otrosprestamos));
                            otrosprestamos.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Prestamo obj4;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj4=datanapshot.getValue(Prestamo.class);
                                    vahipoteca.setText("$ "+separador(obj4.getHipoteca()));
                                    vamensualidadaduto.setText("$ "+separador(obj4.getMensualidadAuto()));
                                    vatarjetadecredito.setText("$ "+separador(obj4.getTarjetaCredito()));
                                    vaprestamo.setText("$ "+separador(obj4.getPrestamo()));
                                    vaotrosprestamos.setText("$ "+separador(obj4.getOtrosPrestamos()));
                                    totalprestamos.setText("$ "+separador(obj4.getTotalPrestamos()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Prestamos.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        hipoteca.setText("");
                        mensualidadauto.setText("");
                        tarjetadecredito.setText("");
                        prestamo.setText("");
                        otrosprestamos.setText("");
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
