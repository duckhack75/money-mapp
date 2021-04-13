package com.example.moneymapp.mapa.activos.model;

public class Liquido {
    private String correo;
    private String cuentaBanco;
    private String otrasInstitucionesFinancieras;
    private String cartera;
    private String totalLiquidos;


    public Liquido() {
    }

    public Liquido(String correo, String cuentaBanco, String otrasInstitucionesFinancieras, String cartera, String totalLiquidos) {
        this.correo = correo;
        this.cuentaBanco = cuentaBanco;
        this.otrasInstitucionesFinancieras = otrasInstitucionesFinancieras;
        this.cartera = cartera;
        this.totalLiquidos = totalLiquidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCuentaBanco() {
        return cuentaBanco;
    }

    public void setCuentaBanco(String cuentaBanco) {
        this.cuentaBanco = cuentaBanco;
    }

    public String getOtrasInstitucionesFinancieras() {
        return otrasInstitucionesFinancieras;
    }

    public void setOtrasInstitucionesFinancieras(String otrasInstitucionesFinancieras) {
        this.otrasInstitucionesFinancieras = otrasInstitucionesFinancieras;
    }

    public String getCartera() {
        return cartera;
    }

    public void setCartera(String cartera) {
        this.cartera = cartera;
    }

    public String getTotalLiquidos() {
        return totalLiquidos;
    }

    public void setTotalLiquidos(String totalLiquidos) {
        this.totalLiquidos = totalLiquidos;
    }
}
