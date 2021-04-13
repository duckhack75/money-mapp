package com.example.moneymapp.presupuesto.model;

public class Casas {
    private String correo;
    private String renta;
    private String mantenimiento;
    private String limpieza;
    private String decoracion;
    private String reparaciones;
    private String jardineria;
    private String otros;
    private String totalCasa;

    public Casas() {
    }

    public Casas(String correo, String renta, String mantenimiento, String limpieza,
                 String decoracion, String reparaciones, String jardineria, String otros, String totalCasa) {
        this.correo = correo;
        this.renta = renta;
        this.mantenimiento = mantenimiento;
        this.limpieza = limpieza;
        this.decoracion = decoracion;
        this.reparaciones = reparaciones;
        this.jardineria = jardineria;
        this.otros = otros;
        this.totalCasa = totalCasa;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRenta() {
        return renta;
    }

    public void setRenta(String renta) {
        this.renta = renta;
    }

    public String getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(String mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public String getLimpieza() {
        return limpieza;
    }

    public void setLimpieza(String limpieza) {
        this.limpieza = limpieza;
    }

    public String getDecoracion() {
        return decoracion;
    }

    public void setDecoracion(String decoracion) {
        this.decoracion = decoracion;
    }

    public String getReparaciones() {
        return reparaciones;
    }

    public void setReparaciones(String reparaciones) {
        this.reparaciones = reparaciones;
    }

    public String getJardineria() {
        return jardineria;
    }

    public void setJardineria(String jardineria) {
        this.jardineria = jardineria;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getTotalCasa() {
        return totalCasa;
    }

    public void setTotalCasa(String totalCasa) {
        this.totalCasa = totalCasa;
    }

}
