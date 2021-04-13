package com.example.moneymapp.mapa.entradas.model;

public class Efuentesingreso {
    private String correo;
    private String trabajo;
    private String negocio;
    private String otrasFuentesIngresos;
    private String totalFuentesIngresos;

    public Efuentesingreso() {
    }

    public Efuentesingreso(String correo, String trabajo, String negocio, String otrasFuentesIngresos, String totalFuentesIngresos) {
        this.correo = correo;
        this.trabajo = trabajo;
        this.negocio = negocio;
        this.otrasFuentesIngresos = otrasFuentesIngresos;
        this.totalFuentesIngresos = totalFuentesIngresos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(String trabajo) {
        this.trabajo = trabajo;
    }

    public String getNegocio() {
        return negocio;
    }

    public void setNegocio(String negocio) {
        this.negocio = negocio;
    }

    public String getOtrasFuentesIngresos() {
        return otrasFuentesIngresos;
    }

    public void setOtrasFuentesIngresos(String otrasFuentesIngresos) {
        this.otrasFuentesIngresos = otrasFuentesIngresos;
    }

    public String getTotalFuentesIngresos() {
        return totalFuentesIngresos;
    }

    public void setTotalFuentesIngresos(String totalFuentesIngresos) {
        this.totalFuentesIngresos = totalFuentesIngresos;
    }
}
