package com.example.moneymapp.presupuesto.model;

public class Servicio {

    private String correo;
    private String gas;
    private String luz;
    private String agua;
    private String telefono;
    private String internet;
    private String cable;
    private String lavanderia;
    private String otrosServicios;
    private String totalServicios;

    public Servicio() {
    }

    public Servicio(String correo, String gas, String luz, String agua, String telefono,
                    String internet, String cable, String lavanderia, String otrosServicios, String totalServicios) {
        this.correo = correo;
        this.gas = gas;
        this.luz = luz;
        this.agua = agua;
        this.telefono = telefono;
        this.internet = internet;
        this.cable = cable;
        this.lavanderia = lavanderia;
        this.otrosServicios = otrosServicios;
        this.totalServicios = totalServicios;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getLuz() {
        return luz;
    }

    public void setLuz(String luz) {
        this.luz = luz;
    }

    public String getAgua() {
        return agua;
    }

    public void setAgua(String agua) {
        this.agua = agua;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getCable() {
        return cable;
    }

    public void setCable(String cable) {
        this.cable = cable;
    }

    public String getLavanderia() {
        return lavanderia;
    }

    public void setLavanderia(String lavanderia) {
        this.lavanderia = lavanderia;
    }

    public String getOtrosServicios() {
        return otrosServicios;
    }

    public void setOtrosServicios(String otrosServicios) {
        this.otrosServicios = otrosServicios;
    }

    public String getTotalServicios() {
        return totalServicios;
    }

    public void setTotalServicios(String totalServicios) {
        this.totalServicios = totalServicios;
    }
}
