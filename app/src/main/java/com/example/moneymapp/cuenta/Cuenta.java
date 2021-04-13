package com.example.moneymapp.cuenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymapp.MenuPrincipal;
import com.example.mentorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Cuenta extends AppCompatActivity {
    private FirebaseUser user;
    private TextView email, nombre;
    private DatabaseReference userDatabaseReference;
    private String usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        email=(TextView)findViewById(R.id.txvCorreo);
        nombre=(TextView)findViewById(R.id.txvNombre);


        //obtención del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("usuario");

        mostrarDatosUsuario();

    }

    private void mostrarDatosUsuario() {

        Query query= userDatabaseReference.orderByChild("email").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario;
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    usuario=datanapshot.getValue(Usuario.class);
                    email.setText(usuario.getEmail());
                    nombre.setText(usuario.getNombre());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void irMenuPrincipal(View v){
        Intent irMenuprincipal = new Intent(this, MenuPrincipal.class);
        startActivity(irMenuprincipal);

    }

    public void irEditarCuenta(View v){
        Intent irEditarCuenta = new Intent(this, CuentaFinal.class);
        startActivity(irEditarCuenta);
    }


}
