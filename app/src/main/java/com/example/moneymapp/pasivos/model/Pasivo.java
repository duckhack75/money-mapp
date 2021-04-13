package com.example.moneymapp.pasivos.model;

public class Pasivo {
    private String correo;
    private String nombrePasivo;
    private String montoInversion;
    private String gananciaPasivos;
    private String montoTotalPasivos;

    public Pasivo() {
    }

    public Pasivo(String correo, String nombrePasivo, String montoInversion, String gananciaPasivos, String montoTotalPasivos) {
        this.correo = correo;
        this.nombrePasivo = nombrePasivo;
        this.montoInversion = montoInversion;
        this.gananciaPasivos = gananciaPasivos;
        this.montoTotalPasivos = montoTotalPasivos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombrePasivo() {
        return nombrePasivo;
    }

    public void setNombrePasivo(String nombrePasivo) {
        this.nombrePasivo = nombrePasivo;
    }

    public String getMontoInversion() {
        return montoInversion;
    }

    public void setMontoInversion(String montoInversion) {
        this.montoInversion = montoInversion;
    }

    public String getGananciaPasivos() {
        return gananciaPasivos;
    }

    public void setGananciaPasivos(String gananciaPasivos) {
        this.gananciaPasivos = gananciaPasivos;
    }

    public String getMontoTotalPasivos() {
        return montoTotalPasivos;
    }

    public void setMontoTotalPasivos(String montoTotalPasivos) {
        this.montoTotalPasivos = montoTotalPasivos;
    }
}
