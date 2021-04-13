package com.example.moneymapp.mapa.activos.model;

public class Posesion {
    private String correo;
    private String vivienda;
    private String automovil;
    private String muebles;
    private String arte;
    private String joyas;
    private String seguros;
    private String otrasPosesiones;
    private String totalPosesiones;

    public Posesion() {
    }

    public Posesion(String correo, String vivienda, String automovil, String muebles, String arte,
                    String joyas, String seguros, String otrasPosesiones, String totalPosesiones) {
        this.correo = correo;
        this.vivienda = vivienda;
        this.automovil = automovil;
        this.muebles = muebles;
        this.arte = arte;
        this.joyas = joyas;
        this.seguros = seguros;
        this.otrasPosesiones = otrasPosesiones;
        this.totalPosesiones = totalPosesiones;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getVivienda() {
        return vivienda;
    }

    public void setVivienda(String vivienda) {
        this.vivienda = vivienda;
    }

    public String getAutomovil() {
        return automovil;
    }

    public void setAutomovil(String automovil) {
        this.automovil = automovil;
    }

    public String getMuebles() {
        return muebles;
    }

    public void setMuebles(String muebles) {
        this.muebles = muebles;
    }

    public String getArte() {
        return arte;
    }

    public void setArte(String arte) {
        this.arte = arte;
    }

    public String getJoyas() {
        return joyas;
    }

    public void setJoyas(String joyas) {
        this.joyas = joyas;
    }

    public String getSeguros() {
        return seguros;
    }

    public void setSeguros(String seguros) {
        this.seguros = seguros;
    }

    public String getOtrasPosesiones() {
        return otrasPosesiones;
    }

    public void setOtrasPosesiones(String otrasPosesiones) {
        this.otrasPosesiones = otrasPosesiones;
    }

    public String getTotalPosesiones() {
        return totalPosesiones;
    }

    public void setTotalPosesiones(String totalPosesiones) {
        this.totalPosesiones = totalPosesiones;
    }
}
