package com.example.moneymapp.presupuesto.model;

public class Hijo {
    private String correo;
    private String escuela;
    private String dineroExtra;
    private String celular;
    private String nana;
    private String utiles;
    private String clases;
    private String juguetes;
    private String otrosGastosHijos;
    private String totalHijos;

    public Hijo() {
    }

    public Hijo(String correo, String escuela, String dineroExtra, String celular, String nana,
                String utiles, String clases, String juguetes, String otrosGastosHijos, String totalHijos) {
        this.correo = correo;
        this.escuela = escuela;
        this.dineroExtra = dineroExtra;
        this.celular = celular;
        this.nana = nana;
        this.utiles = utiles;
        this.clases = clases;
        this.juguetes = juguetes;
        this.otrosGastosHijos = otrosGastosHijos;
        this.totalHijos = totalHijos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    public String getDineroExtra() {
        return dineroExtra;
    }

    public void setDineroExtra(String dineroExtra) {
        this.dineroExtra = dineroExtra;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getNana() {
        return nana;
    }

    public void setNana(String nana) {
        this.nana = nana;
    }

    public String getUtiles() {
        return utiles;
    }

    public void setUtiles(String utiles) {
        this.utiles = utiles;
    }

    public String getClases() {
        return clases;
    }

    public void setClases(String clases) {
        this.clases = clases;
    }

    public String getJuguetes() {
        return juguetes;
    }

    public void setJuguetes(String juguetes) {
        this.juguetes = juguetes;
    }

    public String getOtrosGastosHijos() {
        return otrosGastosHijos;
    }

    public void setOtrosGastosHijos(String otrosGastosHijos) {
        this.otrosGastosHijos = otrosGastosHijos;
    }

    public String getTotalHijos() {
        return totalHijos;
    }

    public void setTotalHijos(String totalHijos) {
        this.totalHijos = totalHijos;
    }
}
