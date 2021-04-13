package com.example.moneymapp.instrucciones;

public class Etapa {
    private String correo;
    private String nombreEtapa;

    public Etapa() {
    }

    public Etapa(String correo, String nombreEtapa) {
        this.correo = correo;
        this.nombreEtapa = nombreEtapa;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreEtapa() {
        return nombreEtapa;
    }

    public void setNombreEtapa(String nombreEtapa) {
        this.nombreEtapa = nombreEtapa;
    }
}
