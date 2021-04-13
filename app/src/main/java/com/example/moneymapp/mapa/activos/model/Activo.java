package com.example.moneymapp.mapa.activos.model;

public class Activo {
    private String correo;
    private String activosTotal;

    public Activo() {
    }

    public Activo(String correo, String activosTotal) {
        this.correo = correo;
        this.activosTotal = activosTotal;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getActivosTotal() {
        return activosTotal;
    }

    public void setActivosTotal(String activosTotal) {
        this.activosTotal = activosTotal;
    }
}
