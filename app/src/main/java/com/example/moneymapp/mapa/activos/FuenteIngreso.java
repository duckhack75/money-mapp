package com.example.moneymapp.mapa.activos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.mapa.activos.model.Activo;
import com.example.moneymapp.mapa.activos.model.FuenteIngresos;
import com.example.moneymapp.presupuesto.Presupuesto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FuenteIngreso extends AppCompatActivity {

    private FirebaseUser user;
    private TextView txvValorActualTrabajo, txvValorActualNegocio, txvValorActualOtrosIngresos,
            txvTotalFuenteIngresos;
    private DatabaseReference fuenteIngresosDatabaseReference, activosDatabaseReference;
    private String usuario;
    private EditText edtValorTrabajo, edtValorNegocio, edtValorOtrosFuentesIngresos;
    private FuenteIngresos nuevoFuenteIngreso;
    private boolean validarFuenteIngreso, validaCamposLlenos, validaValor;
    private int  valorTrabajo, valorNegocio, valorOtrosFuenteIngresos,
            valorTotalFuenteIngresos, valorTotalFinalFuenteIngresos, totalRestaPresupuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingresos);


        edtValorTrabajo =(EditText)findViewById(R.id.edtValorTrabajo);
        edtValorNegocio =(EditText)findViewById(R.id.edtValorNegocio);
        edtValorOtrosFuentesIngresos =(EditText)findViewById(R.id.edtValorOtrosFuentesIngresos);
        txvTotalFuenteIngresos =(TextView)findViewById(R.id.txvTotalFuenteIngresos);

        //inicializamos el obj de separador de miles
       /*String mostrar=separador2("5.000");
        prueba.setText(mostrar);*/


        txvValorActualTrabajo =(TextView)findViewById(R.id.txvValorActualTrabajo);
        txvValorActualNegocio =(TextView)findViewById(R.id.txvValorActualNegocio);
        txvValorActualOtrosIngresos =(TextView)findViewById(R.id.txvValorActualOtrosIngresos);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        fuenteIngresosDatabaseReference = FirebaseDatabase.getInstance().getReference
                ("fuentesIngresos");
        activosDatabaseReference = FirebaseDatabase.getInstance().getReference("activos");


        mostrarValoresActuales();

    }

    private void mostrarValoresActuales() {
        Query query= fuenteIngresosDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FuenteIngresos fueteIngresos;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    fueteIngresos=datanapshot.getValue(FuenteIngresos.class);
                    txvValorActualTrabajo.setText("$ "+ separadorMiles(fueteIngresos.getTrabajo()));
                    txvValorActualNegocio.setText("$ "+ separadorMiles(fueteIngresos.getNegocio()));
                    txvValorActualOtrosIngresos.setText("$ "+ separadorMiles(fueteIngresos.getOtrosFuentesIngresos()));
                    txvTotalFuenteIngresos.setText("$ "+ separadorMiles(fueteIngresos.getTotalFuentesIngresos()));
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void ingresarTotalFuentesIngresos(View view){

        Query query= fuenteIngresosDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               FuenteIngresos obj;

                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    validarFuenteIngreso=true;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(FuenteIngresos.class);
                    //cálculo del total del presupuesto Autos
                    valorTrabajo =Integer.parseInt(obj.getTrabajo());
                    valorNegocio =Integer.parseInt(obj.getNegocio());
                    valorOtrosFuenteIngresos =Integer.parseInt(obj.getOtrosFuentesIngresos());
                    valorTotalFuenteIngresos =Integer.parseInt(obj.getTotalFuentesIngresos());


                    //obtención de la "primary key"
                    String clave=datanapshot.getKey();
                    valorTotalFinalFuenteIngresos =0;

                    if(!edtValorTrabajo.getText().toString().isEmpty()){
                        valorTrabajo = valorTrabajo +Integer.parseInt(edtValorTrabajo.getText().toString());
                        valorTotalFinalFuenteIngresos = valorTotalFinalFuenteIngresos +Integer.parseInt(edtValorTrabajo.getText().toString());
                        fuenteIngresosDatabaseReference.child(clave).child("trabajo").setValue(String.valueOf(valorTrabajo));
                        edtValorTrabajo.setText("");
                    }
                    if(!edtValorNegocio.getText().toString().isEmpty()){
                        valorNegocio = valorNegocio +Integer.parseInt(edtValorNegocio.getText().toString());
                        valorTotalFinalFuenteIngresos = valorTotalFinalFuenteIngresos +Integer.parseInt(edtValorNegocio.getText().toString());
                        fuenteIngresosDatabaseReference.child(clave).child("negocio").setValue(String.valueOf(valorNegocio));
                        edtValorNegocio.setText("");
                    }
                    if(!edtValorOtrosFuentesIngresos.getText().toString().isEmpty()){
                        valorOtrosFuenteIngresos = valorOtrosFuenteIngresos +Integer.parseInt(edtValorOtrosFuentesIngresos.getText().toString());
                        valorTotalFinalFuenteIngresos = valorTotalFinalFuenteIngresos +Integer.parseInt(edtValorOtrosFuentesIngresos.getText().toString());
                        fuenteIngresosDatabaseReference.child(clave).child("otrosIngresos").setValue(String.valueOf(valorOtrosFuenteIngresos));
                        edtValorOtrosFuentesIngresos.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalActivos(valorTotalFinalFuenteIngresos);
                    //ingreso del total presupuesto auto final a la bbdd
                    valorTotalFinalFuenteIngresos = valorTotalFinalFuenteIngresos + valorTotalFuenteIngresos;
                    fuenteIngresosDatabaseReference.child(clave).child("totalFuentesIngresos").setValue(String.valueOf(valorTotalFinalFuenteIngresos));

                    //se realiza otra consulta para actualizar los datos
                    Query q= fuenteIngresosDatabaseReference.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FuenteIngresos obj3;
                            for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                obj3=datanapshot.getValue(FuenteIngresos.class);
                                txvValorActualTrabajo.setText("$ "+ separadorMiles(obj3.getTrabajo()));
                                txvValorActualNegocio.setText("$ "+ separadorMiles(obj3.getNegocio()));
                                txvValorActualOtrosIngresos.setText("$ "+ separadorMiles(obj3.getOtrosFuentesIngresos()));
                                txvTotalFuenteIngresos.setText("$ "+ separadorMiles(obj3.getTotalFuentesIngresos()));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                if(validarFuenteIngreso==false){
                    nuevoFuenteIngreso = new FuenteIngresos();
                    nuevoFuenteIngreso.setCorreo(usuario);

                    if (edtValorTrabajo.getText().toString().isEmpty()){
                        nuevoFuenteIngreso.setTrabajo("0");
                    }else {
                        nuevoFuenteIngreso.setTrabajo(edtValorTrabajo.getText().toString());
                    }

                    if (edtValorNegocio.getText().toString().isEmpty()){
                        nuevoFuenteIngreso.setNegocio("0");
                    }else {
                        nuevoFuenteIngreso.setNegocio(edtValorNegocio.getText().toString());
                    }

                    if (edtValorOtrosFuentesIngresos.getText().toString().isEmpty()){
                        nuevoFuenteIngreso.setOtrosFuentesIngresos("0");
                    }else {
                        nuevoFuenteIngreso.setOtrosFuentesIngresos(edtValorOtrosFuentesIngresos.getText().toString());
                    }


                    //suma del total inicial
                    valorTrabajo =Integer.parseInt(nuevoFuenteIngreso.getTrabajo());
                    valorNegocio =Integer.parseInt(nuevoFuenteIngreso.getNegocio());
                    valorOtrosFuenteIngresos =Integer.parseInt(nuevoFuenteIngreso.
                            getOtrosFuentesIngresos());

                    valorTotalFinalFuenteIngresos =0;
                    valorTotalFinalFuenteIngresos = valorTotalFuenteIngresos + valorTrabajo +
                            valorNegocio + valorOtrosFuenteIngresos;
                    nuevoFuenteIngreso.setTotalFuentesIngresos(String.valueOf
                            (valorTotalFinalFuenteIngresos));

                    sumarTotalActivos(valorTotalFinalFuenteIngresos);

                    String clave= fuenteIngresosDatabaseReference.push().getKey();
                    fuenteIngresosDatabaseReference.child(clave).setValue(nuevoFuenteIngreso);

                    actualizarFuentesIngresos();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void actualizarFuentesIngresos() {
        Query query= fuenteIngresosDatabaseReference.orderByChild("correo").equalTo(usuario);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FuenteIngresos fuenteIngreso;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    fuenteIngreso=datanapshot.getValue(FuenteIngresos.class);
                    txvValorActualTrabajo.setText("$ "+ separadorMiles(fuenteIngreso.getTrabajo()));
                    txvValorActualNegocio.setText("$ "+ separadorMiles(fuenteIngreso.getNegocio()));
                    txvValorActualOtrosIngresos.setText("$ "+ separadorMiles(fuenteIngreso.getOtrosFuentesIngresos()));
                    txvTotalFuenteIngresos.setText("$ "+ separadorMiles(fuenteIngreso.getTotalFuentesIngresos()));

                }


                edtValorTrabajo.setText("");
                edtValorNegocio.setText("");
                edtValorOtrosFuentesIngresos.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sumarTotalActivos(final int valorTotalFinalFuenteIngresos){
        Query query= activosDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nuevoTotalFuenteingresos=valorTotalFinalFuenteIngresos;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo activo =datanapshot.getValue(Activo.class);
                    String clave=datanapshot.getKey();
                    nuevoTotalFuenteingresos=nuevoTotalFuenteingresos+Integer.parseInt
                            (activo.getActivosTotal());

                    activosDatabaseReference.child(clave).child("activosTotal").setValue
                            (String.valueOf(nuevoTotalFuenteingresos));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void actualizarTotalFuentesIngresos(final View view){

        Query query= fuenteIngresosDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FuenteIngresos fuenteIngreso;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){

                    fuenteIngreso=datanapshot.getValue(FuenteIngresos.class);
                    valorTrabajo =Integer.parseInt(fuenteIngreso.getTrabajo());
                    valorNegocio =Integer.parseInt(fuenteIngreso.getNegocio());
                    valorOtrosFuenteIngresos =Integer.parseInt(fuenteIngreso.
                            getOtrosFuentesIngresos());
                    valorTotalFuenteIngresos =Integer.parseInt(fuenteIngreso.
                            getTotalFuentesIngresos());




                    //validación si todos los campos están llenos
                    //totalRestaPresupuesto para sumar lo que se resta al presupuesto global

                    totalRestaPresupuesto=sacarTotalRestaPresupuesto(valorTrabajo,valorNegocio,valorOtrosFuenteIngresos,valorTotalFuenteIngresos);


                    validaCamposLlenos=validaCamposLlenos();
                    validaValor=validaValores(valorTrabajo,valorNegocio,valorOtrosFuenteIngresos);

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (validaCamposLlenos==false){
                        Toast.makeText(FuenteIngreso.this,"error! debe llenar 1 o más " +
                                "campos!",Toast.LENGTH_LONG).show();
                    }else if(validaValor==false){
                        Toast.makeText(FuenteIngreso.this,"error! debe ingresar valores validos" +
                                " positivos",Toast.LENGTH_LONG).show();
                        edtValorTrabajo.setText("");
                        edtValorNegocio.setText("");
                        edtValorOtrosFuentesIngresos.setText("");
                    }else{
                        String clave=datanapshot.getKey();
                        restarActivosTotal(totalRestaPresupuesto);
                        if(!edtValorTrabajo.getText().toString().isEmpty()){
                            valorTotalFuenteIngresos = valorTotalFuenteIngresos -Integer.parseInt(edtValorTrabajo.getText().toString());
                            fuenteIngresosDatabaseReference.child(clave).child("tfingresos").setValue(String.valueOf(valorTotalFuenteIngresos));
                            fuenteIngresosDatabaseReference.child(clave).child("trabajo").setValue(String.valueOf(valorTrabajo));
                            edtValorTrabajo.setText("");
                        }

                        if(!edtValorNegocio.getText().toString().isEmpty()){
                            valorTotalFuenteIngresos = valorTotalFuenteIngresos -Integer.parseInt(edtValorNegocio.getText().toString());
                            fuenteIngresosDatabaseReference.child(clave).child("totalFuentesIngresos").setValue(String.valueOf(valorTotalFuenteIngresos));
                            fuenteIngresosDatabaseReference.child(clave).child("negocio").setValue(String.valueOf(valorNegocio));
                            edtValorNegocio.setText("");
                        }


                        if(!edtValorOtrosFuentesIngresos.getText().toString().isEmpty()){
                            valorTotalFuenteIngresos = valorTotalFuenteIngresos -Integer.parseInt(edtValorOtrosFuentesIngresos.getText().toString());
                            fuenteIngresosDatabaseReference.child(clave).child("totalFuentesIngresos").setValue(String.valueOf(valorTotalFuenteIngresos));
                            fuenteIngresosDatabaseReference.child(clave).child("otrosFuentesIngresos").setValue(String.valueOf(valorOtrosFuenteIngresos));
                            edtValorOtrosFuentesIngresos.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q= fuenteIngresosDatabaseReference.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FuenteIngresos obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(FuenteIngresos.class);
                                    txvValorActualTrabajo.setText("$ "+ separadorMiles(obj3.getTrabajo()));
                                    txvValorActualNegocio.setText("$ "+ separadorMiles(obj3.getNegocio()));
                                    txvValorActualOtrosIngresos.setText("$ "+ separadorMiles(obj3.getOtrosFuentesIngresos()));
                                    txvTotalFuenteIngresos.setText("$ "+ separadorMiles(obj3.getTotalFuentesIngresos()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private int sacarTotalRestaPresupuesto(int valorTrabajo, int valorNegocio, int valorOtrosFuenteIngresos, int x) {
        if (!edtValorTrabajo.getText().toString().isEmpty()){
            valorTrabajo = valorTrabajo -Integer.parseInt(edtValorTrabajo.getText().toString());
            totalRestaPresupuesto=totalRestaPresupuesto+Integer.parseInt(edtValorTrabajo.getText().toString());

        }

        if (!edtValorNegocio.getText().toString().isEmpty()){
            valorNegocio = valorNegocio -Integer.parseInt(edtValorNegocio.getText().toString());
            totalRestaPresupuesto=totalRestaPresupuesto+Integer.parseInt(edtValorNegocio.getText().toString());

        }

        if (!edtValorOtrosFuentesIngresos.getText().toString().isEmpty()){
            valorOtrosFuenteIngresos = valorOtrosFuenteIngresos -Integer.parseInt(edtValorOtrosFuentesIngresos.getText().toString());
            totalRestaPresupuesto=totalRestaPresupuesto+Integer.parseInt(edtValorOtrosFuentesIngresos.getText().toString());
        }
        return totalRestaPresupuesto;
    }

    private int totalRestaPresupuesto() {
            return 4;
    }

    private boolean validaValores(int valorTrabajo, int valorNegocio, int valorOtrosFuenteIngresos) {
        if (!edtValorTrabajo.getText().toString().isEmpty()){
            if (valorTrabajo < 0) {
                return false;
            }else {
                return true;
            }
        }
        else if (!edtValorNegocio.getText().toString().isEmpty()) {
            if (valorNegocio < 0) {
                return false;
            }else {
                return true;
            }
        }
        else if (!edtValorOtrosFuentesIngresos.getText().toString().isEmpty()) {
            if (valorOtrosFuenteIngresos < 0) {
                return false;
            }else {
                return true;
            }
        }else {
            return true;
        }
    }

    private boolean validaCamposLlenos() {
        if (edtValorTrabajo.getText().toString().isEmpty() && edtValorNegocio.getText().
                toString().isEmpty() && edtValorOtrosFuentesIngresos.getText().toString().
                isEmpty()){
                return false;
        }else {
                return true;
        }
    }

    public void restarActivosTotal(final int n ){

        Query query= activosDatabaseReference.orderByChild("correo").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo obj7 =datanapshot.getValue(Activo.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getActivosTotal())-y;
                    //modificacion
                    activosDatabaseReference.child(clave).child("activosTotal").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public String separadorMiles(String num){
        int valor=Integer.parseInt(num);
        String nuevoNumero = String.format("%,d", valor);
        String numeroFinal=nuevoNumero.replace(",",".");
        return numeroFinal;
    }

    public void iraActivos(View v){
        Intent iraactivo = new Intent(this, Activos.class);
        startActivity(iraactivo);
    }

    public void irPresupuesto(View v){
        Intent irPresupuesto = new Intent(this, Presupuesto.class);
        startActivity(irPresupuesto);
    }
}
