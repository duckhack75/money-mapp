package com.example.moneymapp.mapa.entradas.model;

public class Eotro {
    private String correo;
    private String cuentas;
    private String otrasEntradas;
    private String totalOtrasEntradas;

    public Eotro() {
    }

    public Eotro(String correo, String cuentas, String otrasEntradas, String totalOtrasEntradas) {
        this.correo = correo;
        this.cuentas = cuentas;
        this.otrasEntradas = otrasEntradas;
        this.totalOtrasEntradas = totalOtrasEntradas;
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

    public String getOtrasEntradas() {
        return otrasEntradas;
    }

    public void setOtrasEntradas(String otrasEntradas) {
        this.otrasEntradas = otrasEntradas;
    }

    public String getTotalOtrasEntradas() {
        return totalOtrasEntradas;
    }

    public void setTotalOtrasEntradas(String totalOtrasEntradas) {
        this.totalOtrasEntradas = totalOtrasEntradas;
    }
}
