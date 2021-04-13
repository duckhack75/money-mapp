package com.example.moneymapp.deudas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class EgresoDeudas extends AppCompatActivity {
    private FirebaseUser user;
    private TextView nombred, montodeuda, vadeuda, montopagado;
    private DatabaseReference mDatabase, tDatabase;
    private Spinner spinner;
    private Button eliminar;
    private RadioButton rbtdestructiva, rbtconstructiva, rbtyopresto;
    private EditText pdeuda, nuevondeuda, nuevomdeuda;
    private String nombre_deuda, usuario, posicion2, str_montodeuda, nom_deuda, nombre_deuda1;
    private int c, int_ganancia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egreso_deudas);

        nombred=findViewById(R.id.txvNamedeuda);
        montodeuda=findViewById(R.id.txvMontodeuda);
        montopagado=findViewById(R.id.txvValoractuald);

        pdeuda=findViewById(R.id.edtPdeuda);
        nuevondeuda=findViewById(R.id.edtNuevonamed);
        nuevomdeuda=findViewById(R.id.edtNuevomontod);





        spinner=findViewById(R.id.spinner);

        nombre_deuda=nombred.getText().toString();
        nombred.setText(getIntent().getStringExtra("nombre"));
        nombre_deuda1=getIntent().getStringExtra("nombre");


        montodeuda.setText("$"+getIntent().getStringExtra("monto"));
        str_montodeuda=getIntent().getStringExtra("monto2");

        rbtconstructiva=findViewById(R.id.rbtConstructiva);
        rbtdestructiva=findViewById(R.id.rbtDestructiva);
        rbtyopresto=findViewById(R.id.rbtYopresto);


        final String nPosicion=getIntent().getStringExtra("posicion");

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Deudas");

        //se verifica si existe el ingreso pasivo en la bbdd y si no existe se crea otro
        if(nombre_deuda.equals("nombre")){
            verificaDeuda(nombre_deuda1);

        }else {
            verificaDeuda(nombre_deuda);
        }
    }


    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }






    //método que verifica si ya existe la deuda y si no existe lo crea
    private void verificaDeuda(String namedeuda) {
        //se consulta a la bbdd para validar si existe ya el usuario y si no existe crear otro
        Query q2=mDatabase.orderByChild("nombre_deuda").equalTo(namedeuda);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Deuda obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    obj=datanapshot.getValue(Deuda.class);
                    if (usuario.equals(obj.getCorreo())){
                        //monto pagado de la deuda
                        nombred.setText(obj.getNombreDeuda());
                        montopagado.setText("$ "+separador(obj.getMonto_pagado()));
                       //colocamos el tipo de deuda
                        if (obj.getTipoDeuda().equals("constructiva")){
                            rbtconstructiva.setChecked(true);
                        }else if(obj.getTipoDeuda().equals("destructiva")){
                            rbtdestructiva.setChecked(true);
                        }else if (obj.getTipoDeuda().equals("yo presto")){
                            rbtyopresto.setChecked(true);
                        }else {

                        }


                        //para mostrar estado deuda
                        if (obj.getEstado().equals("activa")){
                            //muestraSpinner("activa");
                            muestraSpinner(0);

                        }else{
                            muestraSpinner(1);
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //método para mostrar los valores del spinner actuales de cada deuda a trabajar
    public void muestraSpinner(final int estado1){
        //actualizamos el estado de la deuda
        //ingreso de los valores al spiner
        String [] opciones= {"activa","inactiva"};
        ArrayList<String> arrayList2= new ArrayList<>(Arrays.asList(opciones));
        ArrayAdapter<String> arrayAdapter2= new ArrayAdapter<>(this,R.layout.style_spinner, arrayList2);
        spinner.setAdapter(arrayAdapter2);
        spinner.setSelection(0);//aquí hay un error
    }

    public static int obtenerPosicionItem(Spinner spinner, String fruta) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }



    //método para ingresar todos los datos de cada deuda
    public void ingresarDatos(View view){
        Query q2=mDatabase.orderByChild("nombre_deuda").equalTo(nombred.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Deuda obj;
                int suma=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Deuda.class);
                    if (usuario.equals(obj.getCorreo())){
                        //se encuentra la pk
                        String clave=datanapshot.getKey();
                        //ingresamos el tipo de la deuda
                        if(rbtconstructiva.isChecked()){
                            mDatabase.child(clave).child("tipo_deuda").setValue("constructiva");

                        }else if(rbtdestructiva.isChecked()){
                            mDatabase.child(clave).child("tipo_deuda").setValue("destructiva");
                        }else if(rbtyopresto.isChecked()){
                            mDatabase.child(clave).child("tipo_deuda").setValue("yo presto");
                        }else {
                            //indeterminado
                        }


                      //ingreso del estado de la deuda
                       String estadoactual= (String) spinner.getSelectedItem();
                        if(estadoactual.equals("activa")){
                            mDatabase.child(clave).child("estado").setValue("activa");
                        }else{
                            mDatabase.child(clave).child("estado").setValue("inactiva");
                        }


                        if(!pdeuda.getText().toString().isEmpty()){

                            suma=Integer.parseInt(obj.getMonto_pagado());

                            int totalg=suma+Integer.parseInt(pdeuda.getText().toString());
                            if (totalg > Integer.parseInt(obj.getMontoDeuda())){
                                Toast.makeText(EgresoDeudas.this,"error! valor inválido sobrepasa el " +
                                        "pago de la deuda!",Toast.LENGTH_LONG).show();
                            }else {
                                mDatabase.child(clave).child("monto_pagado").setValue(String.valueOf(totalg));
                                montopagado.setText(separador(String.valueOf(totalg)));
                                pdeuda.setText("");
                            }

                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void restaDeuda(View view){
        Query q2=mDatabase.orderByChild("nombre_deuda").equalTo(nombred.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Deuda obj;
                int suma=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Deuda.class);
                    if (usuario.equals(obj.getCorreo())){
                        suma=Integer.parseInt(obj.getMonto_pagado());

                        if(pdeuda.getText().toString().isEmpty()){
                            Toast.makeText(EgresoDeudas.this,"error! llenar campo!",Toast.LENGTH_LONG).show();
                        }else if (Integer.parseInt(pdeuda.getText().toString())>suma){
                            Toast.makeText(EgresoDeudas.this,"error! valor inválido!",Toast.LENGTH_LONG).show();
                            pdeuda.setText("");
                        }else{
                            int totalg=suma-Integer.parseInt(pdeuda.getText().toString());
                            String clave=datanapshot.getKey();
                            mDatabase.child(clave).child("monto_pagado").setValue(String.valueOf(totalg));
                            montopagado.setText("$"+separador(String.valueOf(totalg)));
                            pdeuda.setText("");
                        }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //método para editar nombre y monto de la deuda
    public void actualizarDatosd(View view){
        Query q2=mDatabase.orderByChild("nombre_deuda").equalTo(nombred.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Deuda obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Deuda.class);
                    if (usuario.equals(obj.getCorreo())){

                        if (nuevondeuda.getText().toString().isEmpty() && nuevomdeuda.getText().toString().isEmpty()){
                            Toast.makeText(EgresoDeudas.this,"error!, debe llenar campos",Toast.LENGTH_LONG).show();
                        }
                        if (!nuevondeuda.getText().toString().isEmpty()){
                            String clave=datanapshot.getKey();
                            String nuevonomdeuda=nuevondeuda.getText().toString();
                            mDatabase.child(clave).child("nombre_deuda").setValue(nuevonomdeuda);
                            nuevondeuda.setText("");
                            nombred.setText(nuevonomdeuda);
                            verificaDeuda(nuevonomdeuda);
                        }

                        if (!nuevomdeuda.getText().toString().isEmpty()){
                            String clave=datanapshot.getKey();
                            String nuevomontodeuda=nuevomdeuda.getText().toString();
                            mDatabase.child(clave).child("monto_deuda").setValue(nuevomontodeuda);
                            nuevomdeuda.setText("");
                            montodeuda.setText(nuevomontodeuda);
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void editDeuda(View view){
        //obtención de la "primary key"
        Query q=mDatabase.orderByChild("nombre_deuda").equalTo(nombred.getText().toString());

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Deuda obj1 =datanapshot.getValue(Deuda.class);
                    String clave=datanapshot.getKey();
                    if (usuario.equals(obj1.getCorreo())){

                        if (nuevondeuda.getText().toString().isEmpty() && nuevomdeuda.getText().toString().isEmpty()){
                            Toast.makeText(EgresoDeudas.this,"error!, debe llenar campos",Toast.LENGTH_LONG).show();
                        }
                        if (!nuevondeuda.getText().toString().isEmpty()){

                            String nuevonomdeuda=nuevondeuda.getText().toString();
                            mDatabase.child(clave).child("nombre_deuda").setValue(nuevonomdeuda);
                            nuevondeuda.setText("");
                            nombred.setText(nuevonomdeuda);
                        }

                        if (!nuevomdeuda.getText().toString().isEmpty()){
                            String nuevomontodeuda=nuevomdeuda.getText().toString();
                            mDatabase.child(clave).child("monto_deuda").setValue(nuevomontodeuda);
                            nuevomdeuda.setText("");
                            montodeuda.setText(separador(nuevomontodeuda));
                        }

                    }



                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //método para sumar a la ganancia
    public void eliminarDeuda(View view){
        Query q2=mDatabase.orderByChild("nombre_deuda").equalTo(nombred.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Deuda obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Deuda.class);
                    if (usuario.equals(obj.getCorreo())){

                        String clave=datanapshot.getKey();
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference ref= database.getReference("Deudas").child(clave);
                        ref.removeValue();
                        iraDeudas();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void iraDeudas(){
        Intent iradeudas = new Intent(this, Deudas.class);
        startActivity(iradeudas);

    }

    public void iraDeudas2(View view){
        Intent iradeudas = new Intent(this, Deudas.class);
        startActivity(iradeudas);

    }
}
