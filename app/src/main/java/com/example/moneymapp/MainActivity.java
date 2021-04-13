package com.example.moneymapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView txvMoneyMapp;
    ImageView imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Agregar animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        txvMoneyMapp = findViewById(R.id.txv_moneyapp);
        imgLogo = findViewById(R.id.img_logo);


        txvMoneyMapp.setAnimation(animacion2);
        imgLogo.setAnimation(animacion1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                GoogleSignInAccount cuenta= GoogleSignIn.getLastSignedInAccount
                                            (MainActivity.this);
                if(user != null && cuenta!=null){
                    Intent intent= new Intent(MainActivity.this,Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent irLoginInicioSesion = new Intent(MainActivity.this,
                                    LoginInicioSesion.class);
                    startActivity(irLoginInicioSesion);
                    finish();
                }
            }
        }, 4000);
    }
}
