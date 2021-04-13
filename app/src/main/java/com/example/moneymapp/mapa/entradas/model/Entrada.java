package com.example.moneymapp.mapa.entradas.model;

public class Entrada {
    private String Correo;
    private String entradasTotal;

    public Entrada() {
    }

    public Entrada(String correo, String entradasTotal) {
        Correo = correo;
        this.entradasTotal = entradasTotal;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getEntradasTotal() {
        return entradasTotal;
    }

    public void setEntradasTotal(String entradasTotal) {
        this.entradasTotal = entradasTotal;
    }
}
