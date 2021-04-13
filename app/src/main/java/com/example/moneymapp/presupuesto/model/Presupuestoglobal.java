package com.example.moneymapp.presupuesto.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Presupuestoglobal {
    private String correo;
    private String presupuestoTotal;
    private String diaInicio;
    private String mesInicio;
    private String añoInicio;
    private String estadoPresupuesto;
    private Boolean validar;
    public String numeroRiqueza;


    public Presupuestoglobal() {
    }

    public Presupuestoglobal(String correo, String presupuestoTotal, String diaInicio,
                             String mesInicio, String añoInicio, String estadoPresupuesto) {
        this.correo = correo;
        this.presupuestoTotal = presupuestoTotal;
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.añoInicio = añoInicio;
        this.estadoPresupuesto = estadoPresupuesto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPresupuestoTotal() {
        return presupuestoTotal;
    }

    public void setPresupuestoTotal(String presupuestoTotal) {
        this.presupuestoTotal = presupuestoTotal;
    }

    public String getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(String diaInicio) {
        this.diaInicio = diaInicio;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getAñoInicio() {
        return añoInicio;
    }

    public void setAñoInicio(String añoInicio) {
        this.añoInicio = añoInicio;
    }

    public String getEstadoPresupuesto() {
        return estadoPresupuesto;
    }

    public void setEstadoPresupuesto(String estadoPresupuesto) {
        this.estadoPresupuesto = estadoPresupuesto;
    }


    //método para obtener el número de riqueza
    //método que separa por miles los números
    public String separador(String num){
        int x=Integer.parseInt(num);
        String str = String.format("%,d", x);
        String numfinal=str.replace(",",".");
        return numfinal;
    }

    public String obtenerNumeroRiqueza(final DatabaseReference referenciaDataBase, String usuario) {
        //se consulta a la bbdd para validar si existe ya el usuario y si no existe crear otro
        Query q2=referenciaDataBase.orderByChild("correo").equalTo(usuario);

        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datanapshot: dataSnapshot.getChildren()){
                    Presupuestoglobal presupuesto =datanapshot.getValue(Presupuestoglobal.class);
                    numeroRiqueza="$"+separador(presupuesto.getPresupuestoTotal());

                    validar=true;
                    break;


                }
                if(validar){
                    //no existe presupuesto ingresado
                }else{
                  numeroRiqueza="$ 0.00";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return numeroRiqueza;
    }



}





