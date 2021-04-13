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
import com.example.moneymapp.mapa.activos.model.Liquido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Liquidos extends AppCompatActivity {
    private FirebaseUser user;
    private TextView vacbanco, vaoifinancieras, vacartera, totalliquidos;
    private DatabaseReference mDatabase, tDatabase;
    private String usuario;
    private EditText cbanco, oifinancieras, cartera;
    private Liquido nuevoliquido;
    private int c, int_cbanco, int_oifinancieras, int_cartera,
            int_totalliquidos, total_final_liquidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidos);


        cbanco = (EditText) findViewById(R.id.edtAcuentabanco);
        oifinancieras = (EditText) findViewById(R.id.edtOinstanciasf);
        cartera = (EditText) findViewById(R.id.edtAcartera);
        totalliquidos = (TextView) findViewById(R.id.txvTotalLiquidos);

        //inicializamos el obj de separador de miles
       /*String mostrar=separador2("5.000");
        prueba.setText(mostrar);*/


        vacbanco = (TextView) findViewById(R.id.txvVAcuentabanco);
        vaoifinancieras = (TextView) findViewById(R.id.txvVAinstanciasf);
        vacartera = (TextView) findViewById(R.id.txvVAcartera);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario = user.getEmail();

        //acceso a la tabla Autos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("liquidos");
        //acceso a la tabla Presupuesto activo glabal de la bbdd
        tDatabase = FirebaseDatabase.getInstance().getReference("activos");
        //muestra de valores actuales, usando where usuario
        Query query = mDatabase.orderByChild("correo").equalTo(usuario);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Liquido obj;
                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    obj = datanapshot.getValue(Liquido.class);
                    vacbanco.setText("$ " + separador(obj.getCuentaBanco()));
                    vaoifinancieras.setText("$ " + separador(obj.getOtrasInstitucionesFinancieras()));
                    vacartera.setText("$ " + separador(obj.getCartera()));
                    totalliquidos.setText("$ " + separador(obj.getTotalLiquidos()));
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
    public void ingresarTotalLiquidos(View view) {

        //se consulta a la bbdd para validar si existe ya el usuario
        Query q = mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c = 0;
                Liquido obj;

                for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                    c++;
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj = datanapshot.getValue(Liquido.class);
                    //cálculo del total del presupuesto Autos
                    int_cbanco = Integer.parseInt(obj.getCuentaBanco());
                    int_oifinancieras = Integer.parseInt(obj.getOtrasInstitucionesFinancieras());
                    int_cartera = Integer.parseInt(obj.getCartera());
                    int_totalliquidos = Integer.parseInt(obj.getTotalLiquidos());


                    //obtención de la "primary key"
                    String clave = datanapshot.getKey();
                    total_final_liquidos = 0;

                    if (!cbanco.getText().toString().isEmpty()) {
                        int_cbanco = int_cbanco + Integer.parseInt(cbanco.getText().toString());
                        total_final_liquidos = total_final_liquidos + Integer.parseInt(cbanco.getText().toString());
                        mDatabase.child(clave).child("cuentaBanco").setValue(String.valueOf(int_cbanco));
                        cbanco.setText("");
                    }
                    if (!oifinancieras.getText().toString().isEmpty()) {
                        int_oifinancieras = int_oifinancieras + Integer.parseInt(oifinancieras.getText().toString());
                        total_final_liquidos = total_final_liquidos + Integer.parseInt(oifinancieras.getText().toString());
                        mDatabase.child(clave).child("otrasInstitucionesFinancieras").setValue(String.valueOf(int_oifinancieras));
                        oifinancieras.setText("");
                    }
                    if (!cartera.getText().toString().isEmpty()) {
                        int_cartera = int_cartera + Integer.parseInt(cartera.getText().toString());
                        total_final_liquidos = total_final_liquidos + Integer.parseInt(cartera.getText().toString());
                        mDatabase.child(clave).child("cartera").setValue(String.valueOf(int_cartera));
                        cartera.setText("");
                    }
                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalActivos(total_final_liquidos);
                    //ingreso del total presupuesto auto final a la bbdd
                    total_final_liquidos = total_final_liquidos + int_totalliquidos;
                    mDatabase.child(clave).child("totalLiquidos").setValue(String.valueOf(total_final_liquidos));

                    //se realiza otra consulta para actualizar los datos
                    Query q = mDatabase.orderByChild("correo").equalTo(usuario);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Liquido obj3;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj3 = datanapshot.getValue(Liquido.class);
                                vacbanco.setText("$ " + separador(obj3.getCuentaBanco()));
                                vaoifinancieras.setText("$ " + separador(obj3.getOtrasInstitucionesFinancieras()));
                                vacartera.setText("$ " + separador(obj3.getCartera()));
                                totalliquidos.setText("$ " + separador(obj3.getTotalLiquidos()));
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
                    nuevoliquido = new Liquido();
                    nuevoliquido.setCorreo(usuario);

                    if (cbanco.getText().toString().isEmpty()) {
                        nuevoliquido.setCuentaBanco("0");
                    } else {
                        nuevoliquido.setCuentaBanco(cbanco.getText().toString());
                    }

                    if (oifinancieras.getText().toString().isEmpty()) {
                        nuevoliquido.setOtrasInstitucionesFinancieras("0");
                    } else {
                        nuevoliquido.setOtrasInstitucionesFinancieras(oifinancieras.getText().toString());
                    }

                    if (cartera.getText().toString().isEmpty()) {
                        nuevoliquido.setCartera("0");
                    } else {
                        nuevoliquido.setCartera(cartera.getText().toString());
                    }


                    //suma del total inicial
                    int_cbanco = Integer.parseInt(nuevoliquido.getCuentaBanco());
                    int_oifinancieras = Integer.parseInt(nuevoliquido.getOtrasInstitucionesFinancieras());
                    int_cartera = Integer.parseInt(nuevoliquido.getCartera());

                    total_final_liquidos = 0;
                    total_final_liquidos = int_totalliquidos + int_cbanco + int_oifinancieras + int_cartera;
                    nuevoliquido.setTotalLiquidos(String.valueOf(total_final_liquidos));

                    //aquí se invoca la función para cambiar presupuesto general
                    sumarTotalActivos(total_final_liquidos);
                    String clave = mDatabase.push().getKey();
                    mDatabase.child(clave).setValue(nuevoliquido);

                    //se realiza otra consulta para actualizar los datos
                    Query qu = mDatabase.orderByChild("correo").equalTo(usuario);
                    qu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Liquido obj4;
                            for (DataSnapshot datanapshot : dataSnapshot.getChildren()) {
                                obj4 = datanapshot.getValue(Liquido.class);
                                vacbanco.setText("$ " + separador(obj4.getTotalLiquidos()));
                                vaoifinancieras.setText("$ " + separador(obj4.getOtrasInstitucionesFinancieras()));
                                vacartera.setText("$ " + separador(obj4.getCartera()));
                                totalliquidos.setText("$ " + separador(obj4.getTotalLiquidos()));

                            }
                            //limpiar campos input
                            cbanco.setText("");
                            oifinancieras.setText("");
                            cartera.setText("");
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
    public void sumarTotalActivos(final int v){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int z=v;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo m =datanapshot.getValue(Activo.class);
                    String clave=datanapshot.getKey();
                    z=z+Integer.parseInt(m.getActivosTotal());
                    //modificacion
                    tDatabase.child(clave).child("activosTotal").setValue(String.valueOf(z));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //método para actualizar datos presupuesto auto
    public void actualizarTotalLiquidos(final View view){

        Query q=mDatabase.orderByChild("correo").equalTo(usuario);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Liquido obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    //acceso a los valores que tiene el usuario y transformación a enteros
                    obj=datanapshot.getValue(Liquido.class);
                    //cálculo del total del presupuesto Autos
                    int_cbanco=Integer.parseInt(obj.getCuentaBanco());
                    int_oifinancieras=Integer.parseInt(obj.getOtrasInstitucionesFinancieras());
                    int_cartera=Integer.parseInt(obj.getCartera());
                    int_totalliquidos=Integer.parseInt(obj.getTotalLiquidos());

                    //validacion de datos
                    boolean valida=true;
                    boolean valida2=true;

                    //validación si todos los campos están llenos
                    int contador=0;//contador para sumar lo que se resta al presupuesto global
                    if (!cbanco.getText().toString().isEmpty()){
                        int_cbanco=int_cbanco-Integer.parseInt(cbanco.getText().toString());
                        contador=contador+Integer.parseInt(cbanco.getText().toString());
                        if (int_cbanco<0){
                            valida=false;
                        }
                    }

                    if (!oifinancieras.getText().toString().isEmpty()){
                        int_oifinancieras=int_oifinancieras-Integer.parseInt(oifinancieras.getText().toString());
                        contador=contador+Integer.parseInt(oifinancieras.getText().toString());
                        if (int_oifinancieras<0){
                            valida=false;
                        }
                    }

                    if (!cartera.getText().toString().isEmpty()){
                        int_cartera=int_cartera-Integer.parseInt(cartera.getText().toString());
                        contador=contador+Integer.parseInt(cartera.getText().toString());
                        if (int_cartera<0){
                            valida=false;
                        }
                    }



                    //validación de campos llenos
                    if (cbanco.getText().toString().isEmpty() && oifinancieras.getText().toString().isEmpty()
                            && cartera.getText().toString().isEmpty()){
                        valida2=false;
                    }

                    //cálculo del total del presupuesto Casas e insert de datos
                    if (valida2==false){
                        Toast.makeText(Liquidos.this,"error! debe llenar 1 o más campos!",Toast.LENGTH_LONG).show();
                    }else if (valida==true){

                        //obtención de la "primary key"
                        String clave=datanapshot.getKey();
                        restarActivosTotal(contador);//método que resta al presupuesto globa
                        if(!cbanco.getText().toString().isEmpty()){
                            int_totalliquidos=int_totalliquidos-Integer.parseInt(cbanco.getText().toString());
                            mDatabase.child(clave).child("totalLiquidos").setValue(String.valueOf(int_totalliquidos));
                            mDatabase.child(clave).child("cuentaBanco").setValue(String.valueOf(int_cbanco));
                            cbanco.setText("");
                        }

                        if(!oifinancieras.getText().toString().isEmpty()){
                            int_totalliquidos=int_totalliquidos-Integer.parseInt(oifinancieras.getText().toString());
                            mDatabase.child(clave).child("totalLiquidos").setValue(String.valueOf(int_totalliquidos));
                            mDatabase.child(clave).child("otrasInstitucionesFinancieras").setValue(String.valueOf(int_oifinancieras));
                            oifinancieras.setText("");
                        }


                        if(!cartera.getText().toString().isEmpty()){
                            int_totalliquidos=int_totalliquidos-Integer.parseInt(cartera.getText().toString());
                            mDatabase.child(clave).child("totalLiquidos").setValue(String.valueOf(int_totalliquidos));
                            mDatabase.child(clave).child("cartera").setValue(String.valueOf(int_cartera));
                            cartera.setText("");
                        }

                        //se realiza otra consulta para actualizar los datos
                        //nueva consulta para mostrar los valores actuales después de eliminar valores
                        Query q=mDatabase.orderByChild("correo").equalTo(usuario);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Liquido obj3;
                                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                                    obj3=datanapshot.getValue(Liquido.class);
                                    vacbanco.setText("$ "+separador(obj3.getTotalLiquidos()));
                                    vaoifinancieras.setText("$ "+separador(obj3.getOtrasInstitucionesFinancieras()));
                                    vacartera.setText("$ "+separador(obj3.getCartera()));
                                    totalliquidos.setText("$ "+separador(obj3.getTotalLiquidos()));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else{
                        Toast.makeText(Liquidos.this,"error! ingresar valor válido!",Toast.LENGTH_LONG).show();
                        //limpiar campos input
                        cbanco.setText("");
                        oifinancieras.setText("");
                        cartera.setText("");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //método para sumar los valores ingresados por cada item al presupuesto global
    public void restarActivosTotal(final int n ){

        //obtención de la "primary key"
        Query q3=tDatabase.orderByChild("correo").equalTo(usuario);

        q3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int y=n;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Activo obj7 =datanapshot.getValue(Activo.class);
                    String clave=datanapshot.getKey();
                    y=Integer.parseInt(obj7.getActivosTotal())-y;
                    //modificacion
                    tDatabase.child(clave).child("activosTotal").setValue(String.valueOf(y));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    //método para volver a activos
    public void iraActivos(View v){
        Intent iraactivo = new Intent(this, Activos.class);
        startActivity(iraactivo);
    }


}
