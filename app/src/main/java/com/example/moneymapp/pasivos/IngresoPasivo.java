package com.example.moneymapp.pasivos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentorapp.R;
import com.example.moneymapp.pasivos.model.Pasivo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class IngresoPasivo extends AppCompatActivity {

    private FirebaseUser user;
    private TextView nombre, montopasivo, posicion, vapasivo;
    private DatabaseReference mDatabase, tDatabase;
    private Button eliminar;
    private EditText gpasivo, nuevonombre, nuevomonto;
    private String nombre_pasivo, usuario, posicion2, montoinversion;
    private int c, int_ganancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_pasivo);

        nombre=findViewById(R.id.txvNamepasivo);
        //posicion=findViewById(R.id.txtViewNombre2);
        eliminar=findViewById(R.id.btnEliminarg);
        montopasivo=findViewById(R.id.txvMontopasivo);
        vapasivo=findViewById(R.id.txvValoractualg);
        gpasivo=findViewById(R.id.edtGpasivo);

        nuevonombre=findViewById(R.id.edtNuevoname);
        nuevomonto=findViewById(R.id.edtNuevomonto);


        nombre.setText(getIntent().getStringExtra("nombre"));
        nombre_pasivo=getIntent().getStringExtra("nombre");
        String posicion=getIntent().getStringExtra("posicion");
        posicion2=posicion;
        montopasivo.setText("$"+getIntent().getStringExtra("monto"));
        montoinversion=getIntent().getStringExtra("monto");

        final String nPosicion=getIntent().getStringExtra("posicion");

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();


        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Pasivos");

        //se verifica si existe el ingreso pasivo en la bbdd y si no existe se crea otro
        verificaPasivo(mDatabase);





    }


    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }


    //método para editar el ingreso pasivo
    public void editarPasivo(View view){
        Intent intent=new Intent(this, EditarPasivo.class);
        intent.putExtra("nombre",nombre.getText().toString());
        intent.putExtra("monto",montopasivo.getText().toString());
        startActivity(intent);
    }

    //método que verifica si ya existe este ingreso pasivo y si no existe lo crea
    private void verificaPasivo(final DatabaseReference mDatabase) {
        //se consulta a la bbdd para validar si existe ya el usuario y si no existe crear otro
        Query q2=mDatabase.orderByChild("nombre_pasivo").equalTo(nombre_pasivo);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Pasivo obj= new Pasivo();
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    c++;
                    obj=datanapshot.getValue(Pasivo.class);
                    vapasivo.setText("$ "+separador(obj.getGananciaPasivos()));

                }

                if(c!=0){
                    //ya existe el usuario registrado
                }else{
                    Pasivo nuevopasivo= new Pasivo();
                    nuevopasivo.setCorreo(usuario);
                    nuevopasivo.setNombrePasivo(nombre_pasivo);
                    nuevopasivo.setMontoInversion(montoinversion);
                    nuevopasivo.setGananciaPasivos("0");
                    String clave= mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevopasivo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //método para sumar a la ganancia
    public void actualizarGanancia(View view){
        Query q2=mDatabase.orderByChild("nombre_pasivo").equalTo(nombre.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pasivo obj;
                int suma=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Pasivo.class);

                    if (!gpasivo.getText().toString().isEmpty()){
                        if (usuario.equals(obj.getCorreo())){
                            suma=Integer.parseInt(obj.getGananciaPasivos());
                            int totalg=suma+Integer.parseInt(gpasivo.getText().toString());
                            String clave=datanapshot.getKey();
                            mDatabase.child(clave).child("ganancia_pasivo").setValue(String.valueOf(totalg));
                            vapasivo.setText("$ "+separador(String.valueOf(totalg)));
                            gpasivo.setText("");
                        }
                    }else {
                        Toast.makeText(IngresoPasivo.this,"error!, debe llenar campo",Toast.LENGTH_LONG).show();
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //método para restar a la ganancia
    public void restarGanancia(View view){
        Query q2=mDatabase.orderByChild("nombre_pasivo").equalTo(nombre.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pasivo obj;
                int suma=0;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Pasivo.class);
                    if (!gpasivo.getText().toString().isEmpty()){

                        if (usuario.equals(obj.getCorreo())){
                            suma=Integer.parseInt(obj.getGananciaPasivos());
                            if (Integer.parseInt(gpasivo.getText().toString())>suma){
                                Toast.makeText(IngresoPasivo.this,"error! valor inválido",Toast.LENGTH_LONG).show();
                                gpasivo.setText("");
                            }else{
                                int totalg=suma-Integer.parseInt(gpasivo.getText().toString());
                                String clave=datanapshot.getKey();
                                mDatabase.child(clave).child("ganancia_pasivo").setValue(String.valueOf(totalg));
                                vapasivo.setText("$"+separador(String.valueOf(totalg)));
                                gpasivo.setText("");
                            }
                        }
                    }else {
                        Toast.makeText(IngresoPasivo.this,"error!, debe llenar campo",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //método para editar nombre y monto del pasivo
    public void actualizarDatosp(View view){
        Query q2=mDatabase.orderByChild("nombre_pasivo").equalTo(nombre.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pasivo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Pasivo.class);
                    if (usuario.equals(obj.getCorreo())){

                        if (nuevonombre.getText().toString().isEmpty() && nuevomonto.getText().toString().isEmpty()){
                            Toast.makeText(IngresoPasivo.this,"error!, debe llenar campos",Toast.LENGTH_LONG).show();
                        }
                        if (!nuevonombre.getText().toString().isEmpty()){
                            String clave=datanapshot.getKey();
                            String nuevopasivo=nuevonombre.getText().toString();
                            mDatabase.child(clave).child("nombre_pasivo").setValue(nuevopasivo);
                            nuevonombre.setText("");
                            nombre.setText(nuevopasivo);
                        }

                        if (!nuevomonto.getText().toString().isEmpty()){
                            String clave=datanapshot.getKey();
                            String nuevomontopasivo=nuevomonto.getText().toString();
                            mDatabase.child(clave).child("monto_inversion").setValue(nuevomontopasivo);
                            nuevomonto.setText("");
                            montopasivo.setText("$"+separador(nuevomontopasivo));
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
    public void eliminarPasivo(View view){
        Query q2=mDatabase.orderByChild("nombre_pasivo").equalTo(nombre.getText().toString());
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pasivo obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Pasivo.class);
                    if (usuario.equals(obj.getCorreo())){

                        String clave=datanapshot.getKey();
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference ref= database.getReference("Pasivos").child(clave);
                        ref.removeValue();
                        iraPasivos();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void iraPasivos(){
        Intent irapasivos = new Intent(this, Pasivos.class);
        startActivity(irapasivos);

    }

    public void iraPasivos2(View view){
        Intent irapasivos = new Intent(this, Pasivos.class);
        startActivity(irapasivos);

    }



}
