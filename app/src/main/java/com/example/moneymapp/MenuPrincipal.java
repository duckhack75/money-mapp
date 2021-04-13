package com.example.moneymapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mentorapp.R;
import com.example.moneymapp.cuenta.Cuenta;
import com.example.moneymapp.deudas.Deudas;
import com.example.moneymapp.estrategias.Estrategias;
import com.example.moneymapp.informe.Informe;
import com.example.moneymapp.instrucciones.Instrucciones;
import com.example.moneymapp.instrucciones.Instrucciones1;
import com.example.moneymapp.mapa.Mapa;
import com.example.moneymapp.pasivos.Pasivos;
import com.example.moneymapp.presupuesto.Presupuesto;
import com.google.firebase.auth.FirebaseAuth;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

    }


    public void irHome(View v){
        Intent irHome = new Intent(this, Home.class);
        startActivity(irHome);
    }


    public void irDeudas(View v){
        Intent irDeudas = new Intent(this, Deudas.class);
        startActivity(irDeudas);
    }


    public void irInforme(View v){
        Intent irInforme = new Intent(this, Informe.class);
        startActivity(irInforme);
    }


    public void irPasivos(View v){
        Intent irPasivos = new Intent(this, Pasivos.class);
        startActivity(irPasivos);
    }


    public void irPresupuesto(View v){
        Intent irPresupuesto = new Intent(this, Presupuesto.class);
        startActivity(irPresupuesto);
    }



    public void irCuenta(View v){
        Intent irCuenta = new Intent(this, Cuenta.class);
        startActivity(irCuenta);
    }

    public void irMapa(View v){
        Intent irMapa = new Intent(this, Mapa.class);
        startActivity(irMapa);
    }

    public void irEtapas(View v){
        Intent irEtapas = new Intent(this, Instrucciones.class);
        startActivity(irEtapas);

    }

    public void cerrarSesion(View v){
        Intent irLogin = new Intent(this, LoginInicioSesion.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(irLogin);
        finish();
    }

    public void iraEstrategias(View v){
        Intent irEstrategias = new Intent(this, Estrategias.class);
        startActivity(irEstrategias);
    }

    public void iraInstrucciones(View v){
        Intent irInstrucciones = new Intent(this, Instrucciones1.class);
        startActivity(irInstrucciones);
    }

}
