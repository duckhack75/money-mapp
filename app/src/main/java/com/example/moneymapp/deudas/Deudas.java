package com.example.moneymapp.deudas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymapp.MenuPrincipal;
import com.example.mentorapp.R;
import com.example.moneymapp.pasivos.ListAdapter;
import com.example.moneymapp.pasivos.Modelo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Deudas extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private FirebaseUser user;
    private EditText edtDeuda, edtMontoDeuda;
    private Button btnIngresarDeuda;
    private String usuario, deudaSinSeparadorMiles;
    private static final int REQUEST_CODE=1;
    private int c;
    private DatabaseReference mDatabase;

    private List<Modelo> mLista= new ArrayList<>();
    private ListView mListview;
    private ListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas);

        edtDeuda=findViewById(R.id.edTNamed);
        edtMontoDeuda=findViewById(R.id.edtMontod);
        btnIngresarDeuda =findViewById(R.id.btnAgregardeuda);
        mListview=findViewById(R.id.listDeudas);
        mListview.setOnItemClickListener(this);

        //obtencion del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        //referencia para acceso a la tabla pasivos de la bbdd
        mDatabase = FirebaseDatabase.getInstance().getReference("Deudas");


        //agrega la lista todos los pasivos ya existentes
        getDeudas();

    }

    //método que verifica si existe la deuda
    public void verificaDeuda(View v) {
        //se consulta a la bbdd para validar si existe ya el usuario
        Query q2=mDatabase.orderByChild("nombre_deuda").equalTo(edtDeuda.getText().toString());

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Deuda obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Deuda.class);

                   if (usuario.equals(obj.getCorreo())){
                            c++;
                            Toast.makeText(Deudas.this,"error! esta Deuda ya existe!",Toast.LENGTH_LONG).show();
                            edtDeuda.setText("");
                            edtMontoDeuda.setText("");

                    }
                }
                if (edtDeuda.getText().toString().isEmpty() && edtMontoDeuda.getText().toString().isEmpty()){
                    Toast.makeText(Deudas.this,"error! debe llenar campos!",Toast.LENGTH_LONG).show();
                }else if (edtDeuda.getText().toString().isEmpty()){
                    Toast.makeText(Deudas.this,"error! debe llenar campos!",Toast.LENGTH_LONG).show();
                }else if (edtMontoDeuda.getText().toString().isEmpty()){
                    Toast.makeText(Deudas.this,"error! debe llenar campos!",Toast.LENGTH_LONG).show();
                }else{
                    agregarDeuda1();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void getDeudas() {
        Query q2=mDatabase.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                Deuda obj;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    obj=datanapshot.getValue(Deuda.class);
                    c++;
                    Modelo modelo=new Modelo();
                    String x=obj.getNombreDeuda();
                    deudaSinSeparadorMiles =obj.getMontoDeuda();
                    String y=separadorMiles(obj.getMontoDeuda());
                    mostrarLista(x,y);
                }

                if(c!=0){
                    //ya existe el usuario registrado

                }else{
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public String separadorMiles(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }

    //metodo para agregar las deudas ya existentes y agregarlos a la lista
    public void mostrarLista(String nombrepasivo, String montopasivo){
        Modelo modelo=new Modelo();
        modelo.setNombre(nombrepasivo);
        modelo.setMonto(montopasivo);
        mLista.add(modelo);
        edtDeuda.getText().clear();
        edtMontoDeuda.getText().clear();
        mAdapter=new ListAdapter(this,R.layout.item_row, mLista);
        mListview.setAdapter(mAdapter);

    }

    //método para agregar deuda a la base de datos
    public void agregarDeuda1(){
        //aquí se agrega el nuevo pasivo al listview
        String nombre=edtDeuda.getText().toString().trim();
        String monto=edtMontoDeuda.getText().toString().trim();

        Modelo modelo=new Modelo();
        modelo.setNombre(nombre);
        modelo.setMonto(separadorMiles(monto));

        mLista.add(modelo);
        edtDeuda.getText().clear();
        edtMontoDeuda.getText().clear();
        mAdapter=new ListAdapter(this,R.layout.item_row, mLista);
        mListview.setAdapter(mAdapter);

        //aquí se agrega el nuevo pasivo a la base de datos
        Deuda nuevadeuda= new Deuda();
        nuevadeuda.setCorreo(usuario);
        nuevadeuda.setNombreDeuda(nombre);
        nuevadeuda.setMontoDeuda(monto);
        nuevadeuda.setTipoDeuda("indeterminado");
        nuevadeuda.setEstado("activa");
        nuevadeuda.setMonto_pagado("0");
        //nuevadeuda.setGanancia_pasivo("0");
        String clave= mDatabase.push().getKey();
        mDatabase.child(clave).setValue(nuevadeuda);

    }



    //método para ir a activity MenuPrincipal
    public void irMenuPrincipal(View v){
        Intent iramenuprincipal = new Intent(this, MenuPrincipal.class);
        startActivity(iramenuprincipal);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this,"elemento clickeado: "+position,Toast.LENGTH_LONG).show();
        Intent intent =new Intent(getApplicationContext(), EgresoDeudas.class);
        intent.putExtra("nombre",mAdapter.getItem(position).getNombre());
        intent.putExtra("monto",mAdapter.getItem(position).getMonto());
        intent.putExtra("monto2", deudaSinSeparadorMiles);//para que me lo envie sin separadores
        intent.putExtra("posicion",String.valueOf(position));
        startActivityForResult(intent,REQUEST_CODE);
    }




}
