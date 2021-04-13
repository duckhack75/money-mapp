package com.example.moneymapp.mapa.activos.model;

public class FuenteIngresos {
    private String correo;
    private String trabajo;
    private String negocio;
    private String otrosFuentesIngresos;
    private String totalFuentesIngresos;


    public FuenteIngresos() {
    }

    public FuenteIngresos(String correo, String trabajo, String negocio, String otrosFuentesIngresos, String totalFuentesIngresos) {
        this.correo = correo;
        this.trabajo = trabajo;
        this.negocio = negocio;
        this.otrosFuentesIngresos = otrosFuentesIngresos;
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

    public String getOtrosFuentesIngresos() {
        return otrosFuentesIngresos;
    }

    public void setOtrosFuentesIngresos(String otrosFuentesIngresos) {
        this.otrosFuentesIngresos = otrosFuentesIngresos;
    }

    public String getTotalFuentesIngresos() {
        return totalFuentesIngresos;
    }

    public void setTotalFuentesIngresos(String totalFuentesIngresos) {
        this.totalFuentesIngresos = totalFuentesIngresos;
    }
}
