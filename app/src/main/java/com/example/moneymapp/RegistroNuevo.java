package com.example.moneymapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroNuevo extends AppCompatActivity {

    private EditText nameUser, emailUser, pass, confirmaPass;
    private FirebaseAuth autentificar;
    private DatabaseReference data;
    private String email, password, confirmPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_nuevo);


        nameUser=findViewById(R.id.edtNombreUser);
        emailUser=findViewById(R.id.edtCorreoUser);
        pass=findViewById(R.id.edtPassUser);
        confirmaPass=findViewById(R.id.edtConfirmaPassUser);

        autentificar= FirebaseAuth.getInstance();
        data= FirebaseDatabase.getInstance().getReference();

    }

    public void registrarUser(View view){
        email= emailUser.getText().toString().trim();
        password= pass.getText().toString().trim();
        confirmPass= confirmaPass.getText().toString().trim();

        boolean validarCorreo =validarCorreo();

        if (validarCorreo){
            autentificar.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String email= emailUser.getText().toString().trim();
                                String password= pass.getText().toString().trim();
                                String nombre= nameUser.getText().toString().trim();

                                Map<String, Object> map= new HashMap<>();
                                map.put("nombre",nombre);
                                map.put("email",email);
                                map.put("password",password);


                                //como obtener id del nuevo usuario registrado en la BBDD
                                String id= autentificar.getCurrentUser().getUid();

                                data.child("Usuario").child(id).setValue(map).addOnCompleteListener
                                        (new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task2) {
                                                if(task2.isSuccessful()){
                                                    Intent intent= new Intent(RegistroNuevo.
                                                            this, Home.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(RegistroNuevo.this,"Fallo en registrarse",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean validarCorreo() {
        if(email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUser.setError("correo inválido");
            return false;
        } else if(password.isEmpty()|| password.length()<8){
            pass.setError("se necesitan mas de 8 carácteres");
            return false;
        }else if (!confirmPass.equals(password)){
            pass.setError("contraseñas deben ser iguales");
            return false;
        } else{
            emailUser.setError(null);
            pass.setError(null);
            return true;
        }
    }

    public void iraLogin(View view){
        Intent irLogin= new Intent(RegistroNuevo.this, LoginInicioSesion.class);
        startActivity(irLogin);
        finish();
    }
}
