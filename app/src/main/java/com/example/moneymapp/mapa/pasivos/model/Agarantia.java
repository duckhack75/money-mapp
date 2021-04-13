package com.example.moneymapp.mapa.pasivos.model;

public class Agarantia {
    private String correo;
    private String tarjetaCredito;
    private String prestamoPersonal;
    private String totalSinGarantia;

    public Agarantia() {
    }

    public Agarantia(String correo, String tarjetaCredito, String prestamoPersonal, String totalSinGarantia) {
        this.correo = correo;
        this.tarjetaCredito = tarjetaCredito;
        this.prestamoPersonal = prestamoPersonal;
        this.totalSinGarantia = totalSinGarantia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setTarjetaCredito(String tarjetaCredito) {
        this.tarjetaCredito = tarjetaCredito;
    }

    public String getPrestamoPersonal() {
        return prestamoPersonal;
    }

    public void setPrestamoPersonal(String prestamoPersonal) {
        this.prestamoPersonal = prestamoPersonal;
    }

    public String getTotalSinGarantia() {
        return totalSinGarantia;
    }

    public void setTotalSinGarantia(String totalSinGarantia) {
        this.totalSinGarantia = totalSinGarantia;
    }
}
