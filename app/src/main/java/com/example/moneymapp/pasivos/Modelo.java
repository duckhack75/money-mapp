package com.example.moneymapp.pasivos;

public class Modelo {
    private String nombre;
    private String monto;

    public Modelo() {
    }

    public Modelo(String nombre, String monto) {
        this.nombre = nombre;
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
}
