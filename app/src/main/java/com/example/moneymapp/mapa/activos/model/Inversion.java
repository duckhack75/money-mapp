package com.example.moneymapp.mapa.activos.model;

public class Inversion {
    private String correo;
    private String negocios;
    private String bienesRaices;
    private String bolsaValores;
    private String metales;
    private String propiedadIntelectual;
    private String proteccion;
    private String otrasInversiones;
    private String totalInversiones;


    public Inversion() {
    }

    public Inversion(String correo, String negocios, String bienesRaices, String bolsaValores, String metales,
                     String propiedadIntelectual, String proteccion, String otrasInversiones, String totalInversiones) {
        this.correo = correo;
        this.negocios = negocios;
        this.bienesRaices = bienesRaices;
        this.bolsaValores = bolsaValores;
        this.metales = metales;
        this.propiedadIntelectual = propiedadIntelectual;
        this.proteccion = proteccion;
        this.otrasInversiones = otrasInversiones;
        this.totalInversiones = totalInversiones;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNegocios() {
        return negocios;
    }

    public void setNegocios(String negocios) {
        this.negocios = negocios;
    }

    public String getBienesRaices() {
        return bienesRaices;
    }

    public void setBienesRaices(String bienesRaices) {
        this.bienesRaices = bienesRaices;
    }

    public String getBolsaValores() {
        return bolsaValores;
    }

    public void setBolsaValores(String bolsaValores) {
        this.bolsaValores = bolsaValores;
    }

    public String getMetales() {
        return metales;
    }

    public void setMetales(String metales) {
        this.metales = metales;
    }

    public String getPropiedadIntelectual() {
        return propiedadIntelectual;
    }

    public void setPropiedadIntelectual(String propiedadIntelectual) {
        this.propiedadIntelectual = propiedadIntelectual;
    }

    public String getProteccion() {
        return proteccion;
    }

    public void setProteccion(String proteccion) {
        this.proteccion = proteccion;
    }

    public String getOtrasInversiones() {
        return otrasInversiones;
    }

    public void setOtrasInversiones(String otrasInversiones) {
        this.otrasInversiones = otrasInversiones;
    }

    public String getTotalInversiones() {
        return totalInversiones;
    }

    public void setTotalInversiones(String totalInversiones) {
        this.totalInversiones = totalInversiones;
    }
}
