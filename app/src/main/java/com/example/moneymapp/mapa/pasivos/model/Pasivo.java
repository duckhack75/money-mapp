package com.example.moneymapp.mapa.pasivos.model;

public class Pasivo {
    private String Correo;
    private String pasivosTotal;

    public Pasivo() {
    }

    public Pasivo(String correo, String pasivosTotal) {
        Correo = correo;
        this.pasivosTotal = pasivosTotal;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getPasivosTotal() {
        return pasivosTotal;
    }

    public void setPasivosTotal(String pasivosTotal) {
        this.pasivosTotal = pasivosTotal;
    }
}
