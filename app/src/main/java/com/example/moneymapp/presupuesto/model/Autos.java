package com.example.moneymapp.presupuesto.model;

public class Autos {
    private String correo;
    private String gasolina;
    private String lavado;
    private String mantenimiento;
    private String estacionamiento;
    private String otros;
    private String totalAutos;

    public Autos() {
    }

    public Autos(String correo, String gasolina, String lavado, String mantenimiento, String estacionamiento, String otros, String totalAutos) {
        this.correo = correo;
        this.gasolina = gasolina;
        this.lavado = lavado;
        this.mantenimiento = mantenimiento;
        this.estacionamiento = estacionamiento;
        this.otros = otros;
        this.totalAutos = totalAutos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGasolina() {
        return gasolina;
    }

    public void setGasolina(String gasolina) {
        this.gasolina = gasolina;
    }

    public String getLavado() {
        return lavado;
    }

    public void setLavado(String lavado) {
        this.lavado = lavado;
    }

    public String getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(String mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public String getEstacionamiento() {
        return estacionamiento;
    }

    public void setEstacionamiento(String estacionamiento) {
        this.estacionamiento = estacionamiento;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public String getTotalAutos() {
        return totalAutos;
    }

    public void setTotalAutos(String totalAutos) {
        this.totalAutos = totalAutos;
    }
}
