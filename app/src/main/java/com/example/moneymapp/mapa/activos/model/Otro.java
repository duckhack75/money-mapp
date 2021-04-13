package com.example.moneymapp.mapa.activos.model;

public class Otro {
    private String correo;
    private String cuentas;
    private String educacion;
    private String otrosActivos;
    private String totalOtrosActivos;


    public Otro() {
    }

    public Otro(String correo, String cuentas, String educacion, String otrosActivos, String totalOtrosActivos) {
        this.correo = correo;
        this.cuentas = cuentas;
        this.educacion = educacion;
        this.otrosActivos = otrosActivos;
        this.totalOtrosActivos = totalOtrosActivos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCuentas() {
        return cuentas;
    }

    public void setCuentas(String cuentas) {
        this.cuentas = cuentas;
    }

    public String getEducacion() {
        return educacion;
    }

    public void setEducacion(String educacion) {
        this.educacion = educacion;
    }

    public String getOtrosActivos() {
        return otrosActivos;
    }

    public void setOtrosActivos(String otrosActivos) {
        this.otrosActivos = otrosActivos;
    }

    public String getTotalOtrosActivos() {
        return totalOtrosActivos;
    }

    public void setTotalOtrosActivos(String totalOtrosActivos) {
        this.totalOtrosActivos = totalOtrosActivos;
    }
}
