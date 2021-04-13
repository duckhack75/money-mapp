package com.example.moneymapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginInicioSesion extends AppCompatActivity {

    Button inicioSesion, nuevoUsuario;
    EditText emailEditText, passwordEditText;
    String email, password;
    private FirebaseAuth autentificar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_inicio_sesion);

        inicioSesion = findViewById(R.id.inicioSesion);
        nuevoUsuario = findViewById(R.id.nuevoUsuario);

        emailEditText =findViewById(R.id.emailEditText);
        passwordEditText =findViewById(R.id.passwordEditText);


        autentificar=FirebaseAuth.getInstance();
    }

    public void iniciarSesion(View view){

        boolean validarCorreo=validarCorreo();

        if (validarCorreo){
            autentificar.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(LoginInicioSesion.this,
                                        Home.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }

    }

    private boolean validarCorreo() {
        email= emailEditText.getText().toString().trim();
        password= passwordEditText.getText().toString().trim();

        if(email.isEmpty()|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("correo inválido");
            return false;
        }else if(password.isEmpty()|| password.length()<8){
            passwordEditText.setError("se necesitan mas de 8 carácteres");
            return false;
        }else{
            passwordEditText.setError(null);
            return true;
        }
    }

    public void irRegistro(View view){
        Intent irRegistro = new Intent(LoginInicioSesion.this, RegistroNuevo.class);
        startActivity(irRegistro);
    }
}
