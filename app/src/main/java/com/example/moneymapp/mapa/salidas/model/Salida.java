package com.example.moneymapp.mapa.salidas.model;

public class Salida {
    private String correo;
    private String salidaTotal;
    private String alterna;

    public Salida() {
    }

    public Salida(String correo, String salidaTotal, String alterna) {
        this.correo = correo;
        this.salidaTotal = salidaTotal;
        this.alterna = alterna;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSalidaTotal() {
        return salidaTotal;
    }

    public void setSalidaTotal(String salidaTotal) {
        this.salidaTotal = salidaTotal;
    }

    public String getAlterna() {
        return alterna;
    }

    public void setAlterna(String alterna) {
        this.alterna = alterna;
    }
}
