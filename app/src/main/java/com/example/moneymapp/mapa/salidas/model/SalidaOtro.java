package com.example.moneymapp.mapa.salidas.model;

public class SalidaOtro {
    private String correo;
    private String cuentas;
    private String ahorro;
    private String otrasSalidas;
    private String totalOtrasSalidas;

    public SalidaOtro() {
    }

    public SalidaOtro(String correo, String cuentas, String ahorro, String otrasSalidas, String totalOtrasSalidas) {
        this.correo = correo;
        this.cuentas = cuentas;
        this.ahorro = ahorro;
        this.otrasSalidas = otrasSalidas;
        this.totalOtrasSalidas = totalOtrasSalidas;
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

    public String getAhorro() {
        return ahorro;
    }

    public void setAhorro(String ahorro) {
        this.ahorro = ahorro;
    }

    public String getOtrasSalidas() {
        return otrasSalidas;
    }

    public void setOtrasSalidas(String otrasSalidas) {
        this.otrasSalidas = otrasSalidas;
    }

    public String getTotalOtrasSalidas() {
        return totalOtrasSalidas;
    }

    public void setTotalOtrasSalidas(String totalOtrasSalidas) {
        this.totalOtrasSalidas = totalOtrasSalidas;
    }
}
