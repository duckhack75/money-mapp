package com.example.moneymapp.pasivos.model;

public class TotalPasivo {
    private String correo;
    private String diaInicio;
    private String mesInicio;
    private String anioInicio;
    private String gananciaTotalPasivos;

    public TotalPasivo() {
    }

    public TotalPasivo(String correo, String diaInicio, String mesInicio, String anioInicio, String gananciaTotalPasivos) {
        this.correo = correo;
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.anioInicio = anioInicio;
        this.gananciaTotalPasivos = gananciaTotalPasivos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(String diaInicio) {
        this.diaInicio = diaInicio;
    }

    public String getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(String mesInicio) {
        this.mesInicio = mesInicio;
    }

    public String getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(String anioInicio) {
        this.anioInicio = anioInicio;
    }

    public String getGananciaTotalPasivos() {
        return gananciaTotalPasivos;
    }

    public void setGananciaTotalPasivos(String gananciaTotalPasivos) {
        this.gananciaTotalPasivos = gananciaTotalPasivos;
    }
}

