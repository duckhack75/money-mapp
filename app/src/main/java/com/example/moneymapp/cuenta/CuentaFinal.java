package com.example.moneymapp.cuenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class CuentaFinal extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference userDatabaseReference;
    private String usuario;
    private EditText nuevoNombre, nuevaPass, confirmaPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_final);

        nuevoNombre=(EditText)findViewById(R.id.edtNuevoNombreUser);
        nuevaPass=(EditText)findViewById(R.id.edtNuevaPass);
        confirmaPass =(EditText)findViewById(R.id.edtConfirmaNuevaPass);

        //obtención del nombre de usuario
        user = FirebaseAuth.getInstance().getCurrentUser();
        usuario= user.getEmail();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("usuario");
    }

    public void editarCuenta(final View view){

        Query query= userDatabaseReference.orderByChild("email").equalTo(usuario);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){

                    String clave=datanapshot.getKey();

                    boolean validarDatos=validarDatos();

                    if (validarDatos){
                        if(!nuevoNombre.getText().toString().isEmpty()){
                            userDatabaseReference.child(clave).child("nombre").setValue(nuevoNombre.
                                    getText().toString());
                            nuevoNombre.setText("");
                        }
                        if(!nuevaPass.getText().toString().isEmpty()){
                            userDatabaseReference.child(clave).child("password").setValue
                                    (nuevaPass.getText().toString());
                            nuevaPass.setText("");
                            confirmaPass.setText("");
                            Toast.makeText(CuentaFinal.this,
                                    "datos modificados exitosamente!", Toast.LENGTH_LONG).
                                    show();
                            String name=nuevaPass.getText().toString();

                            user.updatePassword("emilio1991");
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private boolean validarDatos() {
        if(nuevoNombre.getText().toString().isEmpty() || confirmaPass.getText().toString().isEmpty()
                || nuevaPass.getText().toString().isEmpty()){
            Toast.makeText(CuentaFinal.this,"error!!, llenar campos", Toast.LENGTH_LONG).
                    show();
            return false;

        }else if (!nuevaPass.getText().toString().equals(confirmaPass.getText().toString())){ Toast.
                makeText(CuentaFinal.this, "error!!, password no coinciden",Toast.
                        LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    public void irCuenta(View v){
        Intent irCuenta = new Intent(this, Cuenta.class);
        startActivity(irCuenta);
    }



}
