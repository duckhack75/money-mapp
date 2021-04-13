package com.example.moneymapp.mapa.model;

public class MapaModel {
    private String correo;
    private String activosTotal;
    private String pasivosTotal;
    private String entradasTotal;
    private String salidasTotal;
    private String riquezaNeta;
    private String diaInicio;
    private String mesInicio;
    private String anioInicio;
    private String flujoEfectivoNeto;

    public MapaModel() {
    }

    public MapaModel(String correo, String activosTotal, String pasivosTotal, String entradasTotal,
                     String salidasTotal, String riquezaNeta, String diaInicio, String mesInicio,
                     String anioInicio, String flujoEfectivoNeto) {
        this.correo = correo;
        this.activosTotal = activosTotal;
        this.pasivosTotal = pasivosTotal;
        this.entradasTotal = entradasTotal;
        this.salidasTotal = salidasTotal;
        this.riquezaNeta = riquezaNeta;
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.anioInicio = anioInicio;
        this.flujoEfectivoNeto = flujoEfectivoNeto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getActivosTotal() {
        return activosTotal;
    }

    public void setActivosTotal(String activosTotal) {
        this.activosTotal = activosTotal;
    }

    public String getPasivosTotal() {
        return pasivosTotal;
    }

    public void setPasivosTotal(String pasivosTotal) {
        this.pasivosTotal = pasivosTotal;
    }

    public String getEntradasTotal() {
        return entradasTotal;
    }

    public void setEntradasTotal(String entradasTotal) {
        this.entradasTotal = entradasTotal;
    }

    public String getSalidasTotal() {
        return salidasTotal;
    }

    public void setSalidasTotal(String salidasTotal) {
        this.salidasTotal = salidasTotal;
    }

    public String getRiquezaNeta() {
        return riquezaNeta;
    }

    public void setRiquezaNeta(String riquezaNeta) {
        this.riquezaNeta = riquezaNeta;
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

    public String getFlujoEfectivoNeto() {
        return flujoEfectivoNeto;
    }

    public void setFlujoEfectivoNeto(String flujoEfectivoNeto) {
        this.flujoEfectivoNeto = flujoEfectivoNeto;
    }
}
